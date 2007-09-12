package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.CurricularCourseRequestsBean.CurricularCourseRequestType;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseEquivalenceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseTradeRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

public class CreateCurricularCourseRequest extends Service {

    public void run(CurricularCourseRequestType type, Registration registration, CurricularCourse requestCurricularCourse, CurricularCourse proposedCurricularCourse) {
	switch(type) {
	case CURRICULAR_COURSE_EQUIVALENCE:
	    new CourseEquivalenceRequest(registration, Boolean.FALSE, Boolean.FALSE,requestCurricularCourse,proposedCurricularCourse);
	    break;
	case CURRICULAR_COURSE_TRADE:
	    new CourseTradeRequest(registration,Boolean.FALSE,Boolean.FALSE,requestCurricularCourse,proposedCurricularCourse);
	    break;
	}
    }
}
