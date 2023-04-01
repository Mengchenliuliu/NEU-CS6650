package clientPart2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Part2Main {
  public static final int ThreadSize = 100;s
  public static final int LOAD_SIZE = 500000;
  public static AtomicInteger SUCCESSNUMBER = new AtomicInteger(0);
  public static AtomicInteger FAILNUMBER = new AtomicInteger(0);
  public static List<PostRecord> RESPONSES = Collections.synchronizedList(new ArrayList<>());
  public static AtomicLong SUMOFRESPONSE = new AtomicLong();
  public static String csvPath = "./PostRecords.csv";
  public static String performancePath = "./performanceRecords.csv";


  public static  void main(String[] args) throws InterruptedException, IOException {
    CountDownLatch countDownLatch = new CountDownLatch(LOAD_SIZE);
    ExecutorService executor = new ScheduledThreadPoolExecutor(ThreadSize);

    long startTime = System.currentTimeMillis();
    for(int j = 0; j < LOAD_SIZE; j++){
      Part2SwipeThread thread = new Part2SwipeThread(SUCCESSNUMBER,FAILNUMBER,countDownLatch,RESPONSES,SUMOFRESPONSE);
      executor.execute(thread);
    }

    countDownLatch.await();
    GetThread getThread = new GetThread(countDownLatch);
    new Thread(getThread).start();
    countDownLatch.await();

    executor.shutdown();
    long endTime = System.currentTimeMillis();
    double seconds = (double)(endTime - startTime)/1000;
    double throughput = (SUCCESSNUMBER.get() + FAILNUMBER.get())/seconds;
    System.out.println("The number of successful requests sent is " + SUCCESSNUMBER.get());
    System.out.println("The number of unsuccessful requests sent is " + FAILNUMBER.get());
    System.out.println("The total run time for all threads to complete is " + seconds + "s");
    System.out.println("The total throughput in requests per second is " + throughput);

    ProcessRecord.writeResult(RESPONSES, csvPath);
    ProcessRecord.writePerformance(RESPONSES, performancePath);
    ProcessRecord.sortRecord(RESPONSES);
    ProcessRecord.printResult(RESPONSES, SUMOFRESPONSE, SUCCESSNUMBER, FAILNUMBER);
    System.out.println("This is the result of GET request.");
    System.out.println("The mean response time of GET request is " + getThread.getMaxLatency()/getThread.getCount() + " ms.");
    System.out.println("The min response time of GET request is " + getThread.getMinLatency() + " ms.");
    System.out.println("The max response time of GET request is " + getThread.getMaxLatency() + " ms.");

  }

}
