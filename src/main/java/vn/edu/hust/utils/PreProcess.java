package vn.edu.hust.utils;

import vn.edu.hust.config.SystemInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by quanpv on 08/12/2016.
 */
public class PreProcess {

    public static void createRandomDataTrainAndTest() {

        ArrayList<String> listCategories = FileUtils.getListDirectory(SystemInfo.PATH_RAW_DATA_TRAIN);

        for (String categoryPath : listCategories) {

            ArrayList<String> listFile = FileUtils.getListFile(categoryPath);

            long seed = System.nanoTime();
            Collections.shuffle(listFile, new Random(seed));

            ArrayList<String> tempDataTrain = new ArrayList<>();
            ArrayList<String> tempDataTest = new ArrayList<>();

            double threadsold = listFile.size() *0.2;

            for(int i =0; i < listFile.size(); i ++ ) {
                if(i < threadsold) {
                    try{
                        String text = new String(Files.readAllBytes(Paths.get(listFile.get(i))), StandardCharsets.UTF_8);
                        tempDataTest.add(text);

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } else {
                    try{
                        String text = new String(Files.readAllBytes(Paths.get(listFile.get(i))), StandardCharsets.UTF_8);
                        tempDataTrain.add(text);

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }

            String category = "";

            try{
                String[] splits = categoryPath.split("/");
                category=splits[splits.length-1];
            }catch (Exception e){
                e.printStackTrace();
            }

            FileUtils.writeToFile(tempDataTrain, SystemInfo.PATH_DATA_TRAIN + "" + category);
            FileUtils.writeToFile(tempDataTest, SystemInfo.PATH_DATA_TEST  + "" + category);

        }
    }
}
