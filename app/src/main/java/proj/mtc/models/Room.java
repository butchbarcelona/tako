package proj.mtc.models;

import java.util.ArrayList;

public class Room {

  int id;
  String name;
  String desc;
  String serial;

  public Room(int id, String name, String desc, String serial){
    this.id = id;
    this.desc  = desc;
    this.serial = serial;
    this.name = name;
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

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getSerial() {
    return serial;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }


  public static Room lookForRoom(String name, ArrayList<Room> rooms){
    Room foundRoom = null;
    for(Room room: rooms){
      if(room.getName().equals(name)){
        foundRoom = room;
      }
    }

    return foundRoom;
  }
}
