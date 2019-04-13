package net.vortexdata.autolog.configs;

public class Cfg {

    public static boolean easteregg = false;
    public static boolean dev = false;
    public static boolean expired = false;
    public static boolean fancyBackground = false;
    public static boolean sentUsage = false;
    public static boolean allowGoBackOnTimeout = false;

    public static boolean qConn = false;

    public static int expireDay = 30;
    public static int expireMonth = 2; // from 0 to 11
    public static int expireYear = 2019;

    public static String version = "0.1.1 - BETA";

    public static String err = "Oops! Some connection error occured. Wrong Wifi? Or just left mobile data on?";
    public static String err_color = "#eb3b5a";

    public static String downloadURL = "https://projects.vortexdata.net/autologin/";
    public static String checkWeb = "https://projects.vortexdata.net/autologin/v0.1.1.txt";
    public static String checkForUpdates = "https://projects.vortexdata.net/autologin/changes.txt";
    public static String sendUsage = "https://projects.vortexdata.net/autologin/stats/usage.php?v=latest";
    public static String logURL = "http://10.10.0.251:8002/index.php?zone=cp_htl";
    //public static String logURL = "https://projects.vortexdata.net/autologin/test/index.php";

}