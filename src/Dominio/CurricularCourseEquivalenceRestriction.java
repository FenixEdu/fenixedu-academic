/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
public class CurricularCourseEquivalenceRestriction extends DomainObject implements ICurricularCourseEquivalenceRestriction {
	private ICurricularCourseEquivalence curricularCourseEquivalence;
	private ICurricularCourse equivalentCurricularCourse;
	private String yearOfEquivalence;

	private Integer curricularCourseEquivalenceKey;
	private Integer equivalentCurricularCourseKey;

	/**
	 * 
	 */
	public CurricularCourseEquivalenceRestriction() {
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if ((result) && (obj instanceof ICurricularCourseEquivalenceRestriction)) {
			ICurricularCourseEquivalenceRestriction curricularCourseEquivalenceRestricition = (ICurricularCourseEquivalenceRestriction) obj;
			result = curricularCourseEquivalenceRestricition.getCurricularCourseEquivalence().equals(this.getCurricularCourseEquivalence()) && curricularCourseEquivalenceRestricition.getEquivalentCurricularCourse().equals(this.getEquivalentCurricularCourse());
		}
		return result;
	}

	public ICurricularCourseEquivalence getCurricularCourseEquivalence() {
		return curricularCourseEquivalence;
	}

	public Integer getCurricularCourseEquivalenceKey() {
		return curricularCourseEquivalenceKey;
	}

	public ICurricularCourse getEquivalentCurricularCourse() {
		return equivalentCurricularCourse;
	}

	public Integer getEquivalentCurricularCourseKey() {
		return equivalentCurricularCourseKey;
	}

	public void setCurricularCourseEquivalence(ICurricularCourseEquivalence equivalence) {
		curricularCourseEquivalence = equivalence;
	}

	public void setCurricularCourseEquivalenceKey(Integer integer) {
		curricularCourseEquivalenceKey = integer;
	}

	public void setEquivalentCurricularCourse(ICurricularCourse course) {
		equivalentCurricularCourse = course;
	}

	public void setEquivalentCurricularCourseKey(Integer integer) {
		equivalentCurricularCourseKey = integer;
	}

	public String getYearOfEquivalence() {
		return yearOfEquivalence;
	}

	public void setYearOfEquivalence(String string) {
		yearOfEquivalence = string;
	}

}
