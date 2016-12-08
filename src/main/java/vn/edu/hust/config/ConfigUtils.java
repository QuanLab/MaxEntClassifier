package vn.edu.hust.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by quanpv on 08/12/2016.
 */
public class ConfigUtils {
    public static void loadConfig(){
        try {
            Properties properties = new Properties();
            File f = new File("conf.properties");
            FileInputStream fileInputStream = new FileInputStream(f);
            properties.load(fileInputStream);

            SystemInfo.PATH_RAW_DATA_TRAIN = properties.getProperty("PATH_RAW_DATA_TRAIN");
            SystemInfo.PATH_TRAINED_MODEL = properties.getProperty("PATH_TRAINED_MODEL");
            SystemInfo.PATH_DATA_TRAIN = properties.getProperty("PATH_DATA_TRAIN");
            SystemInfo.PATH_DATA_TEST = properties.getProperty("PATH_DATA_TEST");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
