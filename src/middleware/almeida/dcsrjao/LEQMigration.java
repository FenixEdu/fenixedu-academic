package middleware.almeida.dcsrjao;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LEQMigration {

	private LEQMigration() {
	}

	public static void main(String[] args) {

		LoadAlmeidaCurricularCoursesCodesFromFileToTable.main(null);
		LoadAlmeidaCurricularCoursesFromFileToTable.main(null);
		LoadAlmeidaCurramFromFileToTable.main(null);
//		LoadAlmeidaLEQEnrolmentsFromFileToTable.main(null);

		LoadCurramToFenix.main(null);
		LoadCurricularCoursesToFenix.main(null);
//		LoadLEQEnrolmentsToFenix.main(null);
	}
}