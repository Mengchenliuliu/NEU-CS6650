package Model;

import java.util.*;

public class MatchBody {
  List<String> matchList;

  public MatchBody(List<String> matchList) {
    this.matchList = matchList;
  }

  public List<String> getMatchList() {
    return matchList;
  }

  public void setMatchList(List<String> matchList) {
    this.matchList = matchList;
  }
}
