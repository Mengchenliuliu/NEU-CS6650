package Dao;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class SwipeDao {
  private static final String LIKEQUERY = "insert into likes(swiperId, numLikes, numDislikes) values (?,1,0) on duplicate key update numLikes = numLikes + 1";
  private static final String DISLIKEQUERY = "insert into likes(swiperId, numLikes, numDislikes) values (?,0,1) on duplicate key update numDislikes = numDislikes + 1";

  public static void insertLikes(int swiperId, String direction){
    String query = direction.equals("right")? LIKEQUERY : DISLIKEQUERY;
    try{
      Connection conn = SwipeDataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setInt(1,swiperId);
      ps.executeUpdate();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

}
