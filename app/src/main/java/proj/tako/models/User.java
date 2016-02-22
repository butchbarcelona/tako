package proj.tako.models;

/**
 * Created by mbarcelona on 2/22/16.
 */
public class User {

  String username, firstname, lastname, middlename, account, email;
  int id, courseid;

  public User(int id, String username, String firstname, String lastname, String middlename, String account, String email, int courseid){
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.middlename = middlename;
    this.account = account;
    this.email = email;
    this.courseid = courseid;

  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getMiddlename() {
    return middlename;
  }

  public void setMiddlename(String middlename) {
    this.middlename = middlename;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCourseid() {
    return courseid;
  }

  public void setCourseid(int courseid) {
    this.courseid = courseid;
  }
}
