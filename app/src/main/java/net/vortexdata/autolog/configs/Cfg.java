package net.vortexdata.autolog.configs;

import net.vortexdata.autolog.BuildConfig;

public class Cfg {

    public static boolean easteregg = false;
    public static boolean dev = false;
    public static boolean fancyBackground = false;
    public static boolean fancyBGinQConn = false;
    public static boolean autoConnect = false;
    public static boolean openTab = false;

    public static String version = BuildConfig.VERSION_NAME;
    public static String logURL = "http://10.10.0.251:8002/index.php?zone=cp_htl";
    public static String newsFeed = "https://src.vortexdata.net/modules/api/autolog.php";
    //public static String logURL = "http://cms.vortexdata.net/services/BaseCMS.php"; // test url


}