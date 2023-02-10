package clientPart1;

import io.swagger.client.ApiClient;
import io.swagger.client.api.SwipeApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
  public static final int ThreadSize = 1;
  public static final int LOAD_SIZE = 10000;
  public static AtomicInteger SUCCESSNUMBER = new AtomicInteger(0);
  public static AtomicInteger FAILNUMBER = new AtomicInteger(0);


  public static void main(String[] args) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(LOAD_SIZE);
    ExecutorService executor = new ScheduledThreadPoolExecutor(ThreadSize);


    long startTime = System.currentTimeMillis();

    for(int j = 0; j < LOAD_SIZE; j++){
      SwipeThread thread = new SwipeThread(SUCCESSNUMBER, FAILNUMBER, countDownLatch);
      executor.execute(thread);
    }

    countDownLatch.await();
    executor.shutdown();

    long endTime = System.currentTimeMillis();
    double seconds = (double)(endTime - startTime)/1000;
    double throughput = (SUCCESSNUMBER.get() + FAILNUMBER.get())/seconds;

    System.out.println("The number of successful requests sent is " + SUCCESSNUMBER.get());
    System.out.println("The number of unsuccessful requests sent is " + FAILNUMBER.get());
    System.out.println("The total run time for all threads to complete is " + seconds + "s");
    System.out.println("The total throughput in requests per second is " + throughput);

  }

}
