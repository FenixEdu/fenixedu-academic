/*
 * Created on 4/Ago/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *
 * 4/Ago/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSiteEvaluationMethods implements ISiteComponent {
	private List infoEvaluations;
	private List infoCurricularCourses;

	public int getSize() {
		return infoEvaluations.size();
	}

	/**
	 * @return
	 */
	public List getInfoCurricularCourses() {
		return infoCurricularCourses;
	}

	/**
	 * @param infoCurricularCourses
	 */
	public void setInfoCurricularCourses(List infoCurricularCourses) {
		this.infoCurricularCourses = infoCurricularCourses;
	}

	/**
	 * @return
	 */
	public List getInfoEvaluations() {
		return infoEvaluations;
	}

	/**
	 * @param infoEvaluations
	 */
	public void setInfoEvaluations(List infoEvaluations) {
		this.infoEvaluations = infoEvaluations;
	}


}
