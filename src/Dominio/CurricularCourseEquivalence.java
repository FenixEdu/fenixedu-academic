package Dominio;

import java.util.List;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class CurricularCourseEquivalence extends DomainObject implements ICurricularCourseEquivalence{

	private ICurricularCourse curricularCourse;
	private List equivalenceRestrictions;

	private Integer curricularCourseKey;

	public CurricularCourseEquivalence() {
	}


	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof ICurricularCourseEquivalence) {
			ICurricularCourseEquivalence equivalence = (ICurricularCourseEquivalence) obj;

			resultado = (this.getCurricularCourse().equals(equivalence.getCurricularCourse()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "curricularCourse = " + this.curricularCourse + "; ";
		return result;
	}
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	public Integer getCurricularCourseKey() {
		return curricularCourseKey;
	}


	public void setCurricularCourse(ICurricularCourse course) {
		curricularCourse = course;
	}

	public void setCurricularCourseKey(Integer integer) {
		curricularCourseKey = integer;
	}
	public List getEquivalenceRestrictions() {
		return equivalenceRestrictions;
	}

	public void setEquivalenceRestrictions(List list) {
		equivalenceRestrictions = list;
	}

}
