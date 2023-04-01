import Dao.SwipeDao;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import model.DatingContend;

public class ConsumerThread implements Runnable{
  public static final String QUEUE_NAME = "likes";
  private Connection connection;
  private CountDownLatch countDownLatch;
  private Gson gson = new Gson();

  public ConsumerThread(Connection connection,
      CountDownLatch countDownLatch) {
    this.connection = connection;
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void run() {
    try {
      Channel channel = connection.createChannel();
//      channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT,true);
//      //we don't need to create queue, because queue has been created in servlet.
//      channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

      Consumer consumer = new DefaultConsumer(channel){
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope,
            BasicProperties properties, byte[] body) throws IOException {
          //we get the object of model.DatingContend.
          DatingContend contend = gson.fromJson(new String(body), DatingContend.class);
          int swiper = Integer.parseInt(contend.getSwiper());
         //we process contend to calculate like and dislike.
          SwipeDao.insertLikes(swiper, contend.getDirection());
        }
      };

      channel.basicConsume(QUEUE_NAME, true, consumer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
