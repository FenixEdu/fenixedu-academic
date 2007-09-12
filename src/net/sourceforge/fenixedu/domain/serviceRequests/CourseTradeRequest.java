package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CourseTradeEvent;
import net.sourceforge.fenixedu.domain.student.Registration;

public class CourseTradeRequest extends CourseTradeRequest_Base {
    
    public  CourseTradeRequest(final Registration registration, final Boolean urgentRequest, final Boolean freeProcessed) {
        super();
        init(registration, urgentRequest, freeProcessed);
        new CourseTradeEvent(getAdministrativeOffice(),EventType.CURRICULAR_COURSE_TRADE_FEE,registration.getPerson(),this);
    }
 
    public CourseTradeRequest(final Registration registration, final Boolean urgentRequest, final Boolean freeProcessed, CurricularCourse requestedCurricularCourse, CurricularCourse proposedCurricularCourse) {
	this(registration, urgentRequest, freeProcessed);
	setRequestedCurricularCourse(requestedCurricularCourse);
	setProposedCurricularCourse(proposedCurricularCourse);
    }
    
    public CourseTradeEvent getCourseTradeEvent() {
	return (CourseTradeEvent)getEvent();
    }

    @Override
    public String getDescription() {
	return "Description of CourseTradeRequest";
    }

    @Override
    public EventType getEventType() {
	return EventType.CURRICULAR_COURSE_TRADE_FEE;
    }

    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }

}
