/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public class RestrictionCurricularCourseDone
	extends Restriction
	implements IRestrictionCurricularCourseDone {
	private Integer keyPrecedentCurricularCourse;
	private ICurricularCourse precedentCurricularCourse;

	/**
	 * 
	 */
	public RestrictionCurricularCourseDone() {
		super();
	}

	/**
	 * @return
	 */
	public ICurricularCourse getPrecedentCurricularCourse() {
		return precedentCurricularCourse;
	}

	/**
	 * @param course
	 */
	public void setPrecedentCurricularCourse(ICurricularCourse course) {
		precedentCurricularCourse = course;
	}

	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
//		return enrolmentContext.getEnrolmentsAprovedByStudent().contains(this.getPrecedentCurricularCourse());
		return enrolmentContext.isCurricularCourseDone(this.getPrecedentCurricularCourse());
	}
	/**
	 * @return
	 */
	public Integer getKeyPrecedentCurricularCourse() {
		return keyPrecedentCurricularCourse;
	}

	/**
	 * @param integer
	 */
	public void setKeyPrecedentCurricularCourse(Integer keyPrecedentCurricularCourse) {
		this.keyPrecedentCurricularCourse = keyPrecedentCurricularCourse;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean result = super.equals(obj);
		if ((result) && (obj instanceof IRestrictionCurricularCourseDone)) {
			IRestrictionCurricularCourseDone curricularCourseDoneRestriction =
				(IRestrictionCurricularCourseDone) obj;
			result =
				this.getPrecedentCurricularCourse().equals(
					curricularCourseDoneRestriction
						.getPrecedentCurricularCourse());
		}
		return result;
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Restriction(").append(this.getClass()).append(
			"):").append(
			"\n\t");
		stringBuffer.append(this.getPrecedentCurricularCourse()).append("\n");
		return stringBuffer.toString();
	}

}
