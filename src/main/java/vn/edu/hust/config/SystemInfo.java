package vn.edu.hust.config;

/**
 * Created by quanpv on 08/12/2016.
 */
public class SystemInfo {

    static {
        ConfigUtils.loadConfig();
    }
    public static String PATH_RAW_DATA_TRAIN;
    public static String PATH_DATA_TRAIN;
    public static String PATH_DATA_TEST;
    public static String PATH_TRAINED_MODEL;
}

