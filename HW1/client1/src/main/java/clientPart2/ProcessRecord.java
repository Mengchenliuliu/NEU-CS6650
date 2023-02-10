package clientPart2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessRecord {


 //we need to sort the list according to startTime.
  public static void sortRecord(List<PostRecord> records){
    Collections.sort(records, new Comparator<PostRecord>() {
      @Override
      public int compare(PostRecord o1, PostRecord o2) {
        return (int)(o1.getLatency() - o2.getLatency());
      }
    });
  }

  //we need to write the result of every request to corresponding file.
  public static void writeResult(List<PostRecord> records, String file) throws IOException {
          FileWriter fileWriter = new FileWriter(new File(file),true);
          try {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("startTime" + ",requestType" + ",latency" + ",responseCode");
            bufferedWriter.newLine();
            for(PostRecord record : records){
              bufferedWriter.write(record.getStartTime() + "," + record.getRequestType() + ","
                  + record.getLatency() + "," + record.getResponseCode());
              bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
          }catch(FileNotFoundException e){
            System.out.println("File Not Found");
          }

          fileWriter.close();
  }

  public static void writePerformance(List<PostRecord> records, String file) throws IOException {
    FileWriter fileWriter = new FileWriter(new File(file),true);
    long startTime = records.get(0).getStartTime();
    Map<Long, Long> map = new HashMap<>();

    for(PostRecord record : records){
      long interval = (record.getStartTime() - startTime + record.getLatency())/1000 + 1;
      map.put(interval, map.getOrDefault(interval, 0L) + 1);
    }

    try {
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write("seconds" + ",throughput/second");
      bufferedWriter.newLine();
      for(Long key : map.keySet()){
        bufferedWriter.write(key + "," + map.get(key));
        bufferedWriter.newLine();
      }
      bufferedWriter.flush();
      bufferedWriter.close();
    }catch(FileNotFoundException e){
      System.out.println("File Not Found");
    }
    fileWriter.close();

  }

  public static void printResult(List<PostRecord> records, AtomicLong sumResponse, AtomicInteger successNumber, AtomicInteger failNumber){
    double mean = (double)sumResponse.get()/(successNumber.get() + failNumber.get());
    System.out.println("The mean response time is " + mean + " millisecond.");
    System.out.println("The median response time is " + findMiddle(records));
    long throughput1 = (long)((successNumber.get() + failNumber.get())/(double)(sumResponse.get()/1000));
    System.out.println("The throughput is " + throughput1 + " per seconds.");
    System.out.println("The p99 is " + records.get((int)Math.ceil(0.99 * records.size() - 1)).getLatency());
    System.out.println("The min response time is " + records.get(0).getLatency());
    System.out.println("The max response time is " + records.get(records.size() - 1).getLatency());
  }

  public static double findMiddle(List<PostRecord> records){
    long middle1 = records.get(records.size()/2).getLatency();
    long middle2 = records.get((records.size() - 1)/2).getLatency();
    double middle = (middle1 + middle2)*0.5;
    return middle;
  }







}
