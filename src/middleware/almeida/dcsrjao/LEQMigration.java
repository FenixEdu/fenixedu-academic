package middleware.almeida.dcsrjao;

import middleware.almeida.LoadCurram;


/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LEQMigration {


	private LEQMigration() {
	}

	public static void main(String[] args) {

		LoadAlmeidaLEQCurricularCourses loadAlmeidaLEQCurricularCourses = new LoadAlmeidaLEQCurricularCourses();
		loadAlmeidaLEQCurricularCourses.run();
		
		LoadCurram loadCurram = new LoadCurram();
		loadCurram.run();
		
		LoadLEQEnrolments loadLEQEnrolments = new LoadLEQEnrolments();
		loadLEQEnrolments.run();
	}

}