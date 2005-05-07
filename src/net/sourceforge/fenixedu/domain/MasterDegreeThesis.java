/*
 * Created on 9/Out/2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesis extends MasterDegreeThesis_Base {

	// fields

	public MasterDegreeThesis() {

	}

	/**
	 * @param studentCurricularPlan
	 */
	public MasterDegreeThesis(IStudentCurricularPlan studentCurricularPlan) {
		super();
		setStudentCurricularPlan(studentCurricularPlan);
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": \n";
		result += "idInternal = " + getIdInternal() + "; \n";
		result += "studentCurricularPlan = "
				+ getStudentCurricularPlan().getIdInternal() + "; \n";
		result += "masterDegreeProofVersions = "
				+ getMasterDegreeProofVersions().toString() + "; \n";
		result += "masterDegreeThesisDataVersions = "
				+ getMasterDegreeThesisDataVersions().toString() + "; \n";
		result += "] \n";

		return result;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof IMasterDegreeThesis) {
			IMasterDegreeThesis masterDegreeThesis = (IMasterDegreeThesis) obj;
			result = getStudentCurricularPlan().equals(
					masterDegreeThesis.getStudentCurricularPlan());
		}

		return result;
	}

}