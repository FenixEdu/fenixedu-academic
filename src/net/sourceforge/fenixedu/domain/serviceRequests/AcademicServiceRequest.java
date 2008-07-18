package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

abstract public class AcademicServiceRequest extends AcademicServiceRequest_Base {

    private static final String SERVICE_REQUEST_NUMBER_YEAR_SEPARATOR = "/";

    public static final Comparator<AcademicServiceRequest> COMPARATOR_BY_NUMBER = new Comparator<AcademicServiceRequest>() {
	public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
	    return o1.getServiceRequestNumber().compareTo(o2.getServiceRequestNumber());
	}
    };

    public static final Comparator<AcademicServiceRequest> EXECUTION_YEAR_COMPARATOR = new Comparator<AcademicServiceRequest>() {
	public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
	    if (!o1.hasExecutionYear() && !o2.hasExecutionYear()) {
		return 0;
	    } else if (o1.hasExecutionYear() && !o2.hasExecutionYear()) {
		return 1;
	    } else if (!o1.hasExecutionYear() && o2.hasExecutionYear()) {
		return -1;
	    }

	    return ExecutionYear.COMPARATOR_BY_YEAR.compare(o1.getExecutionYear(), o2.getExecutionYear());
	}
    };

    public static final Comparator<AcademicServiceRequest> EXECUTION_YEAR_AND_OID_COMPARATOR = new Comparator<AcademicServiceRequest>() {
	public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(EXECUTION_YEAR_COMPARATOR);
	    comparatorChain.addComparator(COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    protected AcademicServiceRequest() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setCreationDate(new DateTime());
    }

    protected void init(final DateTime requestDate, final Boolean urgentRequest, final Boolean freeProcessed) {
	init(null, requestDate, urgentRequest, freeProcessed);
    }

    protected void init(final ExecutionYear executionYear, final DateTime requestDate, final Boolean urgentRequest,
	    final Boolean freeProcessed) {

	final AdministrativeOffice administrativeOffice = findAdministrativeOffice();
	checkParameters(administrativeOffice, requestDate, urgentRequest, freeProcessed);

	super.setServiceRequestYear(requestDate.year().get());
	super.setAcademicServiceRequestYear(generateAcademicServiceRequestYear());
	super.setServiceRequestNumber(getAcademicServiceRequestYear().generateServiceRequestNumber());
	super.setRequestDate(requestDate);

	super.setAdministrativeOffice(administrativeOffice);
	super.setUrgentRequest(urgentRequest);
	super.setFreeProcessed(freeProcessed);
	super.setExecutionYear(executionYear);

	final AcademicServiceRequestBean bean = new AcademicServiceRequestBean(AcademicServiceRequestSituationType.NEW,
		getEmployee());
	bean.setSituationDate(requestDate.toYearMonthDay());

	createAcademicServiceRequestSituations(bean);
    }

    private AcademicServiceRequestYear generateAcademicServiceRequestYear() {
	final AcademicServiceRequestYear result = AcademicServiceRequestYear.readByYear(getServiceRequestYear());
	return result == null ? new AcademicServiceRequestYear(getServiceRequestYear()) : result;
    }

    private Employee getEmployee() {
	final Person person = AccessControl.getPerson();
	return person == null ? null : person.getEmployee();
    }

    protected AdministrativeOffice findAdministrativeOffice() {
	final Person person = AccessControl.getPerson();
	return person != null ? person.getEmployeeAdministrativeOffice() : null;
    }

    private void checkParameters(final AdministrativeOffice administrativeOffice, final DateTime requestDate,
	    final Boolean urgentRequest, final Boolean freeProcessed) {

	if (administrativeOffice == null) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.administrativeOffice.cannot.be.null");
	}

	if (requestDate == null || requestDate.isAfterNow()) {
	    throw new DomainException("error.serviceRequests.AcademicServiceRequest.invalid.requestDate");
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
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.urgentRequest");
    }

    @Override
    final public void setFreeProcessed(Boolean freeProcessed) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.freeProcessed");
    }

    @Override
    final public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.executionYear");
    }

    @Override
    final public void setCreationDate(DateTime creationDate) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.creationDate");
    }

    @Override
    final public void setRequestDate(DateTime requestDate) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.requestDate");
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

    abstract public boolean isPayedUponCreation();

    protected String getDescription(final AcademicServiceRequestType academicServiceRequestType, final String specificServiceType) {
	final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", Language
		.getLocale());
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

    public String getDescription() {
	return getDescription(getAcademicServiceRequestType());
    }

    final public void process() throws DomainException {
	process(getEmployee());
    }

    final public void process(final Employee employee) throws DomainException {
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.PROCESSING, employee));
    }

    final public void sendToExternalEntity(final YearMonthDay sendDate, final String description) {
	final Employee employee = getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY, employee, sendDate,
		description));
    }

    final public void receivedFromExternalEntity(final YearMonthDay receivedDate, final String description) {
	final Employee employee = getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY, employee,
		receivedDate, description));
    }

    final public void reject(final String justification) {
	final Employee employee = getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.REJECTED, employee, justification));
    }

    final public void cancel(final String justification) {
	final Employee employee = getEmployee();
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CANCELLED, employee, justification));
    }

    final public void conclude() {
	conclude(getEmployee());
    }

    final public void conclude(final Employee employee) {
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CONCLUDED, employee));
    }

    final public void delivered() {
	delivered(getEmployee());
    }

    final public void delivered(final Employee employee) {
	edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.DELIVERED, employee));
    }

    public void delete() {
	checkRulesToDelete();

	for (; !getAcademicServiceRequestSituations().isEmpty(); getAcademicServiceRequestSituations().iterator().next().delete())
	    ;
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
    final public void setServiceRequestYear(Integer serviceRequestYear) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.serviceRequestYear");
    }

    @Override
    final public void setServiceRequestNumber(Integer serviceRequestNumber) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.serviceRequestNumber");
    }

    final public String getServiceRequestNumberYear() {
	return getServiceRequestNumber() + SERVICE_REQUEST_NUMBER_YEAR_SEPARATOR + getServiceRequestYear();
    }

    @Override
    final public void addAcademicServiceRequestSituations(AcademicServiceRequestSituation academicServiceRequestSituation) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.add.academicServiceRequestSituation");
    }

    @Override
    final public void setEvent(AcademicServiceRequestEvent event) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.modify.event");
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
    final public void removeAcademicServiceRequestSituations(AcademicServiceRequestSituation academicServiceRequestSituation) {
	throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.remove.academicServiceRequestSituation");
    }

    final public AcademicServiceRequestSituation getActiveSituation() {
	return (!getAcademicServiceRequestSituations().isEmpty()) ? (AcademicServiceRequestSituation) Collections.min(
		getAcademicServiceRequestSituations(),
		AcademicServiceRequestSituation.COMPARATOR_BY_MOST_RECENT_CREATION_DATE_AND_ID) : null;
    }

    final public DateTime getActiveSituationDate() {
	return getActiveSituation().getSituationDate();
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

    private void checkRulesToChangeState(final AcademicServiceRequestSituationType situationType) {

	if (!isAcceptedSituationType(situationType)) {
	    final LabelFormatter sourceLabelFormatter = new LabelFormatter().appendLabel(getActiveSituation()
		    .getAcademicServiceRequestSituationType().getQualifiedName(), "enum");
	    final LabelFormatter targetLabelFormatter = new LabelFormatter()
		    .appendLabel(situationType.getQualifiedName(), "enum");

	    throw new DomainExceptionWithLabelFormatter(
		    "error.serviceRequests.AcademicServiceRequest.cannot.change.from.source.state.to.target.state",
		    sourceLabelFormatter, targetLabelFormatter);
	}
    }

    final private boolean isAcceptedSituationType(final AcademicServiceRequestSituationType situationType) {
	return getAcceptedSituationTypes(getAcademicServiceRequestSituationType()).contains(situationType);
    }

    final public boolean isCancelledSituationAccepted() {
	return isAcceptedSituationType(AcademicServiceRequestSituationType.CANCELLED);
    }

    final public boolean isSendToExternalEntitySituationAccepted() {
	return isAcceptedSituationType(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY);
    }

    final public boolean isConcludedSituationAccepted() {
	return isAcceptedSituationType(AcademicServiceRequestSituationType.CONCLUDED);
    }

    final public boolean isDeliveredSituationAccepted() {
	return isAcceptedSituationType(AcademicServiceRequestSituationType.DELIVERED);
    }

    private List<AcademicServiceRequestSituationType> getAcceptedSituationTypes(AcademicServiceRequestSituationType situationType) {

	switch (situationType) {

	case NEW:
	    return getNewSituationAcceptedSituationsTypes();

	case PROCESSING:
	    return getProcessingSituationAcceptedSituationsTypes();

	case SENT_TO_EXTERNAL_ENTITY:
	    return getSentToExternalEntitySituationAcceptedSituationsTypes();

	case RECEIVED_FROM_EXTERNAL_ENTITY:
	    return getReceivedFromExternalEntitySituationAcceptedSituationsTypes();

	case CONCLUDED:
	    return getConcludedSituationAcceptedSituationsTypes();

	default:
	    return Collections.EMPTY_LIST;
	}
    }

    protected List<AcademicServiceRequestSituationType> getNewSituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
		AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.PROCESSING));
    }

    protected List<AcademicServiceRequestSituationType> getProcessingSituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
		AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY,
		AcademicServiceRequestSituationType.CONCLUDED));
    }

    protected List<AcademicServiceRequestSituationType> getSentToExternalEntitySituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Collections
		.singletonList(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY));
    }

    protected List<AcademicServiceRequestSituationType> getReceivedFromExternalEntitySituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
		AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.CONCLUDED));
    }

    protected List<AcademicServiceRequestSituationType> getConcludedSituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
		AcademicServiceRequestSituationType.DELIVERED));
    }

    /** This method is overwritten in the subclasses */
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {

	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	}

	if (academicServiceRequestBean.isToProcess() && hasPersonalInfo() && hasMissingPersonalInfo()) {
	    throw new DomainException("AcademicServiceRequest.has.missing.personal.info");
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

    final public boolean hasProcessed() {
	return (getSituationByType(AcademicServiceRequestSituationType.PROCESSING) != null);
    }

    final public boolean isSentToExternalEntity() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY);
    }

    final public boolean isConcluded() {
	return (getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CONCLUDED);
    }

    final public boolean hasConcluded() {
	return (getSituationByType(AcademicServiceRequestSituationType.CONCLUDED) != null);
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
	return !(isRejected() || isCancelled() || isDelivered());
    }

    final public boolean finishedSuccessfully() {
	return isConcluded() || isDelivered();
    }

    final public boolean finishedUnsuccessfully() {
	return isRejected() || isCancelled();
    }

    final public boolean isDocumentRequest() {
	return this instanceof DocumentRequest;
    }

    abstract public boolean isToPrint();

    public boolean isRequestAvailableToSendToExternalEntity() {
	return isPossibleToSendToOtherEntity() && isSendToExternalEntitySituationAccepted();
    }

    /**
     * Indicates if is possible to AdministrativeOffice send this request to
     * another entity
     */
    abstract public boolean isPossibleToSendToOtherEntity();

    final public boolean createdByStudent() {
	return !getCreationSituation().hasEmployee();
    }

    final public boolean getLoggedPersonCanCancel() {
	return isCancelledSituationAccepted()
		&& (!isPayable() || !hasEvent() || !isPayed())
		&& (createdByStudent() || (AccessControl.getPerson().hasEmployee() && getAdministrativeOffice() == getEmployee()
			.getAdministrativeOffice()));
    }

    @Override
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

    public boolean isFor(final ExecutionYear executionYear) {
	return hasExecutionYear() && getExecutionYear().equals(executionYear);
    }

    abstract public Person getPerson();

    abstract public EventType getEventType();

    abstract public AcademicServiceRequestType getAcademicServiceRequestType();

    abstract public boolean hasPersonalInfo();

    protected boolean hasMissingPersonalInfo() {
	final List<String> toTest = new ArrayList<String>();

	toTest.add(getPerson().getParishOfBirth());
	toTest.add(getPerson().getDistrictOfBirth());

	for (final String testing : toTest) {
	    if (testing == null || StringUtils.isEmpty(testing)) {
		return true;
	    }
	}

	return false;
    }

}
