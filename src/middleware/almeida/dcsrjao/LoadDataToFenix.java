package middleware.almeida.dcsrjao;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import middleware.almeida.PersistentObjectOJBReader;

import org.apache.ojb.broker.query.Criteria;

public abstract class LoadDataToFenix {

	protected static final String OLD_DEGREE_CURRICULAR_PLAN_NAME = "LEQ";

	protected int numberElementsWritten = 0;
	protected int numberUntreatableElements = 0;
	protected Calendar startTime = null;
	protected Calendar endTime = null;

	protected PersistentObjectOJBReader persistentObjectOJB = null;

	abstract protected String getFilenameOutput();

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
			return DateFormat.getDateInstance(DateFormat.SHORT).parse(formattedString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to parse date: " + dateString);
		}
	}

	protected void setupDAO() {
		persistentObjectOJB = new PersistentObjectOJBReader();
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
			System.out.println("Failed to obtain a file writer for " + getFilenameOutput());
		}
	}

	protected void report() {
		long duration = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		long durationHour = duration / 3600;
		long durationMin = (duration % 3600) / 60;
		long durationSec = (duration % 3600) % 60;

		System.out.println("----------------------------------------------------------------");
		System.out.println("      Number of elements added: " + numberElementsWritten);
		System.out.println("      Number of untreatable elements: " + numberUntreatableElements);
		System.out.println("      Total processing time: " + durationHour + "h " + durationMin + "m " + durationSec + "s");
		System.out.println("----------------------------------------------------------------");
	}

	protected void migrationStart(String migration) {
		setupDAO();
		System.out.println("Migrating " + migration + ".\n");
		startTime = Calendar.getInstance();
	}

	protected void migrationEnd(String migration, String log) {
		shutdownDAO();
		endTime = Calendar.getInstance();
		report();
		System.out.println("Done " + migration + ".\n\n\n");
		writeToFile(log);
	}
}