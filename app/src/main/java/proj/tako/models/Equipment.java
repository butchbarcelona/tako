package proj.tako.models;

/**
 * Created by mbarcelona on 2/22/16.
 */
public class Equipment {

  String name;
  int numAvailable;
  int id;
  int reserved;

  public Equipment(int id, String name, int numAvailable){
    this.name = name;
    this.id = id;
    this.numAvailable = numAvailable;

  }


  public int getReserved() {
    return reserved;
  }

  public void setReserved(int reserved) {
    this.reserved = reserved;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNumAvailable() {
    return numAvailable;
  }

  public void setNumAvailable(int numAvailable) {
    this.numAvailable = numAvailable;
  }
}
