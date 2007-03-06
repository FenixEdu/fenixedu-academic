package net.sourceforge.fenixedu.domain.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class Event extends Event_Base {

	public Event() {
		super();
        setOjbConcreteClass(getClass().getName());
	}
    
    public Event(String name, EventType type) {
    	super();
        setOjbConcreteClass(getClass().getName());
        setName(name);
        setEventType(type);
    }    

	public Event(YearMonthDay endDate, YearMonthDay startDate, String eventLocation,
			EventType type, String name) {
		super();
        setOjbConcreteClass(getClass().getName());
        
        if(name == null)
            throw new DomainException("errors.event.requiredAttributes");

		setEndDate(endDate);
		setStartDate(startDate);
		setEventLocation(eventLocation);
		setName(name);
		setEventType(type);
	}

    /**
     * This method is responsible for deleting the object and all its references, particularly
     * Participations and ProjectEventAssociations
     */
    public void delete(){
        for (;this.hasAnyAssociatedProjects(); getAssociatedProjects().get(0).delete());
        super.delete();
    }
    
    /**
     * This method is responsible for checking if the object still has active connections
     *if not, the object is deleted.
     */
    public void sweep(){
        if (!(this.hasAnyAssociatedProjects() || this.hasAnyParticipations() || this.hasAnyResultEventAssociations())){
            delete();
        }
    }
    
    @Override
    public List<ResearchActivityParticipationRole> getAllowedRoles(){
    	return ResearchActivityParticipationRole.getAllEventParticipationRoles();
    }
}
