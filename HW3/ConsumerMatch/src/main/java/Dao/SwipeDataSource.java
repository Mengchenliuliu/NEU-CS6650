package Dao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SwipeDataSource {
  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource hds;

  static{
    try{
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");
      config.setJdbcUrl("jdbc:mysql://localhost:3306/SwipeDataBase");
      config.setUsername("root");
      config.setPassword("LMClw1142!");
      config.addDataSourceProperty("cachePrepstmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      config.setAutoCommit(true);
      hds = new HikariDataSource(config);
    }catch(Exception e){
      e.printStackTrace();
    }

  }

  public static Connection getConnection() throws SQLException {
    return hds.getConnection();
  }


}