package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class RestrictionByNumberOfDoneCurricularCourses
	extends RestrictionByNumberOfCurricularCourses
	implements IRestrictionByNumberOfCurricularCourses
{
	public RestrictionByNumberOfDoneCurricularCourses()
	{
		super();
	}

	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext)
	{
		List doneList = studentEnrolmentContext.getStudentApprovedEnrollments();
		return
			(((doneList == null || doneList.isEmpty()) && (this.numberOfCurricularCourses.intValue() == 0)) ||
			(doneList.size() >= numberOfCurricularCourses.intValue()));
	}
}