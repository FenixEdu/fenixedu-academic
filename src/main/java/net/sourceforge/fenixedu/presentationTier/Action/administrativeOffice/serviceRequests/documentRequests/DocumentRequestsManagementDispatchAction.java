/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests.documentRequests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jvstm.cps.ConsistencyException;
import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.DocumentRequestCreator;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.certificates.ExamDateCertificateExamSelectionBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests.AcademicServiceRequestsManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/documentRequestsManagement", module = "academicAdministration",
        formBeanClass = AcademicServiceRequestsManagementDispatchAction.AcademicServiceRequestsManagementForm.class,
        functionality = AcademicServiceRequestsManagementDispatchAction.class)
@Forwards({
        @Forward(name = "printDocument", path = "/academicAdminOffice/serviceRequests/documentRequests/printDocument.jsp"),
        @Forward(name = "createDocumentRequests",
                path = "/academicAdminOffice/serviceRequests/documentRequests/createDocumentRequests.jsp"),
        @Forward(name = "viewDocumentRequestsToCreate",
                path = "/academicAdminOffice/serviceRequests/documentRequests/viewDocumentRequestsToCreate.jsp"),
        @Forward(name = "chooseExamsToCreateExamDateCertificateRequest",
                path = "/academicAdminOffice/serviceRequests/documentRequests/chooseExamsToCreateExamDateCertificateRequest.jsp"),
        @Forward(name = "viewRegistrationDetails", path = "/academicAdminOffice/student/registration/viewRegistrationDetails.jsp"),
        @Forward(name = "processNewAcademicServiceRequest",
                path = "/academicAdministration/academicServiceRequestsManagement.do?method=processNewAcademicServiceRequest") })
public class DocumentRequestsManagementDispatchAction extends FenixDispatchAction {

    protected IDocumentRequest getDocumentRequest(HttpServletRequest request) {
        return (IDocumentRequest) getDomainObject(request, "documentRequestId");
    }

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
        final AcademicServiceRequest academicServiceRequest = getDomainObject(request, "academicServiceRequestId");
        request.setAttribute("academicServiceRequest", academicServiceRequest);
        return academicServiceRequest;
    }

    public ActionForward downloadDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final IDocumentRequest documentRequest = getDocumentRequest(request);
        GeneratedDocument doc = documentRequest.getLastGeneratedDocument();
        if (doc != null) {
            final ServletOutputStream writer = response.getOutputStream();
            try {
                response.setContentLength(doc.getSize().intValue());
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=" + doc.getFilename());
                writer.write(doc.getContent());
                writer.flush();
            } finally {
                writer.close();
            }
        }
        return null;
    }

    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws JRException, IOException, FenixServiceException {
        final IDocumentRequest documentRequest = getDocumentRequest(request);
        try {
            byte[] data = documentRequest.generateDocument();

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + documentRequest.getReportFileName() + ".pdf");

            final ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();

            response.flushBuffer();
            return null;
        } catch (DomainException e) {
            throw e;
        }
    }

    public ActionForward prepareConcludeDocumentRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        if (request.getAttribute("academicServiceRequest") == null) {
            request.setAttribute("academicServiceRequest", getAndSetAcademicServiceRequest(request));
        }
        return mapping.findForward("printDocument");
    }

    private Registration getRegistration(final HttpServletRequest request) {
        final Registration registration = getDomainObject(request, "registrationId");
        request.setAttribute("registration", registration);
        return registration;
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String schema = "DocumentRequestCreateBean.chooseDocumentRequestType";
        if (!getRegistration(request).isBolonha()) {
            schema += "_preBolonha";
        }
        return prepareCreateDocumentRequest(mapping, form, request, response, schema);
    }

    public ActionForward prepareCreateDocumentRequestQuick(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareCreateDocumentRequest(mapping, form, request, response,
                "DocumentRequestCreateBean.chooseDocumentRequestQuickType");
    }

    private ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String schema) {

        final DocumentRequestCreator creator = new DocumentRequestCreator(getRegistration(request));
        creator.setSchema(schema);
        request.setAttribute("documentRequestCreateBean", creator);
        return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean = getRenderedObject();

        if (requestCreateBean.getChosenDocumentRequestType() != null) {
            getAndSetSpecialEnrolments(request, requestCreateBean);
        }
        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeChosenPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return createDocumentRequestPostback(mapping, request);
    }

    private ActionForward createDocumentRequestPostback(ActionMapping mapping, HttpServletRequest request) {
        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        if (requestCreateBean.getChosenDocumentRequestType() != null) {
            getAndSetSpecialEnrolments(request, requestCreateBean);
        }
        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("createDocumentRequests");
    }

    private void getAndSetSpecialEnrolments(HttpServletRequest request, DocumentRequestCreateBean requestCreateBean) {
        final StudentCurricularPlan curricularPlan = requestCreateBean.getRegistration().getLastStudentCurricularPlan();
        final DocumentRequestType requestType = requestCreateBean.getChosenDocumentRequestType();
        if (requestType.equals(DocumentRequestType.EXTRA_CURRICULAR_CERTIFICATE)) {
            List<Enrolment> enrolments = curricularPlan.getExtraCurricularApprovedEnrolmentsNotInDismissal();
            if (enrolments.size() == 0) {
                addActionMessage("warning", request, "warning.ExtraCurricularCertificateRequest.no.enrolments.available");
            }
            requestCreateBean.setEnrolments(enrolments);
        }
        if (requestType.equals(DocumentRequestType.STANDALONE_ENROLMENT_CERTIFICATE)) {
            List<Enrolment> enrolments = curricularPlan.getStandaloneApprovedEnrolmentsNotInDismissal();
            if (enrolments.size() == 0) {
                addActionMessage("warning", request, "warning.StandaloneEnrolmentCertificateRequest.no.enrolments.available");
            }
            requestCreateBean.setEnrolments(enrolments);
        }
    }

    private void setAdditionalInformationSchemaName(HttpServletRequest request, final DocumentRequestCreateBean requestCreateBean) {
        if (!requestCreateBean.getHasAdditionalInformation()) {
            return;
        }
        DocumentRequestType requestType = requestCreateBean.getChosenDocumentRequestType();
        final StringBuilder schemaName = new StringBuilder();
        schemaName.append("DocumentRequestCreateBean.");
        schemaName.append(requestType.name());

        if (requestType.equals(DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE)
                && !requestCreateBean.getRegistrationAgreement().isNormal()) {
            schemaName.append("_mobility");
        }

        if (!requestCreateBean.getRegistration().isBolonha() && requestType.withBranch()) {
            schemaName.append("_WithBranch");
        }

        schemaName.append(".AdditionalInformation");
        request.setAttribute("additionalInformationSchemaName", schemaName.toString());
    }

    public ActionForward executionYearToCreateDocumentChangedPostBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return createDocumentRequestPostback(mapping, request);
    }

    public ActionForward executionPeriodToCreateDocumentChangedPostBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return createDocumentRequestPostback(mapping, request);
    }

    public ActionForward viewDocumentRequestToCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();

        if (requestCreateBean.getChosenDocumentRequestType() == DocumentRequestType.EXAM_DATE_CERTIFICATE) {
            return prepareChooseExamsToCreateExamDateCertificateRequest(mapping, actionForm, request, response, requestCreateBean);
        }

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("viewDocumentRequestsToCreate");
    }

    public ActionForward prepareChooseExamsToCreateExamDateCertificateRequest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response, DocumentRequestCreateBean requestCreateBean) {

        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        final ExamDateCertificateExamSelectionBean examSelectionBean =
                ExamDateCertificateExamSelectionBean.buildFor(requestCreateBean.getEnrolments(),
                        requestCreateBean.getExecutionPeriod());
        request.setAttribute("examSelectionBean", examSelectionBean);
        request.setAttribute("enrolmentsWithoutExam",
                examSelectionBean.getEnrolmentsWithoutExam(requestCreateBean.getEnrolments()));

        return mapping.findForward("chooseExamsToCreateExamDateCertificateRequest");

    }

    public ActionForward chooseExamsToCreateExamDateCertificateRequest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean = getRenderedObject("documentRequestCreateBean");
        requestCreateBean.setExams(getSelectedExams(request));

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("viewDocumentRequestsToCreate");
    }

    private List<Exam> getSelectedExams(final HttpServletRequest request) {
        final String[] examIds = request.getParameterValues("selectedExams");

        if (examIds == null) {
            return Collections.emptyList();
        }

        final List<Exam> result = new ArrayList<Exam>();
        for (final String examId : examIds) {
            result.add((Exam) FenixFramework.getDomainObject(examId));
        }

        return result;

    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final DocumentRequestCreateBean documentRequestCreateBean = getRenderedObject();
        final Registration registration = documentRequestCreateBean.getRegistration();
        request.setAttribute("registration", registration);

        DocumentRequest documentRequest = null;
        try {
            documentRequest = (DocumentRequest) executeFactoryMethod();
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
            return mapping.findForward("viewRegistrationDetails");
        } catch (ConsistencyException ce) {
            addActionMessage(request, "error." + ce.getMethodFullname());
            return mapping.findForward("viewRegistrationDetails");
        }

        if (documentRequestCreateBean.getChosenDocumentRequestType().isAllowedToQuickDeliver()) {
            request.setAttribute("academicServiceRequestId", documentRequest.getExternalId());
            return mapping.findForward("processNewAcademicServiceRequest");
        } else {
            addActionMessage(request, "document.request.created.with.success");
            return mapping.findForward("viewRegistrationDetails");
        }
    }

    public ActionForward useAllPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean documentRequestCreateBean = getRenderedObject();
        if (documentRequestCreateBean.isToUseAll()) {
            Set<Degree> degrees =
                    AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
                            AcademicOperationType.SERVICE_REQUESTS);
            Set<Enrolment> aprovedEnrolments = new HashSet<Enrolment>();
            for (Degree degree : degrees) {
                for (final Registration registration : documentRequestCreateBean.getStudent().getRegistrationsFor(degree)) {
                    aprovedEnrolments.addAll(registration.getApprovedEnrolments());
                }
            }
            documentRequestCreateBean.setEnrolments(new ArrayList<Enrolment>(aprovedEnrolments));
        } else {
            documentRequestCreateBean.setEnrolments(new ArrayList<Enrolment>());
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("documentRequestCreateBean", documentRequestCreateBean);
        setAdditionalInformationSchemaName(request, documentRequestCreateBean);

        return mapping.findForward("createDocumentRequests");
    }
}