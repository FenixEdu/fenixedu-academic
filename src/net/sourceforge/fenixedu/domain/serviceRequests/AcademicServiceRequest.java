package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.commons.beanutils.BeanComparator;

public abstract class AcademicServiceRequest extends AcademicServiceRequest_Base {

    protected AcademicServiceRequest() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
	super.setServiceRequestNumber(RootDomainObject.getInstance().getAcademicServiceRequestsCount());
    }

    protected void init(Registration registration) {

	AdministrativeOffice administrativeOffice = null;
	final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	if (employee != null) {
	    administrativeOffice = AdministrativeOffice.readByEmployee(employee);
	}
	if (administrativeOffice == null) {
	    administrativeOffice = AdministrativeOffice.getResponsibleAdministrativeOffice(registration
		    .getDegree());
	}

	checkParameters(registration, administrativeOffice);
	super.setAdministrativeOffice(administrativeOffice);
	super.setRegistration(registration);
	new AcademicServiceRequestSituation(this, AcademicServiceRequestSituationType.NEW, employee,
		null);
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

    public final boolean isPayable() {
	return getEventType() != null;
    }

    public abstract EventType getEventType();

    public abstract String getDescription();

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

    /**
         * Each AcademicServiceRequest must verify the conditions necessary for
         * it to begin processing and throw DomainExceptions if otherwise
         * 
         * @throws DomainException
         */
    protected abstract void assertProcessingStatePreConditions() throws DomainException;

    final public void process() throws DomainException {
	assertProcessingStatePreConditions();

	final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.PROCESSING, employee, null);
    }

    final public void reject(final String justification) {
	final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.REJECTED, employee, justification);
    }

    final public void cancel(final String justification) {
	final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.CANCELLED, employee, justification);
    }

    /**
         * Each AcademicServiceRequest must verify the conditions necessary for
         * it to begin processing and throw DomainExceptions if otherwise
         * 
         * @throws DomainException
         */
    protected abstract void assertConcludedStatePreConditions() throws DomainException;

    final public void conclude() {
	assertConcludedStatePreConditions();

	final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	edit(AcademicServiceRequestSituationType.CONCLUDED, employee, null);
    }

    final public void delivered() {
	final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
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

	if (getActiveSituation() != null) {
	    if (!acceptedTypes.contains(academicServiceRequestSituationType)) {
		final LabelFormatter sourceLabelFormatter = new LabelFormatter()
			.appendLabel(getActiveSituation().getAcademicServiceRequestSituationType()
				.getQualifiedName(), "enum");
		final LabelFormatter targetLabelFormatter = new LabelFormatter().appendLabel(
			academicServiceRequestSituationType.getQualifiedName(), "enum");

		throw new DomainExceptionWithLabelFormatter(
			"error.serviceRequests.AcademicServiceRequest.cannot.change.from.source.state.to.target.state",
			sourceLabelFormatter, targetLabelFormatter);
	    }
	} else {
	    if (!acceptedTypes.contains(academicServiceRequestSituationType)) {
		final LabelFormatter labelFormatter = new LabelFormatter().appendLabel(
			academicServiceRequestSituationType.name(), "enum");

		throw new DomainExceptionWithLabelFormatter(
			"error.serviceRequests.AcademicServiceRequest.cannot.change.state.",
			labelFormatter);

	    }
	}
    }

    private List<AcademicServiceRequestSituationType> getAcceptAcademicServiceRequestSituationTypeFor(
	    AcademicServiceRequestSituationType situationType) {

	if (situationType == null) {
	    return Collections.unmodifiableList(Arrays.asList(new AcademicServiceRequestSituationType[] {
		    AcademicServiceRequestSituationType.CANCELLED,
		    AcademicServiceRequestSituationType.REJECTED,
		    AcademicServiceRequestSituationType.PROCESSING }));
	} else {
	    switch (situationType) {

	    case PROCESSING:
		return Collections.unmodifiableList(Arrays
			.asList(new AcademicServiceRequestSituationType[] {
				AcademicServiceRequestSituationType.CANCELLED,
				AcademicServiceRequestSituationType.REJECTED,
				AcademicServiceRequestSituationType.CONCLUDED }));

	    case CONCLUDED:
		return Collections.singletonList(AcademicServiceRequestSituationType.DELIVERED);

	    default:
		return Collections.EMPTY_LIST;
	    }

	}
    }

    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
	// nothing to be done
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

}
