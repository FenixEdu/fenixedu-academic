package middleware.middlewareDomain;

import Dominio.DomainObject;
import Dominio.ICurricularCourse;

/**
 * @author David Santos
 * 20/Out/2003
 */

public class MWCurricularCourseOutsideStudentDegree extends DomainObject {

	private String courseCode;
	private Integer degreeCode;
	private Integer keyCurricularCourse;
	private ICurricularCourse curricularCourse;

	public MWCurricularCourseOutsideStudentDegree() {
		super();
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

	public Integer getDegreeCode() {
		return degreeCode;
	}

	public void setDegreeCode(Integer degreeCode) {
		this.degreeCode = degreeCode;
	}

	public Integer getKeyCurricularCourse() {
		return keyCurricularCourse;
	}

	public void setKeyCurricularCourse(Integer keyCurricularCourse) {
		this.keyCurricularCourse = keyCurricularCourse;
	}

	public String toString(){
	  return  " [courseCode] " + courseCode + " [degreeCode] " + degreeCode + " [keyCurricularCourse] " + keyCurricularCourse;

	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MWCurricularCourseOutsideStudentDegree) {
			MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegree = (MWCurricularCourseOutsideStudentDegree) obj;
			result = mwCurricularCourseOutsideStudentDegree.getCourseCode().equals(this.courseCode) &&
					 mwCurricularCourseOutsideStudentDegree.getDegreeCode().equals(this.degreeCode) &&
					 mwCurricularCourseOutsideStudentDegree.getKeyCurricularCourse().equals(this.keyCurricularCourse);
		}
		return result;
	}
}