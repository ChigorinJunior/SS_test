package ru.leberamai.sstest.friends;

public class Friend {

    String date;
    String message;
    String name;
    String avatar;
    String id;
    

    public Friend() {}

  public Friend(String name,String message,String date,String avatar, String id){
      this.name = name;
      this.message = message;
      this.date = date;
      this.avatar = avatar;
      this.id = id;
  }

}