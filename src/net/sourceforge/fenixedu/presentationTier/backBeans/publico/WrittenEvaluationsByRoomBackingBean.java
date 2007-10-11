package net.sourceforge.fenixedu.presentationTier.backBeans.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;

public class WrittenEvaluationsByRoomBackingBean extends net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation.WrittenEvaluationsByRoomBackingBean {

    @Override
    public Map<AllocatableSpace, List<CalendarLink>> getWrittenEvaluationCalendarLinks() throws FenixFilterException, FenixServiceException {
	final Collection<AllocatableSpace> rooms = getRoomsToDisplayMap();	
	if (rooms != null) {
	    final Map<AllocatableSpace, List<CalendarLink>> calendarLinksMap = new HashMap<AllocatableSpace, List<CalendarLink>>();                
	    for (final AllocatableSpace room : rooms) {
		final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
		for (final ResourceAllocation roomOccupation : room.getResourceAllocations()) {
		    if(roomOccupation.isWrittenEvaluationSpaceOccupation()) {
			List<WrittenEvaluation> writtenEvaluations = ((WrittenEvaluationSpaceOccupation)roomOccupation).getWrittenEvaluations();
			for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {			    		
			    if(verifyWrittenEvaluationExecutionPeriod(writtenEvaluation, getExecutionPeriod())) {
				final ExecutionCourse executionCourse = writtenEvaluation.getAssociatedExecutionCourses().get(0);
				final CalendarLink calendarLink = new CalendarLink();
				calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
				calendarLink.setObjectLinkLabel(constructEvaluationCalendarPresentarionString(writtenEvaluation, executionCourse));
				calendarLink.setLinkParameters(constructLinkParameters(executionCourse, writtenEvaluation));
				calendarLinks.add(calendarLink);
			    }   
			}
		    }
		} 
		calendarLinksMap.put(room, calendarLinks);
	    }            
	    return calendarLinksMap;
	} else {
	    return null;
	}
    }
    
    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse, final WrittenEvaluation writtenEvaluation) {
	final Map<String, String> linkParameters = new HashMap<String, String>();	
	linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
	linkParameters.put("method", "firstPage");	
	return linkParameters;
    }
}
