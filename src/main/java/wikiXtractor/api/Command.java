package wikiXtractor.api;


public interface Command<T> {

    public T execute() throws Exception;

}
