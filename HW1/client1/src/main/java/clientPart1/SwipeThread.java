package clientPart1;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SwipeThread implements Runnable{
//  public static final String BASE_PATH = "http://localhost:8080/assignment1";
  public static final String BASE_PATH = "http://54.188.69.32:8080/assignment1_war";
  public static final String[] directions = {"left","right"};
  private AtomicInteger successCounter;
  private AtomicInteger failCounter;
  private SwipeApi apiInstance;

  private CountDownLatch countDownLatch;
  public SwipeThread(AtomicInteger successCounter, AtomicInteger failCounter, CountDownLatch countDownLatch) {
    this.successCounter = successCounter;
    this.failCounter = failCounter;
    this.countDownLatch = countDownLatch;
    apiInstance = new SwipeApi();
    apiInstance.setApiClient(new ApiClient().setBasePath(BASE_PATH));
  }

  @Override
  public void run() {

     // SwipeDetails | response details

//    for(int i = 0; i < REQUEST_THREAD; i++){
      String leftorright = directions[(int)Math.random()*(2)];
      SwipeDetails body = ProducerBody.generator();
      ApiResponse<Void> res = null;
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
    this.countDownLatch.countDown();

  }

}
