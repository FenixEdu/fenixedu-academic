/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.serviceRequests;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.treasury.IAcademicServiceRequestAndAcademicTaxTreasuryEvent;
import org.fenixedu.academic.domain.treasury.IAcademicTreasuryEvent;
import org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestCreateBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.AcademicPermissionService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

abstract public class AcademicServiceRequest extends AcademicServiceRequest_Base implements IAcademicServiceRequest {

    private static final String SERVICE_REQUEST_NUMBER_YEAR_SEPARATOR = "/";

    public static final Comparator<AcademicServiceRequest> COMPARATOR_BY_NUMBER = new Comparator<AcademicServiceRequest>() {
        @Override
        public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
            return o1.getServiceRequestNumber().compareTo(o2.getServiceRequestNumber());
        }
    };

    public static final Comparator<AcademicServiceRequest> EXECUTION_YEAR_COMPARATOR = new Comparator<AcademicServiceRequest>() {
        @Override
        public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
            if (o1.getExecutionYear() == null && o2.getExecutionYear() == null) {
                return 0;
            } else if (o1.getExecutionYear() != null && o2.getExecutionYear() == null) {
                return 1;
            } else if (o1.getExecutionYear() == null && o2.getExecutionYear() != null) {
                return -1;
            }

            return ExecutionYear.COMPARATOR_BY_YEAR.compare(o1.getExecutionYear(), o2.getExecutionYear());
        }
    };

    public static final Comparator<AcademicServiceRequest> EXECUTION_YEAR_AND_OID_COMPARATOR =
            new Comparator<AcademicServiceRequest>() {
                @Override
                public int compare(AcademicServiceRequest o1, AcademicServiceRequest o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(EXECUTION_YEAR_COMPARATOR);
                    comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

                    return comparatorChain.compare(o1, o2);
                }
            };

    protected AcademicServiceRequest() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final AcademicServiceRequestCreateBean bean, AdministrativeOffice office) {
        checkParameters(bean);

        super.setAdministrativeOffice(office);
        super.setAcademicServiceRequestYear(AcademicServiceRequestYear.readByYear(bean.getRequestDate().year().get(), true));
        super.setServiceRequestNumber(getAcademicServiceRequestYear().generateServiceRequestNumber());
        super.setRequestDate(bean.getRequestDate());

        super.setRequestedCycle(bean.getRequestedCycle());
        super.setDetailed(bean.getDetailed());

        if (bean.getHasCycleTypeDependency()) {
            if (bean.getRequestedCycle() == null) {
                throw new DomainException("error.registryDiploma.requestedCycleMustBeGiven");
            } else if (!bean.getRegistration().getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
                throw new DomainException("error.registryDiploma.requestedCycleTypeIsNotAllowedForGivenStudentCurricularPlan");
            }
            super.setRequestedCycle(bean.getRequestedCycle());
        } else if (bean.getRegistration().getDegreeType().hasExactlyOneCycleType()) {
            super.setRequestedCycle(bean.getRegistration().getDegreeType().getCycleType());
        }

        super.setUrgentRequest(bean.getUrgentRequest());
        super.setFreeProcessed(bean.getFreeProcessed());
        super.setExecutionYear(bean.getExecutionYear());
        super.setLanguage(bean.getLanguage());

        /*
         * TODO: Modify this setter when the AcademicServiceRequest creation gets
         * refactored!
         */
        setServiceRequestType(ServiceRequestType.findUnique(this));

        final AcademicServiceRequestBean situationBean =
                new AcademicServiceRequestBean(AcademicServiceRequestSituationType.NEW, AccessControl.getPerson());
        situationBean.setSituationDate(getRequestDate().toYearMonthDay());
        createAcademicServiceRequestSituations(situationBean);

        checkRules();
    }

    protected void checkRules() {
        // Ensure that are no service requests with same number and year
        if (getRootDomainObject().getAcademicServiceRequestsSet().stream()
                .filter(e -> e.getServiceRequestNumberYear().equals(getServiceRequestNumberYear())).count() > 1) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.duplicate.serviceRequestNumberYear");
        }
    }

    private int getServiceRequestYear() {
        return getAcademicServiceRequestYear().getYear();
    }

    private void checkParameters(final AcademicServiceRequestCreateBean bean) {
        if (bean.getRequestDate() == null || bean.getRequestDate().isAfterNow()) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.invalid.requestDate");
        }
        if (bean.getUrgentRequest() == null) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.urgentRequest.cannot.be.null");
        }
        if (bean.getFreeProcessed() == null) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.freeProcessed.cannot.be.null");
        }
        if (bean.getLanguage() == null) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.language.cannot.be.null");
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
    final public void setRequestDate(DateTime requestDate) {
        super.setRequestDate(requestDate);
    }

    @Override
    public boolean isUrgentRequest() {
        return getUrgentRequest().booleanValue();
    }

    public boolean isFreeProcessed() {
        return getFreeProcessed();
    }

    public boolean isFree() {
        return !isPayable() || isFreeProcessed();
    }

    final protected boolean isPayable() {
        return ServiceRequestType.findUnique(this) != null && ServiceRequestType.findUnique(this).isPayable();
    }

    protected boolean isPayed() {
        if (!isPayable()) {
            return true;
        }

        final IAcademicTreasuryEvent event =
                TreasuryBridgeAPIFactory.implementation().academicTreasuryEventForAcademicServiceRequest(this);

        return event != null && event.isPayed();
    }

    final public boolean getIsPayed() {
        return isPayed();
    }

    abstract public boolean isPayedUponCreation();

    public boolean isPaymentsAccessible() {
        final AcademicProgram program = getAcademicProgram();
        Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_STUDENT_PAYMENTS, Authenticate.getUser())
                .collect(Collectors.toSet());
        programs.addAll(AcademicPermissionService.getDegrees("TREASURY", Authenticate.getUser()));
        return programs.contains(getAcademicProgram())
                && TreasuryBridgeAPIFactory.implementation().academicTreasuryEventForAcademicServiceRequest(this) != null;
    }

    /**
     * Return the URL for debt account of this student
     *
     */
    public String getPaymentURL() {
        final IAcademicServiceRequestAndAcademicTaxTreasuryEvent event =
                TreasuryBridgeAPIFactory.implementation().academicTreasuryEventForAcademicServiceRequest(this);
        return event != null ? event.getDebtAccountURL() : null;
    }

    public boolean isRegistrationAccessible() {
        final AcademicProgram program = getAcademicProgram();
        Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_REGISTRATIONS, Authenticate.getUser())
                .collect(Collectors.toSet());
        programs.addAll(AcademicPermissionService.getDegrees("ACADEMIC_OFFICE_REGISTRATION_ACCESS", Authenticate.getUser()));
        return programs.stream().anyMatch(p -> p == program);
    }

    protected String getDescription(final AcademicServiceRequestType academicServiceRequestType,
            final String specificServiceType) {
        final StringBuilder result = new StringBuilder();

        if (getServiceRequestType() != null && !getServiceRequestType().isLegacy()) {
            return getServiceRequestType().getName().getContent();
        }

        result.append(BundleUtil.getString(Bundle.ENUMERATION, academicServiceRequestType.getQualifiedName()));
        if (specificServiceType != null) {
            result.append(": ");
            result.append(BundleUtil.getString(Bundle.ENUMERATION, specificServiceType));
        }
        return result.toString();
    }

    protected String getDescription(final AcademicServiceRequestType academicServiceRequestType) {
        return getDescription(academicServiceRequestType, null);
    }

    @Override
    public String getDescription() {
        return getDescription(getAcademicServiceRequestType());
    }

    @Atomic
    final public void process() throws DomainException {
        process(AccessControl.getPerson());

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    final public void process(final Person responsible) throws DomainException {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.PROCESSING, responsible));
    }

    final public void process(final Person responsible, final YearMonthDay situationDate) throws DomainException {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.PROCESSING, responsible, situationDate, ""));
    }

    final public void process(final YearMonthDay situationDate) throws DomainException {
        process(AccessControl.getPerson(), situationDate);
    }

    @Atomic
    public void sendToExternalEntity(final YearMonthDay sendDate, final String description) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY,
                AccessControl.getPerson(), sendDate, description));
    }

    @Atomic
    final public void receivedFromExternalEntity(final YearMonthDay receivedDate, final String description) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY,
                AccessControl.getPerson(), receivedDate, description));

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    @Atomic
    final public void reject(final String justification) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.REJECTED, AccessControl.getPerson(),
                justification));

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_REJECT_OR_CANCEL_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    @Atomic
    final public void cancel(final String justification) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CANCELLED, AccessControl.getPerson(),
                justification));

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_REJECT_OR_CANCEL_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    final public void concludeServiceRequest() {
        conclude(AccessControl.getPerson());
    }

    @Deprecated
    /**
     * There are many conclude methods. It should be simplified
     */
    final public void conclude() {
        conclude(AccessControl.getPerson());
    }

    @Deprecated
    /**
     * There are many conclude methods. It should be simplified
     */
    final public void conclude(final Person responsible) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CONCLUDED, responsible));
    }

    @Deprecated
    /**
     * There are many conclude methods. It should be simplified
     */
    final public void conclude(final YearMonthDay situationDate, final String justification) {
        conclude(AccessControl.getPerson(), situationDate, justification);

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    @Deprecated
    /**
     * There are many conclude methods. It should be simplified
     */
    final public void conclude(final Person responsible, final YearMonthDay situationDate) {
        conclude(responsible, situationDate, "");
    }

    @Deprecated
    /**
     * There are many conclude methods. It should be simplified
     */
    final public void conclude(final Person responsible, final YearMonthDay situationDate, final String justification) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CONCLUDED, responsible, situationDate,
                justification));
    }

    @Atomic
    @Deprecated
    /**
     * There are many conclude methods. It should be simplified
     */
    final public void conclude(final YearMonthDay situationDate, final String justification, boolean sendEmail) {
        conclude(AccessControl.getPerson(), situationDate, justification);
        if (sendEmail) {
            sendConcludeEmail();
        }

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    private void sendConcludeEmail() {
        String body = BundleUtil.getString(Bundle.APPLICATION, "mail.academicServiceRequest.concluded.message1");
        body += " " + getServiceRequestNumberYear();
        body += " " + BundleUtil.getString(Bundle.APPLICATION, "mail.academicServiceRequest.concluded.message2");
        body += " '" + getDescription();
        body += "' " + BundleUtil.getString(Bundle.APPLICATION, "mail.academicServiceRequest.concluded.message3");

        if (getAcademicServiceRequestType() == AcademicServiceRequestType.SPECIAL_SEASON_REQUEST) {
            body += "\n" + BundleUtil.getString(Bundle.APPLICATION, "mail.academicServiceRequest.concluded.messageSSR4B");
            body += "\n\n" + BundleUtil.getString(Bundle.APPLICATION, "mail.academicServiceRequest.concluded.messageSSR5");
        } else {

            body += "\n\n" + BundleUtil.getString(Bundle.APPLICATION, "mail.academicServiceRequest.concluded.message4");

        }

        final Sender sender = getAdministrativeOffice().getUnit().getUnitBasedSenderSet().iterator().next();
        final Recipient recipient = new Recipient(getPerson().getUser().groupOf());
        new Message(sender, sender.getReplyTosSet(), recipient.asCollection(), getDescription(), body, "");
    }

    @Atomic
    final public void delivered() {
        delivered(AccessControl.getPerson());

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    final private void delivered(final Person responsible) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.DELIVERED, responsible));
    }

    final public void delivered(final Person responsible, final YearMonthDay situationDate) {
        edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.DELIVERED, responsible, situationDate, ""));

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(this));
    }

    final public void delete() {
        checkRulesToDelete();
        disconnect();
        super.deleteDomainObject();
    }

    protected void disconnect() {
        for (; !getAcademicServiceRequestSituationsSet().isEmpty(); getAcademicServiceRequestSituationsSet().iterator().next()
                .delete()) {
            ;
        }
        super.setAdministrativeOffice(null);
        super.setExecutionYear(null);
        super.setRootDomainObject(null);
        super.setAcademicServiceRequestYear(null);
    }

    protected void checkRulesToDelete() {
    }

    public void cloneAttributes(AcademicServiceRequest original) {
        setRootDomainObject(original.getRootDomainObject());
        super.setAdministrativeOffice(original.getAdministrativeOffice());
        setAcademicServiceRequestYear(original.getAcademicServiceRequestYear());
        super.setServiceRequestNumber(original.getServiceRequestNumber());
        setRequestDate(original.getRequestDate());
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        throw new DomainException("error.serviceRequests.RegistrationAcademicServiceRequest.cannot.modify.administrativeOffice");
    }

    @Override
    final public void setServiceRequestNumber(Integer serviceRequestNumber) {
        super.setServiceRequestNumber(serviceRequestNumber);

        checkRules();
    }

    final public String getServiceRequestNumberYear() {
        return getServiceRequestNumber() + SERVICE_REQUEST_NUMBER_YEAR_SEPARATOR + getServiceRequestYear();
    }

    @Override
    final public void addAcademicServiceRequestSituations(AcademicServiceRequestSituation academicServiceRequestSituation) {
        throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.add.academicServiceRequestSituation");
    }

    @Override
    public Set<AcademicServiceRequestSituation> getAcademicServiceRequestSituationsSet() {
        return Collections.unmodifiableSet(super.getAcademicServiceRequestSituationsSet());
    }

    @Override
    final public void removeAcademicServiceRequestSituations(AcademicServiceRequestSituation academicServiceRequestSituation) {
        throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.remove.academicServiceRequestSituation");
    }

    public AcademicServiceRequestSituation getActiveSituation() {
        return !getAcademicServiceRequestSituationsSet().isEmpty() ? Collections.min(getAcademicServiceRequestSituationsSet(),
                AcademicServiceRequestSituation.COMPARATOR_BY_MOST_RECENT_SITUATION_DATE_AND_ID) : null;
    }

    final public DateTime getActiveSituationDate() {
        return getActiveSituation().getSituationDate();
    }

    final public AcademicServiceRequestSituation getConclusionSituation() {
        return getSituationByType(AcademicServiceRequestSituationType.CONCLUDED);
    }

    public AcademicServiceRequestSituation getSituationByType(final AcademicServiceRequestSituationType type) {
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

    protected void checkRulesToChangeState(final AcademicServiceRequestSituationType situationType) {

        if (!isAcceptedSituationType(situationType)) {
            String sourceState = getActiveSituation().getAcademicServiceRequestSituationType().getLocalizedName();
            String targetState = situationType.getLocalizedName();

            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequest.cannot.change.from.source.state.to.target.state", sourceState,
                    targetState);
        }
    }

    final protected boolean isAcceptedSituationType(final AcademicServiceRequestSituationType situationType) {
        return getAcceptedSituationTypes(getAcademicServiceRequestSituationType()).contains(situationType);
    }

    final public boolean isCancelledSituationAccepted() {
        return isAcceptedSituationType(AcademicServiceRequestSituationType.CANCELLED);
    }

    final public boolean isRejectedSituationAccepted() {
        return isAcceptedSituationType(AcademicServiceRequestSituationType.REJECTED);
    }

    final public boolean isProcessingSituationAccepted() {
        return isAcceptedSituationType(AcademicServiceRequestSituationType.PROCESSING);
    }

    final public boolean isSendToExternalEntitySituationAccepted() {
        return isAcceptedSituationType(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY);
    }

    final public boolean isReceivedSituationAccepted() {
        return isAcceptedSituationType(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY);
    }

    final public boolean isConcludedSituationAccepted() {
        return isAcceptedSituationType(AcademicServiceRequestSituationType.CONCLUDED);
    }

    final public boolean isDeliveredSituationAccepted() {
        return isAcceptedSituationType(AcademicServiceRequestSituationType.DELIVERED);
    }

    public boolean isDownloadPossible() {
        return false;
    }

    final public boolean isRePrintPossible() {
        return finishedSuccessfully() && isToPrint();
    }

    private List<AcademicServiceRequestSituationType> getAcceptedSituationTypes(
            AcademicServiceRequestSituationType situationType) {

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
            return Collections.emptyList();
        }
    }

    protected List<AcademicServiceRequestSituationType> getNewSituationAcceptedSituationsTypes() {
        return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
                AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.PROCESSING));
    }

    protected List<AcademicServiceRequestSituationType> getProcessingSituationAcceptedSituationsTypes() {
        if (isPossibleToSendToOtherEntity()) {
            return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
                    AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY));
        } else {
            return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
                    AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.CONCLUDED));
        }
    }

    protected List<AcademicServiceRequestSituationType> getSentToExternalEntitySituationAcceptedSituationsTypes() {
        return Collections
                .unmodifiableList(Collections.singletonList(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY));
    }

    protected List<AcademicServiceRequestSituationType> getReceivedFromExternalEntitySituationAcceptedSituationsTypes() {
        return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
                AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.CONCLUDED));
    }

    protected List<AcademicServiceRequestSituationType> getConcludedSituationAcceptedSituationsTypes() {
        return Collections.unmodifiableList(
                Arrays.asList(AcademicServiceRequestSituationType.CANCELLED, AcademicServiceRequestSituationType.DELIVERED));
    }

    /** This method is overwritten in the subclasses */
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {
        verifyIsToProcessAndHasPersonalInfo(academicServiceRequestBean);

        verifyIsToDeliveredAndIsPayed(academicServiceRequestBean);

        if (!academicServiceRequestBean.isToCancelOrReject()) {
            assertPayedEvents();
        }
    }

    protected void assertPayedEvents() {
        if (TreasuryBridgeAPIFactory.implementation().isAcademicalActsBlocked(getPerson(), new LocalDate())) {
            throw new DomainException("DocumentRequest.student.has.not.payed.debts");
        }
    }

    protected void verifyIsToDeliveredAndIsPayed(final AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToDeliver()) {
        }
    }

    protected void verifyIsToProcessAndHasPersonalInfo(final AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToProcess() && hasPersonalInfo() && hasMissingPersonalInfo()) {
            throw new DomainException("AcademicServiceRequest.has.missing.personal.info");
        }
    }

    protected void createAcademicServiceRequestSituations(final AcademicServiceRequestBean academicServiceRequestBean) {
        AcademicServiceRequestSituation.create(this, academicServiceRequestBean);
    }

    public boolean isPiggyBackedOnRegistry() {
        return false;
    }

    final public boolean isNewRequest() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.NEW;
    }

    final public boolean isProcessing() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.PROCESSING;
    }

    final public boolean hasProcessed() {
        return getSituationByType(AcademicServiceRequestSituationType.PROCESSING) != null;
    }

    final public boolean isSentToExternalEntity() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY;
    }

    final public boolean isReceivedFromExternalEntity() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY;
    }

    final public boolean isConcluded() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CONCLUDED;
    }

    final public boolean hasConcluded() {
        return getSituationByType(AcademicServiceRequestSituationType.CONCLUDED) != null;
    }

    final public boolean isDelivered() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.DELIVERED;
    }

    final public boolean isRejected() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.REJECTED;
    }

    final public boolean isCancelled() {
        return getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.CANCELLED;
    }

    final public boolean isHistorical() {
        return isDelivered() || isRejected() || isCancelled();
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
        return false;
    }

    /**
     * Indicates if is possible to print document again
     */
    abstract public boolean isToPrint();

    /**
     * Indicates if is possible to AdministrativeOffice send this request to another
     * entity
     */
    abstract public boolean isPossibleToSendToOtherEntity();

    /**
     * Indicates that the document external shipping to rectorate is done using the
     * rectorate batches. The
     * {@link AcademicServiceRequestSituationType#SENT_TO_EXTERNAL_ENTITY} and
     * {@link AcademicServiceRequestSituationType#RECEIVED_FROM_EXTERNAL_ENTITY}
     * states are handled through this system.
     *
     * @return true if managed by batch, false otherwise.
     */
    abstract public boolean isManagedWithRectorateSubmissionBatch();

    /**
     * Special condition for pre-existing documents that are able to consume a
     * registry number.
     */
    public boolean isCanGenerateRegistryCode() {
        return false;
    }

    final public boolean createdByStudent() {
        return getSituationByType(AcademicServiceRequestSituationType.NEW).getCreator() == null;
    }

    /**
     * See if this can be avoided.
     */
    @Deprecated
    final public boolean getLoggedPersonCanCancel() {
        return isCancelledSituationAccepted() && (createdByStudent() && !isConcluded()
                || AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.SERVICE_REQUESTS,
                        this.getAcademicProgram(), Authenticate.getUser())
                || AcademicPermissionService.hasAccess("ACADEMIC_REQUISITIONS", (Degree) this.getAcademicProgram(),
                        Authenticate.getUser()));
    }

    final public DateTime getCreationDate() {
        return getSituationByType(AcademicServiceRequestSituationType.NEW).getCreationDate();
    }

    final public DateTime getProcessingDate() {
        AcademicServiceRequestSituation state = getSituationByType(AcademicServiceRequestSituationType.PROCESSING);
        return state != null ? state.getCreationDate() : null;
    }

    final public DateTime getRequestConclusionDate() {
        AcademicServiceRequestSituation conclusionSituation = getConclusionSituation();
        return conclusionSituation != null ? conclusionSituation.getCreationDate() : null;
    }

    public boolean isRequestForPerson() {
        return false;
    }

    @Override
    public boolean isRequestForPhd() {
        return false;
    }

    @Override
    public boolean isRequestForRegistration() {
        return false;
    }

    public boolean isFor(final ExecutionYear executionYear) {
        return getExecutionYear() != null && getExecutionYear().equals(executionYear);
    }

    @Override
    abstract public Person getPerson();

    abstract public AcademicServiceRequestType getAcademicServiceRequestType();

    /**
     * Indicates if the service result needs personal info
     */
    abstract public boolean hasPersonalInfo();

    protected boolean hasMissingPersonalInfo() {
        return Strings.isNullOrEmpty(getPerson().getName()) || getPerson().getDateOfBirthYearMonthDay() == null
                || Strings.isNullOrEmpty(getPerson().getDocumentIdNumber()) || getPerson().getIdDocumentType() == null;
    }

    public void revertToProcessingState() {
        throw new DomainException("error.serviceRequests.AcademicServiceRequest.cannot.revert.to.processing.state");
    }

    protected void internalRevertToProcessingState() {
        AcademicServiceRequestSituation activeSituation = getActiveSituation();

        if (activeSituation == null || activeSituation.isProcessing() || activeSituation.isNew()) {
            throw new DomainException(
                    "error.serviceRequests.AcademicServiceRequest.revert.to.processing.in.only.possibile.from.later.states");
        } else if (activeSituation.isCancelled()) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.cancelled.requests.cannot.be.reverted");
        }

        if (getAcademicServiceRequestSituationsSet().size() <= 1) {
            throw new DomainException("error.serviceRequests.AcademicServiceRequest.revert.is.requires.more.than.one.state");
        }

        while (getAcademicServiceRequestSituationsSet().size() > 1 && !activeSituation.isProcessing()) {
            activeSituation.delete(false);
            activeSituation = getActiveSituation();
        }

    }

    public boolean isDiploma() {
        return false;
    }

    public boolean isPastDiploma() {
        return false;
    }

    public boolean isRegistryDiploma() {
        return false;
    }

    public boolean isDiplomaSupplement() {
        return false;
    }

    public boolean isBatchSet() {
        return false; // getRectorateSubmissionBatch() != null;
    }

    @Override
    public abstract AcademicProgram getAcademicProgram();

    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

    @Override
    public boolean isRequestedWithCycle() {
        return getRequestedCycle() != null;
    }

    public static Set<AcademicServiceRequest> getAcademicServiceRequests(Person person, Integer year,
            AcademicServiceRequestSituationType situation, Interval interval) {
        Set<AcademicServiceRequest> serviceRequests = new HashSet<AcademicServiceRequest>();
        Set<AcademicProgram> programs =
                AcademicAccessRule.getProgramsAccessibleToFunction(AcademicOperationType.SERVICE_REQUESTS, person.getUser())
                        .collect(Collectors.toSet());
        programs.addAll(AcademicPermissionService.getDegrees("ACADEMIC_REQUISITIONS", person.getUser()));
        Collection<AcademicServiceRequest> possible = null;
        if (year != null) {
            possible = AcademicServiceRequestYear.getAcademicServiceRequests(year);
        } else {
            possible = Bennu.getInstance().getAcademicServiceRequestsSet();
        }
        for (AcademicServiceRequest request : possible) {
            if (!programs.contains(request.getAcademicProgram())) {
                continue;
            }
            if (situation != null && !request.getAcademicServiceRequestSituationType().equals(situation)) {
                continue;
            }
            if (interval != null && !interval.contains(request.getActiveSituationDate())) {
                continue;
            }
            serviceRequests.add(request);
        }
        return serviceRequests;
    }

    @Override
    public boolean isDetailed() {
        return getDetailed();
    }

}
