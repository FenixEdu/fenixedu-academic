/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author jpvl
 */
public class NumberOfCurricularCourseDoneRestriction extends Restriction implements INumberOfCurricularCourseDoneRestriction {
	private Integer numberOfCurricularCourseDone;

	/**
	 * 
	 */
	public NumberOfCurricularCourseDoneRestriction() {
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
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
		List doneList = enrolmentContext.getCurricularCoursesDoneByStudent();
		return (((doneList == null) && (this.numberOfCurricularCourseDone.intValue()== 0)) ||(doneList.size() == numberOfCurricularCourseDone.intValue()));
		
	}

}
