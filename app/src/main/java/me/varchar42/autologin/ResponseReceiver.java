package me.varchar42.autologin;

public interface ResponseReceiver {

    void error(String error);
    void ok(String message);

}
