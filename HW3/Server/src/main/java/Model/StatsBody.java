package Model;

public class StatsBody {
  private Integer numLikes;
  private Integer numDisLikes;

  public StatsBody(Integer like, Integer disLike) {
    this.numLikes = like;
    this.numDisLikes = disLike;
  }

  public long getNumLikes() {
    return numLikes;
  }

  public void setNumLikes(Integer numLikes) {
    this.numLikes = numLikes;
  }

  public long getNumDisLikes() {
    return numDisLikes;
  }

  public void setNumDisLikes(Integer numDisLikes) {
    this.numDisLikes = numDisLikes;
  }
}
