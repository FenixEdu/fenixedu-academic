package DataBeans.degreeAdministrativeOffice;

import java.util.List;

public class InfoEquivalenceContext {
	private List infoCurricularCourseScopesToGiveEquivalence;
	private List infoCurricularCourseScopesToGetEquivalence;
	
	public InfoEquivalenceContext() {
	}

	/**
	 * @return
	 */
	public List getInfoCurricularCourseScopesToGetEquivalence() {
		return infoCurricularCourseScopesToGetEquivalence;
	}

	/**
	 * @return
	 */
	public List getInfoCurricularCourseScopesToGiveEquivalence() {
		return infoCurricularCourseScopesToGiveEquivalence;
	}

	/**
	 * @param list
	 */
	public void setInfoCurricularCourseScopesToGetEquivalence(List list) {
		infoCurricularCourseScopesToGetEquivalence = list;
	}

	/**
	 * @param list
	 */
	public void setInfoCurricularCourseScopesToGiveEquivalence(List list) {
		infoCurricularCourseScopesToGiveEquivalence = list;
	}

}