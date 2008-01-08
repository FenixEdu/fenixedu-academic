package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

abstract public class AcademicServiceRequest extends AcademicServiceRequest_Base {

    static final Comparator<AcademicServiceRequest> COMPARATOR_BY_NUMBER = new Comparator<AcademicServiceRequest>() {
        public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
            return o1.getServiceRequestNumber().compareTo(o2.getServiceRequestNumber());
        }
    };
    
    protected AcademicServiceRequest() {
	super();
	super.setServiceRequestNumber(generateServiceRequestNumber());
	super.setRootDomainObject(RootDomainObject.getInstance());
    }
    
    private Integer generateServiceRequestNumber() {
	final SortedSet<AcademicServiceRequest> requests = new TreeSet<AcademicServiceRequest>(COMPARATOR_BY_NUMBER);
	requests.addAll(RootDomainObject.getInstance().getAcademicServiceRequestsSet());
	return requests.isEmpty() ? 1 : requests.last().getServiceRequestNumber() + 1;
    }

    protected void init(final Boolean urgentRequest, final Boolean freeProcessed) {
	init(null, urgentRequest, freeProcessed);
    }
    
    protected void init(final ExecutionYear executionYear, final Boolean urgentRequest, final Boolean freeProcessed) {

	final AdministrativeOffice administrativeOffice = findAdministrativeOffice();
	checkParameters(administrativeOffice, urgentRequest, freeProcessed);
	
	super.setAdministrativeOffice(administrativeOffice);
	super.setUrgentRequest(urgentRequest);
	super.setFreeProcessed(freeProcessed);
	super.setExecutionYear(executionYear);
	
	createAcademicServiceRequestSituations(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.NEW,
		AccessControl.getPerson().getEmployee()));
    }
    
    protected AdministrativeOffice findAdministrativeOffice() {
	final Person person = AccessControl.getPerson();
	return person != null ? person.getEmployeeAdministrativeOffice() : null;
    }

    private void checkParameters(final AdministrativeOffice administrativeOffice, final Boolean urgentRequest,
	    final Boolean freeProcessed) {

	if (administrativeOffice == null) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.administrativeOffice.cannot.be.null");
	}
	if (urgentRequest == null) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.urgentRequest.cannot.be.null");
	}
	if (freeProcessed == null) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.freeProcessed.cannot.be.null");
	}
    }

    @Override
    final public void setUrgentRequest(Boolean urgentRequest) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.urgentRequest");
    }

    @Override
    final public void setFreeProcessed(Boolean freeProcessed) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.freeProcessed");
    }
    
    @Override
    final public void setExecutionYear(ExecutionYear executionYear) {
        throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.executionYear");
    }

    final public boolean isUrgentRequest() {
	return getUrgentRequest().booleanValue();
    }

    final public boolean isFreeProcessed() {
	return getFreeProcessed();
    }
    
    public boolean isFree() {
	return !isPayable() || isFreeProcessed();
    }

    final protected boolean isPayable() {
	return getEventType() != null;
    }

    final protected boolean isPayed() {
	return !hasEvent() || getEvent().isPayed();
    }

    final public boolean getIsPayed() {
	return isPayed();
    }

    protected String getDescription(final AcademicServiceRequestType academicServiceRequestType, final String specificServiceType) {
	final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());
	final StringBuilder result = new StringBuilder();
	result.append(enumerationResources.getString(academicServiceRequestType.getQualifiedName()));
	if (specificServiceType != null) {
	    result.append(": ");
	    result.append(enumerationResources.getString(specificServiceType));
	}
	return result.toString();
    }
    
    protected String getDescription(final AcademicServiceRequestType academicServiceRequestType) {
	return getDescription(academicServiceRequestType, null);
    }

    final public void process() throws DomainException {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.PROCESSING, employee));
    }

    final public void reject(final String justification) {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.REJECTED, employee, justification));
    }

    final public void cancel(final String justification) {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CANCELLED, employee, justification));
    }

    final public void conclude() {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CONCLUDED, employee));
    }

    final public void delivered() {
	final Employee employee = AccessControl.getPerson().getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.DELIVERED, employee));
    }

    public void delete() {
	checkRulesToDelete();
	
	for (;!getAcademicServiceRequestSituations().isEmpty(); getAcademicServiceRequestSituations().iterator().next().delete());
	super.setAdministrativeOffice(null);
        if (hasEvent()) {
            getEvent().delete();
        }
        super.setExecutionYear(null);
        super.setRootDomainObject(null);
	super.deleteDomainObject();
    }
    
    protected void checkRulesToDelete() {
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	throw new DomainException("error.serviceRequests.RegistrationAcademicServiceRequest.cannot.modify.administrativeOffice");
    }
    
    @Override
    final public void setServiceRequestNumber(Integer serviceRequestNumber) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.serviceRequestNumber");
    }

    @Override
    final public void addAcademicServiceRequestSituations(
	    AcademicServiceRequestSituation academicServiceRequestSituation) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.add.academicServiceRequestSituation");
    }

    @Override
    final public void setEvent(AcademicServiceRequestEvent event) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.modify.event");
    }

    @Override
    final public List<AcademicServiceRequestSituation> getAcademicServiceRequestSituations() {
	return Collections.unmodifiableList(super.getAcademicServiceRequestSituations());
    }

    @Override
    final public Set<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsSet() {
	return Collections.unmodifiableSet(super.getAcademicServiceRequestSituationsSet());
    }

    @Override
    final public Iterator<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsIterator() {
	return getAcademicServiceRequestSituationsSet().iterator();
    }

    @Override
    final public void removeAcademicServiceRequestSituations(
	    AcademicServiceRequestSituation academicServiceRequestSituation) {
	throw new DomainException(
		"error.serviceRequests.AcademicServiceRequest.cannot.remove.academicServiceRequestSituation");
    }

    final public AcademicServiceRequestSituation getActiveSituation() {
	return (!getAcademicServiceRequestSituations().isEmpty()) ? (AcademicServiceRequestSituation) Collections.min(
		getAcademicServiceRequestSituations(), AcademicServiceRequestSituation.COMPARATOR_BY_MOST_RECENT_CREATION_DATE_AND_ID)
		: null;
    }
    
    final public AcademicServiceRequestSituation getCreationSituation() {
	return getSituationByType(AcademicServiceRequestSituationType.NEW);
    }

    final public AcademicServiceRequestSituation getSituationByType(final AcademicServiceRequestSituationType type) {
	for (final AcademicServiceRequestSituation situation : getAcademicServiceRequestSituationsSet()) {
	    if (situation.getAcademicServiceRequestSituationType().equals(type)) {
		return situation;
	    }
	}
	return null;
    }

    final public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
	return getActiveSituation().getAcademicServiceRequestSituationType();
    }

    public void edit(final AcademicServiceRequestBean academicServiceRequestBean) {

	if (!isEditable()) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.is.not.editable");
	}

	if (getAcademicServiceRequestSituationType() != academicServiceRequestBean.getAcademicServiceRequestSituationType()) {
	    checkRulesToChangeState(academicServiceRequestBean.getAcademicServiceRequestSituationType());
	    internalChangeState(academicServiceRequestBean);
	    createAcademicServiceRequestSituations(academicServiceRequestBean);
	    
	} else {
	    getActiveSituation().edit(academicServiceRequestBean);
	}

    }

    private void checkRulesToChangeState(AcademicServiceRequestSituationType academicServiceRequestSituationType) {

	final List<AcademicServiceRequestSituationType> acceptedTypes =
	    getAcceptAcademicServiceRequestSituationTypeFor(getAcademicServiceRequestSituationType());

	if (!acceptedTypes.contains(academicServiceRequestSituationType)) {
	    final LabelFormatter sourceLabelFormatter = new LabelFormatter().appendLabel(getActiveSituation()
		    .getAcademicServiceRequestSituationType().getQualifiedName(), "enum");
	    final LabelFormatter targetLabelFormatter = new LabelFormatter().appendLabel(academicServiceRequestSituationType
		    .getQualifiedName(), "enum");

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

    /** This method is overwritten in the subclasses */
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {

	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	}

	if (academicServiceRequestBean.isToDeliver()) {
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	}
    }
    
    protected void createAcademicServiceRequestSituations(final AcademicServiceRequestBean academicServiceRequestBean) {
	AcademicServiceRequestSituation.create(this, academicServiceRequestBean);
    }

    final public boolean isNewRequest() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.NEW);
    }

    final public boolean isProcessing() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.PROCESSING);
    }

    final public boolean isConcluded() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CONCLUDED);
    }

    final public boolean isDelivered() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.DELIVERED);
    }

    final public boolean isRejected() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.REJECTED);
    }

    final public boolean isCancelled() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CANCELLED);
    }

    final public boolean isHistorical() {
	return (isDelivered() || isRejected() || isCancelled());
    }

    final public boolean isEditable() {
	return (isNewRequest() || isProcessing() || isConcluded());
    }

    final public boolean finishedSuccessfully() {
	return (isConcluded() || isDelivered());
    }

    final public boolean isDocumentRequest() {
	return this instanceof DocumentRequest;
    }
    
    final public boolean createdByStudent(){
        return !getCreationSituation().hasEmployee();
    }
    
    final public boolean getLoggedPersonCanCancel() {
        return (isNewRequest() || isProcessing()) && (createdByStudent() || getAdministrativeOffice() == AccessControl.getPerson().getEmployee().getAdministrativeOffice());
    }
    
    final public DateTime getCreationDate() {
	return getCreationSituation().getCreationDate();
    }
    
    public boolean isAvailableForEmployeeToActUpon() {
	final Person loggedPerson = AccessControl.getPerson();
	if (loggedPerson.hasEmployee()) {
	    return loggedPerson.getEmployeeAdministrativeOffice() == getAdministrativeOffice();
	} else {
	    throw new DomainException("AcademicServiceRequest.non.employee.person.attempt.to.change.request");
	}
    }
    
    public boolean isRequestForPerson() {
	return false;
    }
    
    public boolean isRequestForRegistration() {
	return false;
    }
    
    abstract public EventType getEventType();
    abstract public String getDescription();
    abstract public Person getPerson();
}
