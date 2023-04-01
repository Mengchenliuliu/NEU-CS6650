package clientPart2;

import io.swagger.client.model.SwipeDetails;

public class ProducerBody {
  public static final int SWIPERLOW = 1;
  public static final int SWIPERHIGH = 5000;
  public static final int SWIPEELOW = 1;
  public static final int SWIPEEHIGH = 1000000;

  public static SwipeDetails generator(){
    SwipeDetails body = new SwipeDetails();
    String swiperNumber = String.valueOf(generatorNumber(SWIPERLOW,SWIPERHIGH));
    body.setSwiper(swiperNumber);
    String swipeeNumber = String.valueOf(generatorNumber(SWIPEELOW,SWIPEEHIGH));
    body.setSwipee(swipeeNumber);
    int lengthOfComment = generatorNumber(1,256);
    String comment = generatorString(lengthOfComment);
    body.setComment(comment);
    return body;
  }

  private static int generatorNumber(int low, int high){
    return low + (int)(Math.random()*(high - low + 1));
  }

  private static String generatorString(int length){
    String alphabetsInLowerCase = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < length; i++){
      int index = generatorNumber(0,51);
      sb.append(alphabetsInLowerCase.charAt(index));
    }
    return sb.toString();
  }

}
