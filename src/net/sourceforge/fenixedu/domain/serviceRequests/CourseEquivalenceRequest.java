package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CourseEquivalenceEvent;
import net.sourceforge.fenixedu.domain.student.Registration;

public class CourseEquivalenceRequest extends CourseEquivalenceRequest_Base {
    
    public  CourseEquivalenceRequest(final Registration registration, final Boolean urgentRequest, final Boolean freeProcessed) {
        super();
        super.init(registration, urgentRequest, freeProcessed);
        new CourseEquivalenceEvent(getAdministrativeOffice(),EventType.CURRICULAR_COURSE_EQUIVALENCE_FEE,registration.getPerson(),this);
    }
 
    public CourseEquivalenceRequest(final Registration registration, final Boolean urgentRequest, final Boolean freeProcessed, CurricularCourse requestedCurricularCourse, CurricularCourse proposedCurricularCourse) {
	this(registration, urgentRequest, freeProcessed);
	setRequestedCurricularCourse(requestedCurricularCourse);
	setProposedCurricularCourse(proposedCurricularCourse);
    }
    
    public CourseEquivalenceEvent getCourseEquivalenceEvent() {
	return (CourseEquivalenceEvent)getEvent();
    }

    @Override
    public String getDescription() {
	return "Course Equivalent Request Description";
    }

    @Override
    public EventType getEventType() {
	return EventType.CURRICULAR_COURSE_EQUIVALENCE_FEE;
    }

    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }

}
