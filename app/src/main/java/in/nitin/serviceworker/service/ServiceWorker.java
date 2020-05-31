package in.nitin.serviceworker.service;

import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 * ServiceWorker class
 * */
public class ServiceWorker {

    private static String TAG = "ServiceWorker";

    private String serviceName;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public ServiceWorker(String serviceName) {
        this.serviceName = serviceName;
    }


    /**
     * @param task: It is an interface which contains three methods as follow:-
     *              a). onExecute()
     *              b). onTaskComplete()
     *              c). onError()
     */

    public void addTask(Task task) {

        Log.i(TAG, " task performed by: " + serviceName);

        /*
         * here we using CompletableFuture so we don't need to write a extra class which implement Callable<T>
         */
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(task::onExecute, executorService);

        try {
            /*
             * this will run on main thread to show the result on UI
             * */
            task.onTaskComplete(completableFuture.get());
        } catch (ExecutionException | InterruptedException e) {
            /*
             * to show exception on UI
             * */
            task.onError(e);
        }
    }

    /**
     * use to shutdown the executor service
     */
    public void stop() {
        Log.i(TAG, serviceName + " shut down");
        executorService.shutdown();
    }

}
