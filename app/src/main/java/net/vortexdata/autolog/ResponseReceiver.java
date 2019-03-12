package net.vortexdata.autolog;

public interface ResponseReceiver {

    void error(String error);
    void ok(String message);

}
