package Dominio;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class CurricularCourseEquivalence extends DomainObject implements ICurricularCourseEquivalence{

	private ICurricularCourse curricularCourse;
	private ICurricularCourse equivalentCurricularCourse;

	private Integer curricularCourseKey;
	private Integer equivalentCurricularCourseKey;

	public CurricularCourseEquivalence() {
	}


	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof ICurricularCourseEquivalence) {
			ICurricularCourseEquivalence equivalence = (ICurricularCourseEquivalence) obj;

			resultado = (this.getCurricularCourse().equals(equivalence.getCurricularCourse())) &&
									(this.getEquivalentCurricularCourse().equals(equivalence.getEquivalentCurricularCourse()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "curricularCourse = " + this.curricularCourse + "; ";
		result += "equivalentCurricularCourse = " + this.equivalentCurricularCourse + "; ";
		return result;
	}
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	public Integer getCurricularCourseKey() {
		return curricularCourseKey;
	}

	public ICurricularCourse getEquivalentCurricularCourse() {
		return equivalentCurricularCourse;
	}

	public Integer getEquivalentCurricularCourseKey() {
		return equivalentCurricularCourseKey;
	}

	public void setCurricularCourse(ICurricularCourse course) {
		curricularCourse = course;
	}

	public void setCurricularCourseKey(Integer integer) {
		curricularCourseKey = integer;
	}

	public void setEquivalentCurricularCourse(ICurricularCourse course) {
		equivalentCurricularCourse = course;
	}

	public void setEquivalentCurricularCourseKey(Integer integer) {
		equivalentCurricularCourseKey = integer;
	}

}
