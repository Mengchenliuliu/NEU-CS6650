package clientPart2;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Part2SwipeThread implements Runnable{
  public static final String BASE_PATH = "http://localhost:8080/Server_war_exploded";
//  public static final String BASE_PATH = "http://54.188.69.32:8080/assignment1_war";
  public static final String[] directions = {"left","right"};
  private AtomicInteger successCounter;
  private AtomicInteger failCounter;
  private CountDownLatch countDownLatch;
  private SwipeApi apiInstance;
  private List<PostRecord> responseTimes;
  private AtomicLong sumOfResponse;


  public Part2SwipeThread(AtomicInteger successCounter, AtomicInteger failCounter, CountDownLatch countDownLatch,
      List<PostRecord> responseTimes, AtomicLong sumOfResponse) {
    this.successCounter = successCounter;
    this.failCounter = failCounter;
    this.countDownLatch = countDownLatch;
    this.responseTimes = responseTimes;
    this.sumOfResponse = sumOfResponse;
    apiInstance = new SwipeApi();
    apiInstance.setApiClient(new ApiClient().setBasePath(BASE_PATH));
  }

  @Override
  public void run() {
     // SwipeDetails | response details
    String leftorright = directions[(int)Math.random()*(2)];
    SwipeDetails body = ProducerBody.generator();
    ApiResponse<Void> res = null;
    long startTime = System.currentTimeMillis();

    try {
      res = apiInstance.swipeWithHttpInfo(body, leftorright);
      if(res.getStatusCode()/100 == 2){
        successCounter.getAndIncrement();
      }else{
        //when we receive wrong code, we need to retry the request up to 5 times.
        int count = 0;
        boolean ifRetrySuccess = false;
        while(count < 5){
          res = apiInstance.swipeWithHttpInfo(body, leftorright);
          if(res.getStatusCode()/100 >= 4){
            count++;
          }else{
            ifRetrySuccess = true;
            successCounter.getAndIncrement();
            break;
          }
        }
        if(!ifRetrySuccess){
          failCounter.getAndIncrement();
        }
      }

    } catch (ApiException e) {
      System.err.println("Exception when calling SwipeApi#swipe");
      e.printStackTrace();
      failCounter.getAndIncrement();
    }
    if(res != null) {
      long endTime = System.currentTimeMillis();
      long latency = endTime - startTime;
      PostRecord record = new PostRecord(startTime, "POST", latency, res.getStatusCode());
      sumOfResponse.addAndGet(latency);
      responseTimes.add(record);
    }
    this.countDownLatch.countDown();
  }
}
