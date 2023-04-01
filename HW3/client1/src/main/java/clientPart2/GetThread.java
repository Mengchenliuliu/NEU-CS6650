package clientPart2;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.MatchesApi;
import io.swagger.client.api.StatsApi;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.MatchStats;
import io.swagger.client.model.Matches;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class GetThread implements Runnable{
  private int count;
  private long minLatency;
  private long maxLatency;
  private long sumLatency;
  public static final String BASE_PATH = "http://localhost:8080/Server_war_exploded";
//  public static final String BASE_PATH = "http://54.188.69.32:8080/assignment1_war";
  private final int MAXSWIPERID = 5000;
  private CountDownLatch countDownLatch;
  private ApiClient apiInstance;

  public GetThread(CountDownLatch countDownLatch){
    count = 0;
    minLatency = Long.MAX_VALUE;
    maxLatency = Long.MIN_VALUE;
    sumLatency = 0;
    this.countDownLatch = countDownLatch;
    apiInstance = new ApiClient();
    apiInstance.setBasePath(BASE_PATH);

  }

  public int getCount() {
    return count;
  }

  public long getMinLatency() {
    return minLatency;
  }

  public long getMaxLatency() {
    return maxLatency;
  }

  public long getSumLatency() {
    return sumLatency;
  }

  @Override
  public void run() {
    while(countDownLatch.getCount() > 1){
      String swiperId = String.valueOf(1 + (int)(Math.random()*(MAXSWIPERID)));
      int apiType = (int)Math.random()*2;
      long startTime = System.currentTimeMillis();
      long endTime;
      if(apiType == 1){
        MatchesApi matchesApi = new MatchesApi(apiInstance);
        try{
          ApiResponse<Matches> res = matchesApi.matchesWithHttpInfo(swiperId);
          endTime = System.currentTimeMillis();
        }catch(Exception e){
          e.printStackTrace();
          endTime = System.currentTimeMillis();
        }
      }else{
        StatsApi statsApi = new StatsApi(apiInstance);
        try{
          ApiResponse<MatchStats> res = statsApi.matchStatsWithHttpInfo(swiperId);
          endTime = System.currentTimeMillis();
        }catch(Exception e){
          e.printStackTrace();
          endTime = System.currentTimeMillis();
        }
      }
      long latency = endTime - startTime;
      count++;
      minLatency = Math.min(minLatency, latency);
      maxLatency = Math.max(maxLatency, latency);
      sumLatency += latency;

      //we need to get 5 request in 1 minute, so we sleep 200 ms when we get one.
      try{
        Thread.sleep(200);
      }catch (Exception e){
        throw new RuntimeException();
      }
    }

  }
}
