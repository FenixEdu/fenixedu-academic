/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public abstract class LoadDataFile {

	protected int numberLinesProcessed = 0;
	protected int numberElementsWritten = 0;
	protected int numberUntreatableElements = 0;
	protected Calendar startTime = null;
	protected Calendar endTime = null;

	protected PersistentObjectOJB persistentObjectOJB = null;

	public void load() {
		System.out.println("Loading " + getFilename());
		startTime = Calendar.getInstance();
		setupDAO();

		BufferedReader bufferedReader;

		try {
			FileInputStream fin = new FileInputStream(getFilename());
			InputStreamReader inputStreamReader = new InputStreamReader(fin, "8859_1");
			bufferedReader = new BufferedReader(inputStreamReader);
			String line = bufferedReader.readLine();
			while (line != null) {
				processLine(line);
				line = bufferedReader.readLine();

				numberLinesProcessed++;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		shutdownDAO();
		endTime = Calendar.getInstance();
		System.out.println("Done.");

		report();
	}

	/**
	 * @param Line from text file to be processed
	 */
	abstract protected void processLine(String line);

	abstract protected String getFilename();

	abstract protected String getFilenameOutput();

	abstract protected String getFieldSeparator();

	protected void writeElement(Object persistentObject) {
		persistentObjectOJB.lockWrite(persistentObject);
		numberElementsWritten++;
	}

	protected List query(Class classToQuery, Criteria criteria) {
		return persistentObjectOJB.query(classToQuery, criteria);
	}

	protected List query(Object object) {
		return persistentObjectOJB.query(object);
	}

	protected Date convertToJavaDate(String dateString) {
		try {
			String formattedString = dateString.substring(3, 5);
			formattedString += "/" + dateString.substring(0, 2);
			formattedString += "/" + dateString.substring(6);
			return DateFormat.getDateInstance(DateFormat.SHORT).parse(
				formattedString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to parse date: " + dateString);
		}
	}

	protected void setupDAO() {
		persistentObjectOJB = new PersistentObjectOJB ();
		persistentObjectOJB.beginTransaction();
	}

	protected void shutdownDAO() {
		persistentObjectOJB.commitTransaction();
	}

	protected void writeToFile(String bufferToWrite) {
		try {
			FileWriter outFile = new FileWriter(getFilenameOutput());
			outFile.write(bufferToWrite);
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(
				"Failed to obtain a file writer for " + getFilenameOutput());
		}
	}

	protected void report() {
		long duration =
			(endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		long durationHour = duration / 3600;
		long durationMin = (duration % 3600) / 60;
		long durationSec = (duration % 3600) % 60;

		System.out.println(
			"----------------------------------------------------------------");
		System.out.println("   Report for loading of file: " + getFilename());
		System.out.println(
			"      Number of lines parsed: " + numberLinesProcessed);
		System.out.println(
			"      Number of elements added: " + numberElementsWritten);
		System.out.println(
			"      Number of untreatable elements: "
				+ numberUntreatableElements);
		System.out.println(
			"      Total processing time: "
				+ durationHour
				+ "h "
				+ durationMin
				+ "m "
				+ durationSec
				+ "s");
		System.out.println(
			"----------------------------------------------------------------");
	}

}
