package middleware.almeida.dcsrjao;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class MigrateEnrolmentEquivalences {

	private MigrateEnrolmentEquivalences() {
	}

	public static void main(String[] args) {
		LoadCurricularCoursesEquivalencesFromFileToTable.main(true);
		CreateEnrolmentEquivalences.main(true);
		LoadCurricularCoursesEquivalencesFromFileToTable.main(false);
		CreateEnrolmentEquivalences.main(false);
	}
}