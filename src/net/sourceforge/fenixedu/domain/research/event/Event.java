package net.sourceforge.fenixedu.domain.research.event;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class Event extends Event_Base {

	public Event() {
		setRootDomainObject(RootDomainObject.getInstance());
	}
    
    public Event(MultiLanguageString name, EventType type) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setName(name);
        setEventType(type);
    }    

	public Event(YearMonthDay endDate, YearMonthDay startDate, String eventLocation, Boolean fee,
			EventType type, MultiLanguageString name) {
        super();
        
        if(name == null)
            throw new DomainException("errors.event.requiredAttributes");
		setRootDomainObject(RootDomainObject.getInstance());
		setEndDate(endDate);
		setStartDate(startDate);
		setEventLocation(eventLocation);
		setFee(fee);
		setName(name);
		setEventType(type);
	}

    /**
     * This method is responsible for deleting the object and all its references, particularly
     * EventParticipations and ProjectEventAssociations
     */
    public void delete(){
        for (;this.hasAnyEventParticipations(); getEventParticipations().get(0).delete());
        for (;this.hasAnyAssociatedProjects(); getAssociatedProjects().get(0).delete());
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    /**
     * This method is responsible for checking if the object still has active connections
     *if not, the object is deleted.
     */
    public void sweep(){
        if (!(this.hasAnyAssociatedProjects() || this.hasAnyEventParticipations() || this.hasAnyResultEventAssociations())){
            delete();
        }
    }
}
