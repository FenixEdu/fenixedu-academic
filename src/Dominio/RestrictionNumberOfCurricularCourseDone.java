/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */
public class RestrictionNumberOfCurricularCourseDone extends Restriction implements IRestrictionNumberOfCurricularCourseDone {
	private Integer numberOfCurricularCourseDone;

	/**
	 * 
	 */
	public RestrictionNumberOfCurricularCourseDone() {
		super();
	}

	/**
	 * @return
	 */
	public Integer getNumberOfCurricularCourseDone() {
		return numberOfCurricularCourseDone;
	}

	/**
	 * @param integer
	 */
	public void setNumberOfCurricularCourseDone(Integer numberOfCurricularCourseDone) {
		this.numberOfCurricularCourseDone = numberOfCurricularCourseDone;
	}

	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
		List doneList = enrolmentContext.getEnrolmentsAprovedByStudent();
		return (((doneList == null) && (this.numberOfCurricularCourseDone.intValue()== 0)) ||(doneList.size() >= numberOfCurricularCourseDone.intValue()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean result = super.equals(obj); 
		if ((result) && (obj instanceof IRestrictionNumberOfCurricularCourseDone)){
			IRestrictionNumberOfCurricularCourseDone numberOfCurricularCourseDoneRestriction = (IRestrictionNumberOfCurricularCourseDone) obj;
			result = numberOfCurricularCourseDoneRestriction.getNumberOfCurricularCourseDone().equals(this.getNumberOfCurricularCourseDone());
		}
		return result;
	}

}
