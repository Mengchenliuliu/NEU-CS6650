package Dao;

import Model.MatchBody;
import Model.StatsBody;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SwipeDao {
  private static final String MATCHQUERY = "select swipeeId from matches where swiperId = ?";
  private static final String STATSQUERY = "select numLikes, numDislikes from likes where swiperId = ?";

  public static MatchBody getMatchMessage(Integer swiperId){
    List<String> matchList = new ArrayList<>();
    try{
      Connection conn = SwipeDataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement(MATCHQUERY);
      ps.setInt(1, swiperId);
      ResultSet resultSet = ps.executeQuery();
      while(resultSet.next()){
        matchList.add(String.valueOf(resultSet.getInt(1)));
      }

    }catch(Exception e){
      e.printStackTrace();
    }
    return new MatchBody(matchList);
  }

  public static StatsBody getStatsMessage(Integer swiperId){
    StatsBody statsBody = null;
    try{
      Connection conn = SwipeDataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement(STATSQUERY);
      ps.setInt(1, swiperId);
      ResultSet resultSet = ps.executeQuery();
      while(resultSet.next()){
        statsBody = new StatsBody(resultSet.getInt(1),resultSet.getInt(2));
      }

    }catch(Exception e){
      e.printStackTrace();
    }
    return statsBody;
  }

}
