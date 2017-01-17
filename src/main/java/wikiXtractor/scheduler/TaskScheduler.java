package wikiXtractor.scheduler;


import wikiXtractor.api.cli.CLICommand;
import wikiXtractor.util.Loggable;
import wikiXtractor.worker.Worker;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Schedule and dispatch jobs from the queue
 *
 * @author Dinu Ganea
 */
public class TaskScheduler implements Loggable {

    // Total number of threads we can start along the main one
    protected static final int WORKERS_NR = 2;

    // Thread instances
    protected static final ArrayList<Worker> workers = new ArrayList<>();

    // Set the number of threads which can acquire permission at the same time
    protected static final Semaphore semaphore = new Semaphore(WORKERS_NR);

    // Track number of threads that are still running
    protected static int running_workers = 0;

    // we'll save all executed commands
    protected HashMap<String, Boolean> executedCommands;

    // Given tasks/commands
    protected List<CLICommand> taskQueue;

    /**
     * Only instantiate needed structures
     */
    public TaskScheduler() {
        taskQueue = new ArrayList<>();
        executedCommands = new HashMap<>();
    }

    /**
     * Add command to queue
     *
     * @param command Command implementation
     * @return TaskScheduler instance
     */
    public TaskScheduler addTask(CLICommand command) {
        taskQueue.add(command);
        return this;
    }

    /**
     * Execute all tasks in the queue
     *
     * @throws Exception
     */
    public void execute() throws Exception {

        ListIterator<CLICommand> iterator = taskQueue.listIterator();

        CLICommand command;

        while (iterator.hasNext()) {

            // Destroy threads if they're done.
            // Will try only when the semaphore has available permits
            if (semaphore.availablePermits() > 0) {
                releaseWorkers();
            }

            command = iterator.next();

            // If command has no dependencies, try to execute it
            if (currentDependencies(command).size() == 0) {
                // Check if at least one thread is available
                if (semaphore.availablePermits() > 0) {
                    // hit the road command...
                    startFreeWorker(command);
                    // .. and no don't you come back no more...
                    iterator.remove();
                }
            }

            // It we still have commands to execute
            if (!iterator.hasNext()) {
                // .. reset iterator
                iterator = taskQueue.listIterator();
                // if one thread is still running, that means, that it causes dependencies for other commands, so will wait till it's done
                if (running_workers == 1) {
                    waitForThreads();
                    // but, if no thread is running, we may have cyclic dependencies and/oder commands not added to queue
                } else if (running_workers == 0) {
                    throw new Exception("Could not dispatch any more tasks due to dependencies!");
                }
            }

        }
    }


    /**
     * Get all dependencies that a command has at the current time
     *
     * @param command Command implementation
     * @return Set of dependencies as String of class names
     */
    public Set<String> currentDependencies(CLICommand command) {

        HashSet<String> dependencies = new HashSet<>();

        // Get all dependencies of the current command
        Iterator<String> commandNames = command.getDependencies().iterator();

        // Parse each dependency and check if it was already executed
        while (commandNames.hasNext()) {
            String cmdName = commandNames.next();
            if (executedCommands.get(cmdName) == null) {
                dependencies.add(cmdName);
            }
        }

        return dependencies;
    }

    /**
     * Check if processes are terminated, and remove them from the workers list
     */
    public void releaseWorkers() {
        for (Iterator<Worker> iterator = workers.iterator(); iterator.hasNext(); ) {
            Worker worker = iterator.next();
            if (worker.isTerminated()) {
                // Mark job as done, so we'll get rid of this dependency
                executedCommands.put(worker.getCommand().getClass().getSimpleName(), true);
                running_workers--;
                iterator.remove();
            }
        }
    }

    /**
     * Start a thread that will execute given command
     *
     * @param command Command that needs to be executed
     * @throws InterruptedException
     */
    public void startFreeWorker(CLICommand command) throws InterruptedException {
        if (workers.size() < WORKERS_NR) {
            Worker worker = new Worker(semaphore, command);
            running_workers++;
            workers.add(worker);
            worker.start();
        }
    }


    /**
     * For each thread that's still running, wait till it's done.
     *
     * @throws InterruptedException
     */
    public void waitForThreads() throws InterruptedException {
        for (Worker worker : workers) {
            worker.join();
        }
    }

}
