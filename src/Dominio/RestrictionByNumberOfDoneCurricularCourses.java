package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public class RestrictionByNumberOfDoneCurricularCourses extends RestrictionByNumberOfCurricularCourses implements IRestrictionByNumberOfCurricularCourses {

	public RestrictionByNumberOfDoneCurricularCourses() {
		super();
	}

	public boolean evaluate(EnrolmentContext enrolmentContext) {
		List doneList = enrolmentContext.getEnrolmentsAprovedByStudent();
		return (((doneList == null) && (this.numberOfCurricularCourses.intValue()== 0)) ||(doneList.size() >= numberOfCurricularCourses.intValue()));
	}
}