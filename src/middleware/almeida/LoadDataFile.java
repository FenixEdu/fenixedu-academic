/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public abstract class LoadDataFile {

	private int numberLinesProcessed = 0;
	protected int numberElementsWritten = 0;
	private Calendar startTime = null;
	private Calendar endTime = null;

	protected PersistentObjectOJB persistentObjectOJB = null;

	public void load() {
		System.out.println("Loading " + getFilename());
		startTime = Calendar.getInstance();
		setupDAO();

		BufferedReader bufferedReader;

		try {
			bufferedReader =
				new BufferedReader(new FileReader(getFilename()));
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
	
	abstract protected String getFieldSeperator();

	protected void writeElement(Object persistentObject) {
		persistentObjectOJB.lockWrite(persistentObject);
		numberElementsWritten++;
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

	private void setupDAO() {
		persistentObjectOJB = new PersistentObjectOJB();
		persistentObjectOJB.beginTransaction();
	}

	private void shutdownDAO() {
		persistentObjectOJB.commitTransaction();
	}

	private void report() {
		long duration =
			(endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;

		System.out.println("----------------------------------------------------------------");
		System.out.println("   Report for loading of file: " + getFilename());
		System.out.println("      Number of lines parsed: " + numberLinesProcessed);
		System.out.println("      Number of elements added: " + numberElementsWritten);
		System.out.println("      Total processing time: " + duration + "s");
		System.out.println("----------------------------------------------------------------");
	}

}