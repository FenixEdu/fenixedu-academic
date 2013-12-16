package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent;
import net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationEvent;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class ManageCareerWorkshopApplicationsBean implements Serializable {

    private DateTime newEventStartDate;
    private DateTime newEventEndDate;
    private String newEventInformation;
    private List<CareerWorkshopApplicationEvent> existingEvents;

    private DateTime confirmationStartDate;
    private DateTime confirmationEndDate;
    private CareerWorkshopApplicationEvent affectedEvent;

    public DateTime getConfirmationStartDate() {
        return confirmationStartDate;
    }

    public void setConfirmationStartDate(DateTime confirmationStartDate) {
        this.confirmationStartDate = confirmationStartDate;
    }

    public DateTime getConfirmationEndDate() {
        return confirmationEndDate;
    }

    public void setConfirmationEndDate(DateTime confirmationEndDate) {
        this.confirmationEndDate = confirmationEndDate;
    }

    public CareerWorkshopApplicationEvent getAffectedEvent() {
        return affectedEvent;
    }

    public void setAffectedEvent(CareerWorkshopApplicationEvent affectedEvent) {
        this.affectedEvent = affectedEvent;
    }

    public ManageCareerWorkshopApplicationsBean() {
        existingEvents =
                new ArrayList<CareerWorkshopApplicationEvent>(Bennu.getInstance().getCareerWorkshopApplicationEventsSet());
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

    @Atomic
    public void addNewEvent() {
        new CareerWorkshopApplicationEvent(newEventStartDate, newEventEndDate, newEventInformation);

        setNewEventStartDate(null);
        setNewEventEndDate(null);
        setNewEventInformation(null);
        existingEvents =
                new ArrayList<CareerWorkshopApplicationEvent>(Bennu.getInstance().getCareerWorkshopApplicationEventsSet());
    }

    @Atomic
    public void deleteEvent(CareerWorkshopApplicationEvent careerWorkshopApplicationEvent) {
        careerWorkshopApplicationEvent.delete();
        existingEvents =
                new ArrayList<CareerWorkshopApplicationEvent>(Bennu.getInstance().getCareerWorkshopApplicationEventsSet());
    }

    @Atomic
    public void addConfirmationPeriod() {
        new CareerWorkshopConfirmationEvent(affectedEvent, confirmationStartDate, confirmationEndDate);

        setConfirmationStartDate(null);
        setConfirmationEndDate(null);
        setAffectedEvent(null);
    }

}
