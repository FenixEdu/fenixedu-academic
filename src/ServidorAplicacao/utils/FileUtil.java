/*
 * Created on Nov 20, 2003
 *  
 */
package ServidorAplicacao.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Luis Cruz
 *  
 */
public abstract class FileUtil {

    protected static String inputFile;

    protected static String outputFile;

    protected static BufferedReader bufferedReader;

    public void processFile() {
        String outputBuffer = generateHeader();

        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            String line = new String();
            int i = 0;
            while (line != null) {
                line = bufferedReader.readLine();
                outputBuffer += processLine(new Integer(++i), line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputBuffer += generateFooter();

        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(outputBuffer);
            fileWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @param line
     * @return
     */
    abstract protected String processLine(Integer lineNumber, String line);

    /**
     * @param
     * @return
     */
    abstract protected String generateHeader();

    /**
     * @param
     * @return
     */
    abstract protected String generateFooter();

}