package Dao;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class SwipeDao {
  private static final String MATCHQUERY = "insert into matches(swiperId, swipeeId, num) values (?,?,1) on duplicate key update num = num + 1";

  public static void insertMatch(int swiperId, int swipeeId){

    try{
      Connection conn = SwipeDataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement(MATCHQUERY);
      ps.setInt(1,swiperId);
      ps.setInt(2,swipeeId);
      ps.executeUpdate();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

}
