package proj.tako.models;

public class Reservation {

  String start;
  String end;
  String date;

  public Reservation(){

  }
  public Reservation(String start, String end){
    this.start = start;
    this.end = end;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
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
