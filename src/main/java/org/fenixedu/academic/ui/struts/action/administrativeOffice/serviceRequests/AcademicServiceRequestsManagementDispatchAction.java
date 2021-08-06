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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.serviceRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituation;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.IDeferableRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistrationAcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.factoryExecutors.RegistrationAcademicServiceRequestCreator;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.serviceRequests.SendAcademicServiceRequestToExternalEntity;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminServicesApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.CollectionPager;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;

@StrutsFunctionality(app = AcademicAdminServicesApp.class, path = "service-requests",
        titleKey = "label.academic.service.requests", accessGroup = "academic(SERVICE_REQUESTS)")
@Mapping(path = "/academicServiceRequestsManagement", module = "academicAdministration",
        formBeanClass = AcademicServiceRequestsManagementDispatchAction.AcademicServiceRequestsManagementForm.class)
@Forwards({
        @Forward(name = "viewRegistrationAcademicServiceRequestsHistoric",
                path = "/academicAdminOffice/serviceRequests/viewRegistrationAcademicServiceRequestsHistoric.jsp"),
        @Forward(name = "viewAcademicServiceRequest",
                path = "/academicAdminOffice/serviceRequests/viewAcademicServiceRequest.jsp"),
        @Forward(name = "viewRegistrationDetails", path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp"),
        @Forward(name = "confirmCreateServiceRequest",
                path = "/academicAdminOffice/serviceRequests/confirmCreateServiceRequest.jsp"),
        @Forward(name = "prepareRejectAcademicServiceRequest",
                path = "/academicAdminOffice/serviceRequests/prepareRejectAcademicServiceRequest.jsp"),
        @Forward(name = "prepareSendAcademicServiceRequest",
                path = "/academicAdminOffice/serviceRequests/prepareSendAcademicServiceRequest.jsp"),
        @Forward(name = "prepareReceiveAcademicServiceRequest",
                path = "/academicAdminOffice/serviceRequests/prepareReceiveAcademicServiceRequest.jsp"),
        @Forward(name = "prepareCancelAcademicServiceRequest",
                path = "/academicAdminOffice/serviceRequests/prepareCancelAcademicServiceRequest.jsp"),
        @Forward(name = "prepareConcludeDocumentRequest",
                path = "/academicAdministration/documentRequestsManagement.do?method=prepareConcludeDocumentRequest"),
        @Forward(name = "prepareConcludeServiceRequest", path = "/academicAdminOffice/serviceRequests/concludeServiceRequest.jsp"),
        @Forward(name = "prepareCreateServiceRequest",
                path = "/academicAdminOffice/serviceRequests/prepareCreateServiceRequest.jsp"),
        @Forward(name = "searchResults", path = "/academicAdminOffice/serviceRequests/searchResults.jsp"),
        @Forward(name = "showCurrentBag", path = "/academicAdminOffice/serviceRequests/showCurrentBag.jsp"),
        @Forward(name = "entryPoint", path = "/academicAdminOffice/serviceRequests/entryPoint.jsp") })
public class AcademicServiceRequestsManagementDispatchAction extends FenixDispatchAction {

    private static final int REQUESTS_PER_PAGE = 50;
    private static final String REQUEST_NUMBER_YEAR = "serviceRequestNumberYear";
    private static final String REGISTRATION_NUMBER = "registration.number";
    private static final String DESCRIPTION = "description";
    private static final String EXECUTION_YEAR = "executionYear";
    private static final String URGENT_REQUEST = "urgentRequest";
    private static final String REQUEST_DATE = "requestDate";
    private static final String ACTIVE_SITUATION_DATE = "activeSituationDate";
    private static final String DEFAULT_ORDER_GETTER = ACTIVE_SITUATION_DATE;
    private static final String ORDER_PARAMETER = "sortBy";
    private static final String ORDER_MARKER = "=";
    public static final String[] ASC_ORDER_DIR = { "ascending", "asc" };
    public static final String DEFAULT_ORDER_DIR = "desc";

    public static class AcademicServiceRequestsManagementForm extends ActionForm {

        private static final long serialVersionUID = 1L;

        private String justification;
        private Integer numberOfPages;
        private Boolean sendEmailToStudent;
        private Boolean deferRequest;

        public String getJustification() {
            return justification;
        }

        public void setJustification(String justification) {
            this.justification = justification;
        }

        public Integer getNumberOfPages() {
            return numberOfPages;
        }

        public void setNumberOfPages(Integer numberOfPages) {
            this.numberOfPages = numberOfPages;
        }

        public Boolean getSendEmailToStudent() {
            return sendEmailToStudent;
        }

        public void setSendEmailToStudent(Boolean sendEmailToStudent) {
            this.sendEmailToStudent = sendEmailToStudent;
        }

        public Boolean getDeferRequest() {
            return deferRequest;
        }

        public void setDeferRequest(Boolean deferRequest) {
            this.deferRequest = deferRequest;
        }

    }

    @EntryPoint
    public ActionForward entryPoint(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("entryPoint");
    }

    private RegistrationAcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
        final AcademicServiceRequest academicServiceRequest = getDomainObject(request, "academicServiceRequestId");
        request.setAttribute("academicServiceRequest", academicServiceRequest);
        return (RegistrationAcademicServiceRequest) academicServiceRequest;
    }

    private Registration getAndSetRegistration(final HttpServletRequest request) {
        final Registration registration = getDomainObject(request, "registrationID");
        request.setAttribute("registration", registration);
        return registration;
    }

    private String getAndSetUrl(ActionForm actionForm, HttpServletRequest request) {
        final StringBuilder result = new StringBuilder();

        if (!StringUtils.isEmpty(request.getParameter("backAction"))) {
            result.append("/").append(request.getParameter("backAction")).append(".do?");

            if (!StringUtils.isEmpty(request.getParameter("backMethod"))) {
                result.append("method=").append(request.getParameter("backMethod"));
            }
        }

        request.setAttribute("url", result.toString());
        return result.toString();
    }

    public ActionForward viewRegistrationAcademicServiceRequestsHistoric(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("registration", getAndSetRegistration(request));
        return mapping.findForward("viewRegistrationAcademicServiceRequestsHistoric");
    }

    public ActionForward viewAcademicServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final AcademicServiceRequest serviceRequest = getAndSetAcademicServiceRequest(request);
        getAndSetUrl(form, request);

        request.setAttribute("canRevertToProcessingState", canRevertToProcessingState(serviceRequest));
        request.setAttribute("serviceRequestSituations", getAcademicServiceRequestSituations(serviceRequest));

        return mapping.findForward("viewAcademicServiceRequest");
    }

    private boolean canRevertToProcessingState(final AcademicServiceRequest academicServiceRequest) {
        return AcademicPredicates.SERVICE_REQUESTS_REVERT_TO_PROCESSING_STATE.evaluate(academicServiceRequest)
                && !academicServiceRequest.isPossibleToSendToOtherEntity();
    }

    private List<AcademicServiceRequestSituation> getAcademicServiceRequestSituations(AcademicServiceRequest serviceRequest) {
        final List<AcademicServiceRequestSituation> result =
                new ArrayList<AcademicServiceRequestSituation>(serviceRequest.getAcademicServiceRequestSituationsSet());
        Collections.sort(result, AcademicServiceRequestSituation.COMPARATOR_BY_MOST_RECENT_SITUATION_DATE_AND_ID);
        return result;
    }

    @Atomic
    public ActionForward revertRequestToProcessingState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        getAndSetUrl(form, request);
        request.setAttribute("canRevertToProcessingState", canRevertToProcessingState(academicServiceRequest));

        try {
            academicServiceRequest.revertToProcessingState();
        } catch (DomainException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
        }

        return mapping.findForward("viewAcademicServiceRequest");
    }

    public ActionForward processNewAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

        try {
            academicServiceRequest.process();
            addActionMessage(request, "academic.service.request.processed.with.success");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            request.setAttribute("failingCondition", ex.getLocalizedMessage());
            return mapping.findForward("prepareRejectAcademicServiceRequest");
        }

        if (academicServiceRequest.isDocumentRequest()
                && ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
            return prepareConcludeAcademicServiceRequest(mapping, actionForm, request, response);
        } else if (request.getParameter("academicSituationType") != null) {
            return search(mapping, actionForm, request, response);
        } else {
            request.setAttribute("registration", academicServiceRequest.getRegistration());
            return mapping.findForward("viewRegistrationDetails");
        }
    }

    public ActionForward prepareSendAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean(getAndSetAcademicServiceRequest(request),
                AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY));
        return mapping.findForward("prepareSendAcademicServiceRequest");
    }

    public ActionForward sendAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final RegistrationAcademicServiceRequest serviceRequest = getAndSetAcademicServiceRequest(request);
        final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");

        try {
            SendAcademicServiceRequestToExternalEntity.run(serviceRequest, requestBean.getSituationDate(),
                    requestBean.getJustification());

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            request.setAttribute("serviceRequestBean", requestBean);
            return mapping.findForward("prepareSendAcademicServiceRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            request.setAttribute("serviceRequestBean", requestBean);
            return mapping.findForward("prepareSendAcademicServiceRequest");
        }

        request.setAttribute("registration", serviceRequest.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareReceiveAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean(getAndSetAcademicServiceRequest(request),
                AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY));
        return mapping.findForward("prepareReceiveAcademicServiceRequest");
    }

    public ActionForward receiveAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final RegistrationAcademicServiceRequest serviceRequest = getAndSetAcademicServiceRequest(request);
        final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");

        try {
            serviceRequest.receivedFromExternalEntity(requestBean.getSituationDate(), requestBean.getJustification());

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            request.setAttribute("serviceRequestBean", requestBean);
            return mapping.findForward("prepareReceiveAcademicServiceRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            request.setAttribute("serviceRequestBean", requestBean);
            return mapping.findForward("prepareReceiveAcademicServiceRequest");
        }

        request.setAttribute("registration", serviceRequest.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareRejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        getAndSetAcademicServiceRequest(request);
        return mapping.findForward("prepareRejectAcademicServiceRequest");
    }

    public ActionForward rejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        final String justification = ((AcademicServiceRequestsManagementForm) actionForm).getJustification();

        try {
            academicServiceRequest.reject(justification);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return mapping.findForward("prepareRejectAcademicServiceRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            return mapping.findForward("prepareRejectAcademicServiceRequest");
        }

        request.setAttribute("registration", academicServiceRequest.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareCancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        getAndSetAcademicServiceRequest(request);
        return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        final String justification = ((AcademicServiceRequestsManagementForm) actionForm).getJustification();

        try {
            academicServiceRequest.cancel(justification);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        }

        request.setAttribute("registration", academicServiceRequest.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareConcludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        AcademicServiceRequestsManagementForm form = (AcademicServiceRequestsManagementForm) actionForm;
        form.setSendEmailToStudent(Boolean.TRUE);

        if (academicServiceRequest.isDocumentRequest()) {
            request.setAttribute("serviceRequestBean", new AcademicServiceRequestBean(academicServiceRequest,
                    AcademicServiceRequestSituationType.CONCLUDED));
            return mapping.findForward("prepareConcludeDocumentRequest");
        } else {
            return mapping.findForward("prepareConcludeServiceRequest");
        }
    }

    public ActionForward concludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Boolean sendEmail = true;
        RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

        final AcademicServiceRequestsManagementForm form = (AcademicServiceRequestsManagementForm) actionForm;
        if (academicServiceRequest instanceof IDeferableRequest) {
            if (form.getDeferRequest() == null) {
                return prepareConcludeAcademicServiceRequest(mapping, actionForm, request, response);
            }
            final IDeferableRequest deferableRequest = (IDeferableRequest) academicServiceRequest;
            deferableRequest.setDeferment(form.getDeferRequest());
        }

        if (academicServiceRequest.getRegistration().getRegistrationProtocol().isMobilityAgreement()) {
            sendEmail = false;
        }

        if (academicServiceRequest.getAcademicServiceRequestType() == AcademicServiceRequestType.DIPLOMA_SUPPLEMENT_REQUEST) {
            sendEmail = false;
        }

        try {
            academicServiceRequest.conclude(getSituationDate(), getJustification(),
                    form.getSendEmailToStudent() != null ? form.getSendEmailToStudent() : sendEmail);
            addActionMessage(request, "academic.service.request.concluded.with.success");

            if (academicServiceRequest.isDocumentRequest()
                    && ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
                return deliveredAcademicServiceRequest(mapping, actionForm, request, response);
            }
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
        }

        request.setAttribute("registration", academicServiceRequest.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

    private YearMonthDay getSituationDate() {
        final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");
        return requestBean == null ? new YearMonthDay() : requestBean.getSituationDate();
    }

    private String getJustification() {
        final AcademicServiceRequestBean requestBean = (AcademicServiceRequestBean) getObjectFromViewState("serviceRequestBean");
        return requestBean == null ? null : requestBean.getJustification();
    }

    public ActionForward deliveredAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

        try {
            academicServiceRequest.delivered();
            addActionMessage(request, "academic.service.request.delivered.with.success");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
        }

        request.setAttribute("registration", academicServiceRequest.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

    @Atomic
    public ActionForward generateRegistryCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final RegistrationAcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        DiplomaRequest diploma = (DiplomaRequest) academicServiceRequest;
        diploma.generateRegistryCode();
        addActionMessage(request, "rectorate.code.generated.with.success");
        request.setAttribute("registration", academicServiceRequest.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final AcademicServiceRequestBean bean = getOrCreateAcademicServiceRequestBean(request);
        request.setAttribute("bean", bean);

        final Collection<AcademicServiceRequest> remainingRequests = bean.searchAcademicServiceRequests();
        final Collection<AcademicServiceRequest> specificRequests = getAndRemoveSpecificRequests(bean, remainingRequests);

        final SortedSet<AcademicServiceRequest> sorted = new TreeSet<AcademicServiceRequest>(getComparator(request));
        sorted.addAll(remainingRequests);
        request.setAttribute("remainingRequests", remainingRequests);
        request.setAttribute("specificRequests", specificRequests);

        final CollectionPager<AcademicServiceRequest> pager =
                new CollectionPager<AcademicServiceRequest>(sorted, REQUESTS_PER_PAGE);
        request.setAttribute("collectionPager", pager);
        request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
        request.setAttribute("pageNumber", page);
        request.setAttribute("resultPage", pager.getPage(page));

        return mapping.findForward("searchResults");
    }

    private AcademicServiceRequestBean getOrCreateAcademicServiceRequestBean(HttpServletRequest request) {
        AcademicServiceRequestBean bean = (AcademicServiceRequestBean) getObjectFromViewState("bean");
        if (bean == null) {
            Integer year = getIntegerFromRequest(request, "serviceRequestYear");
            if (year == null) {
                year = new YearMonthDay().getYear();
            }

            bean =
                    new AcademicServiceRequestBean(AcademicServiceRequestSituationType.valueOf(request
                            .getParameter("academicSituationType")), AccessControl.getPerson(), year);
        }
        return bean;
    }

    private Comparator getComparator(HttpServletRequest request) {
        final String orderParameter = request.getParameter(ORDER_PARAMETER);
        final String orderGetter =
                StringUtils.isEmpty(orderParameter) ? DEFAULT_ORDER_GETTER : orderParameter.substring(0,
                        orderParameter.indexOf(ORDER_MARKER));

        final String orderDir =
                StringUtils.isEmpty(orderParameter) ? DEFAULT_ORDER_DIR : orderParameter.substring(
                        orderParameter.indexOf(ORDER_MARKER) + 1, orderParameter.length());
        final boolean orderAsc = Arrays.asList(ASC_ORDER_DIR).contains(orderDir);

        if (orderGetter.equals(REQUEST_NUMBER_YEAR)) {
            return orderAsc ? AcademicServiceRequest.COMPARATOR_BY_NUMBER : new ReverseComparator(
                    AcademicServiceRequest.COMPARATOR_BY_NUMBER);
        } else if (orderGetter.equals(EXECUTION_YEAR)) {
            return orderAsc ? AcademicServiceRequest.EXECUTION_YEAR_AND_OID_COMPARATOR : new ReverseComparator(
                    AcademicServiceRequest.EXECUTION_YEAR_AND_OID_COMPARATOR);
        } else if (orderGetter.equals(REGISTRATION_NUMBER) || orderGetter.equals(DESCRIPTION)
                || orderGetter.equals(URGENT_REQUEST) || orderGetter.equals(REGISTRATION_NUMBER)
                || orderGetter.equals(REQUEST_DATE) || orderGetter.equals(ACTIVE_SITUATION_DATE)) {
            final ComparatorChain chain = new ComparatorChain();
            chain.addComparator(orderAsc ? new BeanComparator(orderGetter) : new ReverseComparator(
                    new BeanComparator(orderGetter)));
            chain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
            return chain;
        }

        return null;
    }

    private Collection<AcademicServiceRequest> getAndRemoveSpecificRequests(final AcademicServiceRequestBean bean,
            final Collection<AcademicServiceRequest> remainingRequests) {
        final Collection<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

        for (Iterator<AcademicServiceRequest> iter = remainingRequests.iterator(); iter.hasNext();) {
            final AcademicServiceRequest academicServiceRequest = iter.next();
            if (bean.getAcademicServiceRequestSituationType() == AcademicServiceRequestSituationType.NEW) {
                if (academicServiceRequest.getActiveSituation().getCreator() == null
                        || academicServiceRequest.getActiveSituation().getCreator().equals(academicServiceRequest.getPerson())) {
                    iter.remove();
                    result.add(academicServiceRequest);
                }
            } else {
                if (AccessControl.getPerson().equals(academicServiceRequest.getActiveSituation().getCreator())) {
                    iter.remove();
                    result.add(academicServiceRequest);
                }
            }
        }

        return result;
    }

    public ActionForward chooseServiceRequestType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("academicServiceRequestCreateBean", new RegistrationAcademicServiceRequestCreator(
                getAndSetRegistration(request)));
        return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward chooseServiceRequestTypePostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward chooseServiceRequestTypeInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
        return mapping.findForward("prepareCreateServiceRequest");
    }

    public ActionForward backToViewRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        getAndSetRegistration(request);
        return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward confirmCreateServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("academicServiceRequestCreateBean", getRenderedObject("academicServiceRequestCreateBean"));
        return mapping.findForward("confirmCreateServiceRequest");
    }

    public ActionForward createServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            executeFactoryMethod();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return confirmCreateServiceRequest(mapping, actionForm, request, response);
        }

        final RegistrationAcademicServiceRequestCreator bean = getRenderedObject("academicServiceRequestCreateBean");
        request.setAttribute("registration", bean.getRegistration());
        return mapping.findForward("viewRegistrationDetails");
    }
}