package net.sourceforge.fenixedu.tools;

import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * Created on Oct 25, 2003
 *
 */

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class DataSetGeneratorMain {

    /**
     *  
     */
    public DataSetGeneratorMain() {
        super();
    }

    public static void main(String[] args) {
        DataSetGenerator dataSetGen = null;

        try {
            if (args.length > 0) {
                String filePath = args[0];
                filePath = filePath.replaceAll("\\\\", "/");
                dataSetGen = new DataSetGenerator(filePath);
            } else
                dataSetGen = new DataSetGenerator();

            dataSetGen.writeDataSet();
        } catch (NullPointerException e) {
            System.out.println("Configuration file not found");
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}