/*
 * Created on Apr 16, 2003
 *
 */
package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamExecutionCourse;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class ExamsDataTranslation {

	static SuportePersistenteOJB persistentSupport = null;
	static IPersistentExam persistentExam = null;
	static IPersistentExamExecutionCourse persistentExamExecutionCourse = null;

	public ExamsDataTranslation() {
		super();
	}

	public static void main(String[] args) {
		setupDAO();

		System.out.println("Start");

		BufferedReader bufferedReader;

		try {
			bufferedReader = new BufferedReader(new FileReader("etc/exams.txt"));
			String line = bufferedReader.readLine();
			while (line != null) {
				processExamFromTextFile(line);
				line = bufferedReader.readLine();
			}
		} catch (Exception e) {
			System.out.println("Error processing file: " + e);
		}

		System.out.println("End");
	}

	private static void processExamFromTextFile(String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, "\t");

		// Process Line
		String executionCourseCode = stringTokenizer.nextToken();
		String executionCourseName = stringTokenizer.nextToken();
		String curricularYear = stringTokenizer.nextToken();
		String executionDegree = stringTokenizer.nextToken();
		stringTokenizer.nextToken(); //Trash
		String date = stringTokenizer.nextToken();
		String hour = stringTokenizer.nextToken();

		System.out.println(
			"Line: "
				+ executionCourseCode
				+ executionCourseName
				+ curricularYear
				+ executionDegree
				+ date
				+ hour);

		// Do something with the info... Maybe save it in the DB.
	}

	private static void setupDAO() {
		// Establish DAO support
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			System.out.println(
				"Failed to obtain instance of persistente support");
		}

		persistentExam = persistentSupport.getIPersistentExam();
		persistentExamExecutionCourse =
			persistentSupport.getIPersistentExamExecutionCourse();
	}

}
