package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

abstract public class RegistrationAcademicServiceRequest extends RegistrationAcademicServiceRequest_Base {

    protected RegistrationAcademicServiceRequest() {
	super();
    }

    protected void init(final Registration registration, final DateTime requestDate, final Boolean urgentRequest,
	    final Boolean freeProcessed) {
	init(registration, null, requestDate, urgentRequest, freeProcessed);
    }

    protected void init(final Registration registration, final ExecutionYear executionYear, final DateTime requestDate,
	    final Boolean urgentRequest, final Boolean freeProcessed) {
	// first set own parameters because of findAdministrativeOffice
	checkParameters(registration);
	super.setRegistration(registration);
	// then set super parameters
	super.init(executionYear, requestDate, urgentRequest, freeProcessed);
    }

    private void checkParameters(final Registration registration) {
	if (registration == null) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.registration.cannot.be.null");
	} else if (!isAvailableForTransitedRegistrations() && registration.isTransited()) {
	    throw new DomainException("RegistrationAcademicServiceRequest.registration.cannot.be.transited");
	}
    }

    @Override
    protected AdministrativeOffice findAdministrativeOffice() {
	AdministrativeOffice administrativeOffice = super.findAdministrativeOffice();
	if (administrativeOffice == null) {
	    administrativeOffice = AdministrativeOffice.getResponsibleAdministrativeOffice(getRegistration().getDegree());
	}
	return administrativeOffice;
    }

    @Override
    public void setRegistration(Registration registration) {
	throw new DomainException("error.serviceRequests.RegistrationAcademicServiceRequest.cannot.modify.registration");
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	final ExecutionYear executionYear = hasExecutionYear() ? getExecutionYear() : ExecutionYear
		.readByDateTime(getRequestDate());
	return getRegistration().getStudentCurricularPlan(executionYear);
    }

    public Degree getDegree() {
	return getStudentCurricularPlan().getDegree();
    }

    public DegreeType getDegreeType() {
	return getDegree().getDegreeType();
    }

    public boolean isBolonha() {
	return getDegree().isBolonhaDegree();
    }

    public Campus getCampus() {
	final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan();
	return studentCurricularPlan != null ? studentCurricularPlan.getCurrentCampus() : null;
    }

    @Override
    public boolean isAvailableForEmployeeToActUpon() {
	final Person loggedPerson = AccessControl.getPerson();
	if (loggedPerson.hasEmployee()) {
	    final Employee employee = loggedPerson.getEmployee();
	    return employee.getAdministrativeOffice() == getAdministrativeOffice() && employee.getCurrentCampus() == getCampus();
	} else {
	    throw new DomainException("RegistrationAcademicServiceRequest.non.employee.person.attempt.to.change.request");
	}
    }

    @Override
    public boolean isRequestForRegistration() {
	return true;
    }

    @Override
    public void delete() {
	super.setRegistration(null);
	super.delete();
    }

    @Override
    public Person getPerson() {
	return getRegistration().getPerson();
    }

    abstract public boolean isAvailableForTransitedRegistrations();

}
