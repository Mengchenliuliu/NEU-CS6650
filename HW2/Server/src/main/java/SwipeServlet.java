import Model.DatingContend;
import RMQ.RMQChannelFactory;
import RMQ.RMQChannelPool;
import com.google.gson.Gson;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SwipeServlet", value = "/SwipeServlet")
public class SwipeServlet extends HttpServlet {

  private Gson gson = new Gson();
  public static final String queueName1 = "likes";
  public static final String queueName2 = "match";
  private static final int CHANNEL_SIZE = 100;
  private static final String EXCHANGE_NAME = "hw2Exchange";
  private RMQChannelPool pool;

  @Override
  public void init() throws ServletException {
    super.init();
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setUsername("mengchen");
    connectionFactory.setPassword("mengchen");
    connectionFactory.setVirtualHost("/hw2");
//    connectionFactory.setHost("54.173.155.71");
    connectionFactory.setPort(5672);
    Connection connection;
    try {
      connection = connectionFactory.newConnection();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) {
      throw new RuntimeException(e);
    }

    RMQChannelFactory channelFactory = new RMQChannelFactory(connection);
    this.pool = new RMQChannelPool(CHANNEL_SIZE, channelFactory);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("Invalid URL");
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write("It works!");
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/left" or "/right"
    if (urlPath == null || urlPath.length == 0 || urlPath.length != 2) {
      return false;
    }

    if (!urlPath[1].isEmpty()) {
      if (!"left".equalsIgnoreCase(urlPath[1]) && !"right".equalsIgnoreCase(urlPath[1])) {
        return false;
      }
      return true;
    }

    return true;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("Invalid URL");
      return;
    }
    response.setContentType("application/json");
    String direciton = urlParts[1];
    try {
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = request.getReader().readLine()) != null) {
        sb.append(s);
      }
      DatingContend contend = gson.fromJson(sb.toString(), DatingContend.class);
      contend.setDirection(direciton);

      if (!contend.ifValidRequest()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(gson.toJson("Invalid post!"));
        System.out.println("request is not valid");
        return;
      }

      if (ifSendToQueue(contend)) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(gson.toJson("Message was sent to queue successfully."));
      } else {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(gson.toJson("Message was not sent to queue!"));
      }
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  private boolean ifSendToQueue(DatingContend contend) {
    String message = gson.toJson(contend);

    Channel channel = null;
    try {
      channel = this.pool.borrowObject();

      channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);
      channel.queueDeclare(queueName1, false, false, false, null);
      channel.queueDeclare(queueName2, false, false, false, null);
      channel.queueBind(queueName1, EXCHANGE_NAME, "");
      channel.queueBind(queueName2, EXCHANGE_NAME, "");
      channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
      this.pool.returnObject(channel);
      return true;
    } catch (Exception e) {
      Logger.getLogger(SwipeServlet.class.getName()).info("Message is not sent to RabbitMQ!");
      return false;
    }
  }
}
