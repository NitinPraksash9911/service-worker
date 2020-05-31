package in.nitin.serviceworker.service;

public interface Task<T> {

    T onExecute();

    void onTaskComplete(T result);

    void onError(Exception exception);


}
