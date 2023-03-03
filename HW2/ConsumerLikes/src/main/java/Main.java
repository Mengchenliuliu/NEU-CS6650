import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
  public static ConcurrentHashMap<Integer, Pair> map = new ConcurrentHashMap<>();
  public static final int SWIPER_MAX = 5000;
  public static final int THREAD_NUM = 20;


  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
    //init the map, so when we put (key, value) in the map, we don't need to check whether key is contained in map
   for(int i = 1; i <= SWIPER_MAX; i++){
     map.put(i, new Pair(new AtomicInteger(0), new AtomicInteger(0)));
   }

    CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
   //init connection
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setUsername("mengchen");
    connectionFactory.setPassword("mengchen");
    connectionFactory.setVirtualHost("/hw2");
    connectionFactory.setHost("54.173.155.71");//This ip will change
    connectionFactory.setPort(5672);
    Connection connection = connectionFactory.newConnection();

    for(int i = 0; i < THREAD_NUM; i++){
      Runnable thread = new ConsumerThread(connection, map, countDownLatch);
      new Thread(thread).start();
    }
    countDownLatch.await();
//    printMap(map);
    System.out.println("the number of likes and dislikes has been calculated!");
  }


}
