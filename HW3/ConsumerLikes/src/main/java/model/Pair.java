package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Pair {
  private AtomicInteger like;
  private AtomicInteger dislike;

  public Pair(AtomicInteger like, AtomicInteger dislike) {
    this.like = like;
    this.dislike = dislike;
  }

  public AtomicInteger getLike() {
    return like;
  }

  public void setLike(AtomicInteger like) {
    this.like = like;
  }

  public AtomicInteger getDislike() {
    return dislike;
  }

  public void setDislike(AtomicInteger dislike) {
    this.dislike = dislike;
  }

  public void addLike(){
    like.incrementAndGet();
  }

  public void addDislike(){
    dislike.incrementAndGet();
  }
}
