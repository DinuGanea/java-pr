package wikiXtractor.worker;

import wikiXtractor.api.cli.CLICommand;
import wikiXtractor.util.Loggable;

import java.util.concurrent.Semaphore;

/**
 * Independent process/machine, that can execute a given command
 *
 * @author Dinu Ganea
 */
public class Worker extends Thread implements Loggable {

    // red-yellow-green thing...
    protected final Semaphore semaphore;

    // command that needs to be executed
    protected CLICommand command;


    /**
     * Prepare a new process
     *
     * @param semaphore Permits manager
     * @param command CLICommand that needs to be excuted
     */
    public Worker(Semaphore semaphore, CLICommand command) {
        super(command, command.getName());
        this.command = command;
        this.semaphore = semaphore;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        try {
            logger.info("Waiting to acquire lock");
            // Wait for permit
            semaphore.acquire();
            logger.info("Lock acquired!");

            super.run();

        } catch (Exception e) {
            logger.error("Lock acquiring has caused an exception. Boiling out!");
            logger.error(e.getMessage(), e);
        }

        logger.info("Releasing lock");
        // give the permit back
        semaphore.release();

    }

    /**
     * @return Command assigned to the process
     */
    public CLICommand getCommand() {
        return command;
    }

    /**
     * @return Whether the process is done or not
     */
    public boolean isTerminated() {
        return this.getState() == State.TERMINATED;
    }
}