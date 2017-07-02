package com.vlad17021995m.android.usersapplication.data.orm.entity;

public class Lock implements entity {
    private int id;
    private String time_start;
    private String time_end;
    private String reason;
    private int user_id;

    public Lock() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getUser() {
        return user_id;
    }

    public void setUser(int user) {
        this.user_id = user;
    }
}
