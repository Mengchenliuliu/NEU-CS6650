import java.util.Objects;

public class DatingContend {
  private String swiper;
  private String swipee;
  private String comment;

  public String getSwiper() {
    return swiper;
  }

  public void setSwiper(String swiper) {
    this.swiper = swiper;
  }

  public String getSwipee() {
    return swipee;
  }

  public void setSwipee(String swipee) {
    this.swipee = swipee;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean ifValidRequest(){
    try {
      int numberOfSwiper = Integer.parseInt(this.swiper);
      if(numberOfSwiper < 1 || numberOfSwiper > 5000){
        return false;
      }
    }catch (NumberFormatException e){
      return false;
    }

    try {
      int numberOfSwipee = Integer.parseInt(this.swipee);
      if(numberOfSwipee < 1 || numberOfSwipee > 1000000){
        return false;
      }
    }catch (NumberFormatException e){
      return false;
    }

    if(this.comment.length() > 256){
      return false;
    }
    return true;
  }

}
