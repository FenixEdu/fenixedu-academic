package net.sourceforge.fenixedu.domain.careerWorkshop;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class CareerWorkshopConfirmation extends CareerWorkshopConfirmation_Base {
    
    public  CareerWorkshopConfirmation(Student student, CareerWorkshopConfirmationEvent confirmationEvent, CareerWorkshopApplication application) {
        super();
        
        if(student == null)
	    throw new DomainException("error.careerWorkshop.creatingNewApplication: Student cannot be a null value.");
	setStudent(student);
	
	if(confirmationEvent == null)
	    throw new DomainException("error.careerWorkshop.creatingNewApplication: ConfirmationEvent cannot be a null value.");
	setCareerWorkshopConfirmationEvent(confirmationEvent);
	
	if(application == null)
	    throw new DomainException("error.careerWorkshop.creatingNewApplication: Application cannot be a null value.");
	setCareerWorkshopApplication(application);
    }
    
    @Service
    @Override
    public void setConfirmation(Boolean confirmation) {
        super.setConfirmation(confirmation);
    }
    
    @Service
    @Override
    public void setConfirmationCode(String confirmationCode) {
        super.setConfirmationCode(confirmationCode);
    }
    
    @Service
    public void sealConfirmation() {
	DateTime timestamp = new DateTime();
	setSealStamp(timestamp);
	getCareerWorkshopConfirmationEvent().setLastUpdate(timestamp);
    }
    
}
