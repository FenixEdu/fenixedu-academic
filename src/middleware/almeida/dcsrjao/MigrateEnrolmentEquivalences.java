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
		String x[] = {"","" };
		LoadCurricularCoursesEquivalencesFromFileToTable.main(x);
		CreateEnrolmentEquivalences.main(x);
//		LoadCurricularCoursesEquivalencesFromFileToTable.main(false);
//		CreateEnrolmentEquivalences.main(false);
	}
}