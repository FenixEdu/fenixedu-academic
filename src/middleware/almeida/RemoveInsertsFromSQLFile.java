/*
 * Created on Apr 16, 2003
 *
 */
package middleware.almeida;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class RemoveInsertsFromSQLFile {

	private static final String FILENAME_SQL_INPUT = "etc/migration/graduacao.sql";
	private static final String FILENAME_SQL_OUTPUT = "etc/migration/tables_graduacao.sql";

	private static int numberLinesProcessed = 0;
	private static int numberLinesRemoved = 0;

	public RemoveInsertsFromSQLFile() {
		super();
	}

	public static void main(String[] args) {

		System.out.println("Removeing inserts...");

		BufferedReader bufferedReader;
		String bufferToWrite = new String();

		try {
			bufferedReader = new BufferedReader(new FileReader(FILENAME_SQL_INPUT));
			String line = bufferedReader.readLine();
			while (line != null) {
				if (keepLine(line)) {
					bufferToWrite += line + "\n";
				} else {
					numberLinesRemoved++;
				}
				line = bufferedReader.readLine();
				numberLinesProcessed++;
			}
		} catch (Exception e) {
			System.out.println("Error processing file: " + e);
		}

		writeToFile(bufferToWrite);

		System.out.println("End");
		report();
	}

	private static boolean keepLine(String line) {
		return !line.startsWith("INSERT INTO");
	}

	private static void writeToFile(String bufferToWrite) {
		try {
			FileWriter outFile = new FileWriter(FILENAME_SQL_OUTPUT);
			outFile.write(bufferToWrite);
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(
				"Failed to obtain a file writer for " + FILENAME_SQL_OUTPUT);
		}
	}

	/**
	 * 
	 */
	private static void report() {
		System.out.println("----------------------------------------------------------------");
		System.out.println("   Report for INSERT INTO REMOVAL");
		System.out.println("      Input filename: " + FILENAME_SQL_INPUT);
		System.out.println("      Output filename: " + FILENAME_SQL_OUTPUT);
		System.out.println("      Number of lines parsed: " + numberLinesProcessed);
		System.out.println("      Number of lines removed: " + numberLinesRemoved);
		System.out.println("----------------------------------------------------------------");		
	}

}
