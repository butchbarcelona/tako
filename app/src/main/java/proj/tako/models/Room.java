package proj.tako.models;

import java.util.ArrayList;

/**
 * Created by mbarcelona on 2/21/16.
 */
public class Room {

  int id;
  String name;
  boolean reserved;
  ArrayList<Reservation> reservations;

  public Room(int id, String name, boolean reserved, ArrayList<Reservation> reservations){
    this.id = id;
    this.name = name;
    this.reserved = reserved;
    this.reservations = reservations;
  }



}
