package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-06.
 */

public class Friend {
    private String name, message, phoneNumber;
    private int imageId;


    public Friend() {
    }

    public Friend(String name, String message, String phoneNumber, int imageId) {
        this.name = name;
        this.message = message;
        this.phoneNumber = phoneNumber;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
