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
package org.fenixedu.academic.ui.struts.action.candidate.degree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.installments.InstallmentForFirstTimeStudents;
import org.fenixedu.academic.domain.accounting.paymentCodes.InstallmentPaymentCode;
import org.fenixedu.academic.domain.candidacy.CandidacyOperationType;
import org.fenixedu.academic.domain.candidacy.CandidacySummaryFile;
import org.fenixedu.academic.domain.candidacy.FirstTimeCandidacyStage;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.candidacy.workflow.CandidacyOperation;
import org.fenixedu.academic.domain.candidacy.workflow.form.ResidenceInformationForm;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.workflow.Form;
import org.fenixedu.academic.domain.util.workflow.Operation;
import org.fenixedu.academic.service.services.candidacy.ExecuteStateOperation;
import org.fenixedu.academic.service.services.candidacy.LogFirstTimeCandidacyTimestamp;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.candidate.ViewCandidaciesDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

@Mapping(path = "/degreeCandidacyManagement", module = "candidate", functionality = ViewCandidaciesDispatchAction.class)
@Forwards({ @Forward(name = "showWelcome", path = "/candidate/degree/showWelcome.jsp"),
        @Forward(name = "showCandidacyDetails", path = "/candidate/degree/showCandidacyDetails.jsp"),
        @Forward(name = "fillData", path = "/candidate/degree/fillData.jsp"),
        @Forward(name = "showData", path = "/candidate/degree/showData.jsp"),
        @Forward(name = "showOperationFinished", path = "/candidate/degree/showOperationFinished.jsp") })
public class DegreeCandidacyManagementDispatchAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(DegreeCandidacyManagementDispatchAction.class);

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("showWelcome");
    }

    public ActionForward showCandidacyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        final StudentCandidacy candidacy = getCandidacy(request);
        request.setAttribute("candidacy", candidacy);

        final SortedSet<Operation> operations = new TreeSet<Operation>();
        operations.addAll(candidacy.getActiveCandidacySituation().getOperationsForPerson(getLoggedPerson(request)));
        request.setAttribute("operations", operations);

        request.setAttribute("person", getUserView(request).getPerson());
        return mapping.findForward("showCandidacyDetails");
    }

    public ActionForward doOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        final CandidacyOperation operation =
                (CandidacyOperation) getCandidacy(request).getActiveCandidacySituation().getOperationByTypeAndPerson(
                        getOperationType(request), getLoggedPerson(request));
        request.setAttribute("operation", operation);
        request.setAttribute("candidacy", getCandidacy(request));

        if (operation != null && operation.isInput()) {
            LogFirstTimeCandidacyTimestamp.logTimestamp(getCandidacy(request), FirstTimeCandidacyStage.STARTED_FILLING_FORMS);
            request.setAttribute("currentForm", operation.moveToNextForm());
            return mapping.findForward("fillData");
        } else {
            return executeOperation(mapping, form, request, response, operation);
        }

    }

    public ActionForward processForm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        request.setAttribute("candidacy", getCandidacy(request));

        final CandidacyOperation operation =
                (CandidacyOperation) RenderUtils.getViewState("operation-view-state").getMetaObject().getObject();
        request.setAttribute("operation", operation);

        if (!validateCurrentForm(request)) {
            return mapping.findForward("fillData");
        }

        if (operation.hasMoreForms()) {
            request.setAttribute("currentForm", operation.moveToNextForm());
            return mapping.findForward("fillData");
        } else {
            final StudentCandidacy candidacy = getCandidacy(request);
            if (candidacy.isConcluded()) {
                request.setAttribute("candidacyID", candidacy.getExternalId());

                addActionMessage(request, "warning.candidacy.process.is.already.concluded");

                return showCandidacyDetails(mapping, actionForm, request, response);
            }

            executeOperation(mapping, actionForm, request, response, operation);
            LogFirstTimeCandidacyTimestamp.logTimestamp(candidacy, FirstTimeCandidacyStage.FINISHED_FILLING_FORMS);

            return new ActionForward(null, buildSummaryPdfGeneratorURL(request, getCandidacy(request)), true, "/student");
        }
    }

    private boolean validateCurrentForm(HttpServletRequest request) {
        final Form form =
                (Form) RenderUtils.getViewState("fillData" + getCurrentFormPosition(request)).getMetaObject().getObject();
        final List<LabelFormatter> messages = form.validate();
        if (!messages.isEmpty()) {
            request.setAttribute("formMessages",
                    solveLabelFormatterArgs(request, messages.toArray(new LabelFormatter[messages.size()])));
            request.setAttribute("currentForm", form);

            return false;
        } else {
            return true;
        }

    }

    private ActionForward executeOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, CandidacyOperation candidacyOperation) throws FenixServiceException,
            FenixActionException {

        final User userView = getUserView(request);

        if (candidacyOperation != null) {
            ExecuteStateOperation.run(candidacyOperation, getLoggedPerson(request));
        }

        if (candidacyOperation != null && candidacyOperation.getType() == CandidacyOperationType.FILL_PERSONAL_DATA) {
            request.setAttribute(
                    "aditionalInformation",
                    getResources(request).getMessage("label.candidacy.username.changed.message",
                            userView.getPerson().getUsername(), Unit.getInstitutionAcronym()));
        }

        if (candidacyOperation != null) {
            request.setAttribute("candidacyID", candidacyOperation.getCandidacy().getExternalId());
        }

        return showCandidacyDetails(mapping, form, request, response);

    }

    private Object totalGratuityPaymentCode(Collection<PaymentCode> availablePaymentCodes) {
        for (PaymentCode paymentCode : availablePaymentCodes) {
            if (PaymentCodeType.GRATUITY_FIRST_INSTALLMENT.equals(paymentCode.getType())
                    && !(paymentCode instanceof InstallmentPaymentCode)) {
                return paymentCode;
            }
        }

        return null;
    }

    private Object installmmentPaymentCodes(Collection<PaymentCode> availablePaymentCodes) {
        List<PaymentCode> installmentPaymentCodes = new ArrayList<PaymentCode>();

        CollectionUtils.select(availablePaymentCodes, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                PaymentCode paymentCode = (PaymentCode) arg0;

                if (paymentCode instanceof InstallmentPaymentCode) {
                    return true;
                }

                return false;
            }
        }, installmentPaymentCodes);

        Collections.sort(installmentPaymentCodes, new BeanComparator("code"));

        return installmentPaymentCodes;
    }

    private Object administrativeOfficeFeeAndInsurancePaymentCode(Collection<PaymentCode> availablePaymentCodes) {
        for (PaymentCode paymentCode : availablePaymentCodes) {
            if (PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE.equals(paymentCode.getType())) {
                return paymentCode;
            }
        }

        return null;
    }

    private YearMonthDay calculateFirstInstallmentEndDate(final Registration registration,
            Collection<PaymentCode> availablePaymentCodes) {
        for (PaymentCode paymentCode : availablePaymentCodes) {
            if (!paymentCode.isInstallmentPaymentCode()) {
                continue;
            }

            InstallmentPaymentCode installmentPaymentCode = (InstallmentPaymentCode) paymentCode;
            if (!installmentPaymentCode.getInstallment().isForFirstTimeStudents()) {
                continue;
            }

            InstallmentForFirstTimeStudents firstInstallment =
                    (InstallmentForFirstTimeStudents) installmentPaymentCode.getInstallment();
            return registration.getStartDate().plusDays(firstInstallment.getNumberOfDaysToStartApplyingPenalty());
        }

        return null;
    }

    public ActionForward showCurrentForm(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        request.setAttribute("candidacy", getCandidacy(request));
        request.setAttribute("operation", RenderUtils.getViewState("operation-view-state").getMetaObject().getObject());
        Form form = (Form) RenderUtils.getViewState("fillData" + getCurrentFormPosition(request)).getMetaObject().getObject();
        request.setAttribute("currentForm", form);

        if (isPostback(request)) {
            if (getFromRequest(request, "country") != null) {
                ResidenceInformationForm rif = (ResidenceInformationForm) form;
                rif.setDistrictSubdivisionOfResidence(null);
            }
            RenderUtils.invalidateViewState();
        }

        return mapping.findForward("fillData");
    }

    private boolean isPostback(HttpServletRequest request) {
        return request.getParameter("postback") != null && Boolean.valueOf(request.getParameter("postback")).equals(Boolean.TRUE);
    }

    private StudentCandidacy getCandidacy(HttpServletRequest request) {
        return getDomainObject(request, "candidacyID");
    }

    private Integer getCurrentFormPosition(HttpServletRequest request) {
        final String requestParameter = request.getParameter("currentFormPosition");

        if (!StringUtils.isEmpty(requestParameter)) {
            return Integer.valueOf(requestParameter);
        } else {
            return null;
        }
    }

    private CandidacyOperationType getOperationType(HttpServletRequest request) {
        return CandidacyOperationType.valueOf(getFromRequest(request, "operationType").toString());
    }

    private String buildSummaryPdfGeneratorURL(HttpServletRequest request, final StudentCandidacy candidacy) {
        String url = "/student/firstTimeCandidacyDocuments.do?method=generateDocuments&candidacyID=" + candidacy.getExternalId();
        String urlWithChecksum =
                GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), url, request.getSession(false));
        return urlWithChecksum.substring("/student".length());
    }

    @Deprecated
    public ActionForward generateSummaryFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return new ActionForward(null, buildSummaryPdfGeneratorURL(request, getCandidacy(request)), false, "/student");
    }

    @Deprecated
    public ActionForward showSummaryFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        CandidacySummaryFile file = getCandidacy(request).getSummaryFile();

        response.reset();
        try {
            response.getOutputStream().write(file.getContent());
            response.setContentLength(file.getContent().length);
            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}