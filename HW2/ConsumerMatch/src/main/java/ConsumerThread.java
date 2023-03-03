import com.google.gson.Gson;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerThread implements Runnable{
  public static final String QUEUE_NAME = "match";

  private final Connection connection;
  private final ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicInteger>> map;
  private final CountDownLatch countDownLatch;
  private final Gson gson = new Gson();

  public ConsumerThread(Connection connection, ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicInteger>> map,
      CountDownLatch countDownLatch) {
    this.connection = connection;
    this.map = map;
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void run() {
    try {
      Channel channel = connection.createChannel();
      //we don't need to create queue, because queue has been created in servlet.
      channel.basicQos(1);

      Consumer consumer = new DefaultConsumer(channel){
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope,
            BasicProperties properties, byte[] body) throws IOException {
          //we get the object of DatingContend.
          DatingContend contend = gson.fromJson(new String(body),DatingContend.class);
          if(contend.getDirection().equalsIgnoreCase("right")){
            String swiper = contend.getSwiper();
            String swipee = contend.getSwipee();
            if(!map.get(swiper).containsKey(swipee)){
              map.get(swiper).put(swipee, new AtomicInteger(0));
            }
            map.get(swiper).get(swipee).incrementAndGet();
          }
        }
      };

      channel.basicConsume(QUEUE_NAME, true, consumer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
