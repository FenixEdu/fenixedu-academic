/*
 * Created on 30/Ago/2003
 *
 */
package DataBeans;

import java.util.List;

import DataBeans.ISiteComponent;

/**
 * @author Tânia Nunes
 *
 */
public class InfoSitePrograms extends DataTranferObject implements ISiteComponent{
	private List infoCurriculums;
	private List infoCurricularCourses;

	/**
	 * @return
	 */
	public List getInfoCurricularCourses() {
		return infoCurricularCourses;
	}

	/**
	 * @return
	 */
	public List getInfoCurriculums() {
		return infoCurriculums;
	}

	/**
	 * @param list
	 */
	public void setInfoCurricularCourses(List list) {
		infoCurricularCourses = list;
	}

	/**
	 * @param list
	 */
	public void setInfoCurriculums(List list) {
		infoCurriculums = list;
	}
	/**
	 * 
	 */
	public InfoSitePrograms() {
		super();
			
		}
}
