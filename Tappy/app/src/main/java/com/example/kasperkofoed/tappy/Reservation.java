package com.example.kasperkofoed.tappy;

import java.io.Serializable;

public class Reservation implements Serializable {
    private String id;
    private int roomId;
    private String userId;
    private String fromTimeString, toTimeString;
    private String purpose;

    public String getId() {
        return id;
    }


    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFromTimeString() {
        return fromTimeString;
    }

    public void setFromTimeString(String fromTimeToString) {
        this.fromTimeString = fromTimeToString;
    }

    public String getToTimeString() {
        return toTimeString;
    }

    public void setToTimeString(String toTimeString) {
        this.toTimeString = toTimeString;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    @Override
    public String toString() {
        return
                "Room: " + roomId +
                " || From: " + fromTimeString  +
                " To: " + toTimeString;
    }
}