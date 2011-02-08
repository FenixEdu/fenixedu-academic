package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent;

public class ManageCareerWorkshopApplicationsBean implements Serializable {
    
    private DateTime newEventStartDate;
    private DateTime newEventEndDate;
    private String newEventInformation;
    private List<CareerWorkshopApplicationEvent> existingEvents;
    
    public ManageCareerWorkshopApplicationsBean() {
	existingEvents = new ArrayList<CareerWorkshopApplicationEvent>(RootDomainObject.getInstance().getCareerWorkshopApplicationEvents());
    }
    
    public DateTime getNewEventStartDate() {
	return newEventStartDate;
    }
    
    public void setNewEventStartDate(DateTime newEventStartDate) {
	this.newEventStartDate = newEventStartDate;
    }
    
    public DateTime getNewEventEndDate() {
	return newEventEndDate;
    }
    
    public void setNewEventEndDate(DateTime newEventEndDate) {
	this.newEventEndDate = newEventEndDate;
    }
    
    public String getNewEventInformation() {
	return newEventInformation;
    }
    
    public void setNewEventInformation(String newEventInformation) {
	this.newEventInformation = newEventInformation;
    }
    
    public List<CareerWorkshopApplicationEvent> getCareerWorkshopApplicationEvents() {
	return existingEvents;
    }
    
    @Service
    public void addNewEvent() {
	new CareerWorkshopApplicationEvent(newEventStartDate, newEventEndDate, newEventInformation);
	
	setNewEventStartDate(null);
	setNewEventEndDate(null);
	setNewEventInformation(null);
	existingEvents = new ArrayList<CareerWorkshopApplicationEvent>(RootDomainObject.getInstance().getCareerWorkshopApplicationEvents());
    }
    
    @Service
    public void deleteEvent(CareerWorkshopApplicationEvent careerWorkshopApplicationEvent) {
	careerWorkshopApplicationEvent.delete();
	existingEvents = new ArrayList<CareerWorkshopApplicationEvent>(RootDomainObject.getInstance().getCareerWorkshopApplicationEvents());
    }
    
}
