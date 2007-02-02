package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

public abstract class AcademicServiceRequest extends AcademicServiceRequest_Base {

    protected AcademicServiceRequest() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
	super.setServiceRequestNumber(RootDomainObject.getInstance().getAcademicServiceRequestsCount());
    }

    protected void init(Registration registration) {

	final Person loggedPerson = AccessControl.getPerson();

	final AdministrativeOffice administrativeOffice;
	if (loggedPerson != null && loggedPerson.hasEmployee()
		&& loggedPerson.getEmployee().isAdministrativeOfficeEmployee()) {
	    administrativeOffice = loggedPerson.getEmployee().getAdministrativeOffice();
	} else {
	    administrativeOffice = AdministrativeOffice.getResponsibleAdministrativeOffice(registration
		    .getDegree());
	}

	checkParameters(registration, administrativeOffice);
	super.setAdministrativeOffice(administrativeOffice);
	super.setRegistration(registration);
	new AcademicServiceRequestSituation(this, AcademicServiceRequestSituationType.NEW, AccessControl
		.getPerson().getEmployee(), null);
    }

    private void checkParameters(Registration registration, AdministrativeOffice administrativeOffice) {
	if (registration == null) {
	    throw new DomainException(
		    "error.serviceRequests.AcademicServiceRequest.registration.cannot.be.null");
	}
	if (administrativeOffice == null) {
	    throw new DomainException(
		    "error.serviceRequests.AcademicServiceRequest.administrativeOffice.cannot.be.null");
	}
    }

    public boolean isUrgentRequest() {
	return getUrgentRequest().booleanValue();
    }

    final public boolean isFreeProcessed() {
	return getFreeProcessed();
    }
    
    protected boolean isFree() {
	return !isPayable() || isFreeProcessed();
    }

    protected final boolean isPayable() {
	return getEventType() != null;
    }

    protected boolean isPayed() {
	return !hasEvent() || getEvent().isPayed();
    }

    abstract public EventType getEventType();

    abstract public String getDescription();

    abstract public ExecutionYear getExecutionYear();
    
    public boolean hasExecutionYear() {
	return getExecutionYear() != null;
    }

    protected String getDescription(final String academicServiceRequestType,
	    final String specificServiceType) {
	final ResourceBundle enumerationResources = ResourceBundle.getBundle(
		"resources.EnumerationResources", LanguageUtils.getLocale());

	final StringBuilder result = new StringBuilder();
	result.append(enumerationResources.getString(academicServiceRequestType));
	if (specificServiceType != null) {
	    result.append(": ");
	    result.append(enumerationResources.getString(specificServiceType));
	}

	return result.toString();
    }

    public abstract Set<AdministrativeOfficeType> getPossibleAdministrativeOffices();

    final public void process() throws DomainException {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.PROCESSING, employee, null);
    }

    final public void reject(final String justification) {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.REJECTED, employee, justification);
    }

    final public void cancel(final String justification) {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.CANCELLED, employee, justification);
    }

    final public void conclude() {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.CONCLUDED, employee, null);
    }

    final public void delivered() {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.DELIVERED, employee, null);
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.administrativeOffice");
    }

    public void setRegistration(Registration registration) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.registration");
    }

    @Override
    public void setServiceRequestNumber(Integer serviceRequestNumber) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.serviceRequestNumber");
    }

    @Override
    public void addAcademicServiceRequestSituations(
	    AcademicServiceRequestSituation academicServiceRequestSituation) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.add.academicServiceRequestSituation");
    }

    @Override
    public void setEvent(AcademicServiceRequestEvent event) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.event");
    }

    @Override
    public List<AcademicServiceRequestSituation> getAcademicServiceRequestSituations() {
	return Collections.unmodifiableList(super.getAcademicServiceRequestSituations());
    }

    @Override
    public Set<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsSet() {
	return Collections.unmodifiableSet(super.getAcademicServiceRequestSituationsSet());
    }

    @Override
    public Iterator<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsIterator() {
	return getAcademicServiceRequestSituationsSet().iterator();
    }

    @Override
    public void removeAcademicServiceRequestSituations(
	    AcademicServiceRequestSituation academicServiceRequestSituation) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.remove.academicServiceRequestSituation");
    }

    public AcademicServiceRequestSituation getActiveSituation() {
	return (!getAcademicServiceRequestSituations().isEmpty()) ? (AcademicServiceRequestSituation) Collections
		.max(getAcademicServiceRequestSituations(), new BeanComparator("creationDate"))
		: null;
    }
    
    public AcademicServiceRequestSituation getCreationSituation() {
	return getSituationByType(AcademicServiceRequestSituationType.NEW);
    }

    public AcademicServiceRequestSituation getSituationByType(AcademicServiceRequestSituationType type) {
	for (AcademicServiceRequestSituation situation : getAcademicServiceRequestSituationsSet()) {
	    if (situation.getAcademicServiceRequestSituationType().equals(type)) {
		return situation;
	    }
	}
	return null;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
	return getActiveSituation().getAcademicServiceRequestSituationType();
    }

    public void edit(AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    Employee employee, String justification) {

	if (!isEditable()) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.is.not.editable");
	}

	if (getAcademicServiceRequestSituationType() != academicServiceRequestSituationType) {

	    checkRulesToChangeState(academicServiceRequestSituationType);

	    internalChangeState(academicServiceRequestSituationType, employee);

	    new AcademicServiceRequestSituation(this, academicServiceRequestSituationType, employee,
		    justification);
	} else {
	    getActiveSituation().edit(employee, justification);
	}

    }

    private void checkRulesToChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType) {

	final List<AcademicServiceRequestSituationType> acceptedTypes = getAcceptAcademicServiceRequestSituationTypeFor(getAcademicServiceRequestSituationType());

	if (!acceptedTypes.contains(academicServiceRequestSituationType)) {
	    final LabelFormatter sourceLabelFormatter = new LabelFormatter().appendLabel(
		    getActiveSituation().getAcademicServiceRequestSituationType().getQualifiedName(),
		    "enum");
	    final LabelFormatter targetLabelFormatter = new LabelFormatter().appendLabel(
		    academicServiceRequestSituationType.getQualifiedName(), "enum");

	    throw new DomainExceptionWithLabelFormatter(
		    "error.serviceRequests.AcademicServiceRequest.cannot.change.from.source.state.to.target.state",
		    sourceLabelFormatter, targetLabelFormatter);
	}
    }

    private List<AcademicServiceRequestSituationType> getAcceptAcademicServiceRequestSituationTypeFor(
	    AcademicServiceRequestSituationType situationType) {

	switch (situationType) {

	case NEW:
 		return Collections.unmodifiableList(Arrays.asList(new AcademicServiceRequestSituationType[] {
		    AcademicServiceRequestSituationType.CANCELLED,
		    AcademicServiceRequestSituationType.REJECTED,
		    AcademicServiceRequestSituationType.PROCESSING }));

	case PROCESSING:
	    return Collections.unmodifiableList(Arrays.asList(new AcademicServiceRequestSituationType[] {
		    AcademicServiceRequestSituationType.CANCELLED,
		    AcademicServiceRequestSituationType.REJECTED,
		    AcademicServiceRequestSituationType.CONCLUDED }));

	case CONCLUDED:
	    return Collections.singletonList(AcademicServiceRequestSituationType.DELIVERED);

	default:
	    return Collections.EMPTY_LIST;
	}
    }

    protected void internalChangeState(AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
	if ((academicServiceRequestSituationType == AcademicServiceRequestSituationType.CANCELLED 
		|| academicServiceRequestSituationType == AcademicServiceRequestSituationType.REJECTED)
		&& hasEvent()) {
	    getEvent().cancel(employee);
	}
	
	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.DELIVERED) {
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	}
    }

    public boolean isNewRequest() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.NEW);
    }

    public boolean isProcessing() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.PROCESSING);
    }

    public boolean isConcluded() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CONCLUDED);
    }

    public boolean isDelivered() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.DELIVERED);
    }

    public boolean isRejected() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.REJECTED);
    }

    public boolean isCancelled() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CANCELLED);
    }

    public boolean isHistorical() {
	return (isDelivered() || isRejected() || isCancelled());
    }

    public boolean isEditable() {
	return (isNewRequest() || isProcessing() || isConcluded());
    }

    public boolean finishedSuccessfully() {
	return (isConcluded() || isDelivered());
    }

    public boolean isDocumentRequest() {
	return this instanceof DocumentRequest;
    }
    
    public boolean createdByStudent(){
        return getCreationSituation().getEmployee() == null;
    }
    
    public boolean getStudentCanCancel() {
        return this.isNewRequest() && this.createdByStudent();
    }

    public DateTime getCreationDate() {
	return getCreationSituation().getCreationDate();
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	final ExecutionYear executionYear = hasExecutionYear() ? getExecutionYear() : ExecutionYear
		.readByDateTime(getCreationDate());
	return getRegistration().getStudentCurricularPlan(executionYear);
    }

    public Degree getDegree() {
	return getStudentCurricularPlan().getDegree();
    }

    public boolean isBolonha() {
	return getDegree().isBolonhaDegree();
    }

    public Campus getCampus() {
	return getStudentCurricularPlan().getCurrentCampus();
    }
    
    public boolean isAvailableForEmployeeToActUpon() {
	final Person loggedPerson = AccessControl.getPerson();

	if (loggedPerson.hasEmployee()) {
	    final Employee employee = loggedPerson.getEmployee();

	    return employee.getAdministrativeOffice() == getAdministrativeOffice()
		    && employee.getCurrentCampus() == getCampus();
	} else {
	    throw new DomainException(
		    "AcademicServiceRequest.non.employee.person.attempt.to.change.request");
	}
    }
    
}
