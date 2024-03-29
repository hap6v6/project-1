package com.example.studyproject;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MakeRoomDB {
    public String roomname;
    public String roomcategory;
    public String roominfo;
    public String roomauth;
    public String roomperson;
    public boolean roomday;
    public int[] roomwhen;
    public boolean roomtime;
    public boolean roomlock;
    public int roomhow;
    public String roomtime1;
    public String roomtime2;

    public Map<String, Boolean> stars = new HashMap<>();

    public MakeRoomDB(){

    }

    public MakeRoomDB(String roomname, String roomcategory, String roominfo, String roomauth,
                      String roomperson, boolean roomday, int[] roomWhen, boolean roomtime,
                      boolean roomlock, int roomHow, String time1, String time2){
        this.roomname = roomname; // 방 이름
        this.roomcategory = roomcategory; // 카테고리 (습관/공부/취미/운동/기타)
        this.roominfo = roominfo; // 소개글
        this.roomauth = roomauth; // 인증 횟수

        this.roomperson = roomperson; // 최대 인원
        this.roomday = roomday; // 인증 요일 사용 여부
        this.roomwhen = roomWhen; // 인증 요일
        this.roomtime = roomtime; // 인증 시간 사용 여부

        this.roomlock = roomlock; // 비공개 여부
        this.roomhow = roomHow; // 인증 방식 선택 (횟수 / 시간)
        this.roomtime1 = time1; // 인증 시작 시간
        this.roomtime2 = time2; // 인증 종료 시간
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getRoomcategory() {
        return roomcategory;
    }

    public void setRoomcategory(String roomcategory) {
        this.roomcategory = roomcategory;
    }

    public String getRoominfo() {
        return roominfo;
    }

    public void setRoominfo(String roominfo) {
        this.roominfo = roominfo;
    }

    public String getRoomauth() { return roomauth;
    }

    public void setRoomauth(String roomauth) { this.roomauth = roomauth;
    }

    public String getRoomperson() {return roomperson;}

    public void setRoomperson(String roomperson) {this.roomperson = roomperson;}

    public boolean getRoomday() {return roomday;}

    public void setRoomday() {this.roomday = roomday;}

    public boolean getRoomtime() {return roomtime;}

    public void setRoomtime() {this.roomtime = roomtime;}

    public boolean getRoomlock() {return roomlock;}

    public void setRoomlock() {this.roomlock = roomlock;}
/*
    @Override
    public String toString() {
        return "MakeRoomDB{" +
                "roomname='" + roomname + '\'' +
                ", roomcategory='" + roomcategory + '\'' +
                ", roominfo='" + roominfo + '\'' +
                ", roomauth='" + roomauth + '\'' +
                '}';
    }
*/

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomname", roomname);
        result.put("roomcategory", roomcategory);
        result.put("roominfo", roominfo);
        result.put("roomauth", roomauth);
        result.put("roomperson", roomperson);
        result.put("roomtime", roomtime);
        result.put("roomday", roomday);
        result.put("roomlock", roomlock);
        return result;
    }
}
