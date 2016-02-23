package proj.mtc.models;

public class Reservation {

  String start;
  String end;
  String date;

  int id, attendants, userId, venue;

  String status, event, remarks, contactNum, strVenue;

  /*
  * {"id":98,"attendants":100,"user_id":18
  * ,"dateTimeStart":"2016-01-02 10:00:00"
  * ,"dateTimeEnd":"2016-01-02 11:00:00"
  * ,"status":"disapproved"
  * ,"created_at":"2016-02-21 13:16:50"
  * ,"updated_at":"2016-02-21 13:41:40","purpose":"10","venue":3
  * ,"event":"test","notify":1,"dateStart":"2016-01-02"
  * ,"remarks":"","contactnumber":"100","process_id":1}
  * */

  public Reservation(){

  }

  public Reservation(int id, int attendants, int userId, int venue
    , String start, String end, String date, String status, String event, String remarks,
                     String contactNum){
    this.id = id;
    this.attendants = attendants;
    this.userId = userId;
    this.venue = venue;
    this.start = start;
    this.end = end;
    this.date = date;
    this.status = status;
    this.event = event;
    this.remarks = remarks;
    this.contactNum = contactNum;
  }

  public Reservation(String start, String end){
    this.start = start;
    this.end = end;
  }

  public String getStrVenue() {
    return strVenue;
  }

  public void setStrVenue(String strVenue) {
    this.strVenue = strVenue;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAttendants() {
    return attendants;
  }

  public void setAttendants(int attendants) {
    this.attendants = attendants;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getVenue() {
    return venue;
  }

  public void setVenue(int venue) {
    this.venue = venue;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getContactNum() {
    return contactNum;
  }

  public void setContactNum(String contactNum) {
    this.contactNum = contactNum;
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
