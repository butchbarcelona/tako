package proj.tako.models;

/**
 * Created by mbarcelona on 2/21/16.
 */
public class Reservation {

  String start;
  String end;

  public Reservation(String start, String end){
    this.start = start;
    this.end = end;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }
}
