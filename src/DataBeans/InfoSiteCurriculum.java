/*
 * Created on 29/Jul/2003
 *
 * 
 */
package DataBeans;

/**
 * @author João Mota
 *
 * 29/Jul/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSiteCurriculum extends DataTranferObject implements ISiteComponent {
	
	private InfoCurriculum infoCurriculum;
	private InfoCurricularCourse infoCurricularCourse;

	/**
	 * @return
	 */
	public InfoCurricularCourse getInfoCurricularCourse() {
		return infoCurricularCourse;
	}

	/**
	 * @param infoCurricularCourse
	 */
	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
		this.infoCurricularCourse = infoCurricularCourse;
	}

	/**
	 * @return
	 */
	public InfoCurriculum getInfoCurriculum() {
		return infoCurriculum;
	}

	/**
	 * @param infoCurriculum
	 */
	public void setInfoCurriculum(InfoCurriculum infoCurriculum) {
		this.infoCurriculum = infoCurriculum;
	}

	/**
	 * 
	 */
	public InfoSiteCurriculum() {
		super();
		// TODO Auto-generated constructor stub
	}

}
