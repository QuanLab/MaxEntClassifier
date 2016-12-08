package vn.edu.hust.maxent;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.maxent.io.GISModelReader;
import opennlp.maxent.io.SuffixSensitiveGISModelWriter;
import opennlp.model.AbstractModelWriter;
import opennlp.model.DataReader;
import opennlp.model.MaxentModel;
import opennlp.model.PlainTextFileDataReader;
import vn.edu.hust.config.SystemInfo;
import vn.edu.hust.utils.FileUtils;
import vn.edu.hust.utils.PreProcess;

import static vn.edu.hust.maxent.PlainTextEventStream.buildContext;


/**
 * Created by quanpv on 08/12/2016.
 */

public class MaxEnt {

    private static final String TRAING_FOLDER =SystemInfo.PATH_DATA_TRAIN;
    private static final String TEST_FOLDER = SystemInfo.PATH_DATA_TEST;
    private static final String MODEL_FILE_NAME =SystemInfo.PATH_TRAINED_MODEL;


    public static void main(String[] args) throws Exception{

//        trainModel();

        FileInputStream inputStream = new FileInputStream(MODEL_FILE_NAME);
        InputStream decodedInputStream = new GZIPInputStream(inputStream);
        DataReader modelReader = new PlainTextFileDataReader(decodedInputStream);
        MaxentModel loadedMaxentModel = new GISModelReader(modelReader).getModel();

        ArrayList<String> allDataTest = FileUtils.getListFile(TEST_FOLDER);

        for (String dataTestFile: allDataTest) {
            ArrayList<String> dataTest = FileUtils.readFile(dataTestFile);

            int cnt =0;
            int total=0;
            String category = FileUtils.getFileName(dataTestFile);

            for(String test: dataTest) {
                total ++;
                if(classify(loadedMaxentModel, test).equals(category)){
                    cnt++;
                };
            }
            System.out.println(category +" :\t" + cnt/(float)total);
        }
    }


    public static void trainModel()  throws Exception {

        PreProcess.createRandomDataTrainAndTest();

        ArrayList<String> dataTrain = FileUtils.getListFile(TRAING_FOLDER);
        ArrayList<LearningSample> samples = new ArrayList<>();

        for (String categoryPath : dataTrain) {

            System.out.println(categoryPath);

            String preDefinedCategories="";
            try {
                String splits[] = categoryPath.split("/");
                preDefinedCategories = splits[splits.length-1];
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayList<String> allTextExample = FileUtils.readFile(categoryPath);

            for (String example : allTextExample){
                LearningSample learningSample = new LearningSample(preDefinedCategories, example);
                samples.add(learningSample);
            }
        }

        PlainTextEventStream stream = new PlainTextEventStream(samples);
        GISModel model = GIS.trainModel(stream, 10000, 1, false, true);

        // Storing the trained model into a file for later use (gzipped)
        File outFile = new File(MODEL_FILE_NAME);
        AbstractModelWriter writer = new SuffixSensitiveGISModelWriter(model, outFile);
        writer.persist();
        System.out.println("Training model complete........");

    }

    private static String classify(MaxentModel loadedMaxentModel, String textContent) throws  Exception{
        double[] result = loadedMaxentModel.eval(buildContext(textContent));
        return loadedMaxentModel.getBestOutcome(result);
    }
}