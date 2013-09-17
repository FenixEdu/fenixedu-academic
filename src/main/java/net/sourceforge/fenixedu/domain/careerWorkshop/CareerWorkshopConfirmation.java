package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CareerWorkshopConfirmation extends CareerWorkshopConfirmation_Base {

    public CareerWorkshopConfirmation(Student student, CareerWorkshopConfirmationEvent confirmationEvent,
            CareerWorkshopApplication application) {
        super();

        if (student == null) {
            throw new DomainException("error.careerWorkshop.creatingNewApplication: Student cannot be a null value.");
        }
        setStudent(student);

        if (confirmationEvent == null) {
            throw new DomainException("error.careerWorkshop.creatingNewApplication: ConfirmationEvent cannot be a null value.");
        }
        setCareerWorkshopConfirmationEvent(confirmationEvent);

        if (application == null) {
            throw new DomainException("error.careerWorkshop.creatingNewApplication: Application cannot be a null value.");
        }
        setCareerWorkshopApplication(application);
    }

    @Atomic
    public void delete() {
        setStudent(null);
        setCareerWorkshopApplication(null);
        setCareerWorkshopConfirmationEvent(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Atomic
    @Override
    public void setConfirmation(Boolean confirmation) {
        super.setConfirmation(confirmation);
    }

    @Atomic
    @Override
    public void setConfirmationCode(String confirmationCode) {
        super.setConfirmationCode(confirmationCode);
    }

    @Atomic
    public void sealConfirmation() {
        DateTime timestamp = new DateTime();
        setSealStamp(timestamp);
        getCareerWorkshopConfirmationEvent().setLastUpdate(timestamp);
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasConfirmation() {
        return getConfirmation() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopConfirmationEvent() {
        return getCareerWorkshopConfirmationEvent() != null;
    }

    @Deprecated
    public boolean hasConfirmationCode() {
        return getConfirmationCode() != null;
    }

    @Deprecated
    public boolean hasSealStamp() {
        return getSealStamp() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopApplication() {
        return getCareerWorkshopApplication() != null;
    }

}
