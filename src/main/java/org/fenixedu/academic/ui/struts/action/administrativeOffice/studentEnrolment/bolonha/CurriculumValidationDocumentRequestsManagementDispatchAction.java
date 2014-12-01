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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment.bolonha;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.administrativeOffice.curriculumValidation.DocumentPrintRequest;
import org.fenixedu.academic.domain.documents.DocumentRequestGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.report.academicAdministrativeOffice.AdministrativeOfficeDocument;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.serviceRequests.documentRequests.DocumentRequestsManagementDispatchAction;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/curriculumValidationDocumentRequestsManagement", module = "academicAdministration",
        functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "printDocument",
                path = "/academicAdminOffice/student/curriculumValidation/documentRequests/printDocument.jsp"),
        @Forward(name = "viewDocuments",
                path = "/academicAdminOffice/student/curriculumValidation/documentRequests/viewDocuments.jsp"),
        @Forward(name = "notValidDocument",
                path = "/academicAdminOffice/student/curriculumValidation/documentRequests/notValidDocument.jsp") })
public class CurriculumValidationDocumentRequestsManagementDispatchAction extends DocumentRequestsManagementDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(request);
        request.setAttribute("studentCurriculumValidationAllowed",
                studentCurricularPlan.getEvaluationForCurriculumValidationAllowed());

        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward listDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(request);

        Registration registration = studentCurricularPlan.getRegistration();
        request.setAttribute("registration", registration);

        return mapping.findForward("viewDocuments");
    }

    public ActionForward preparePrintDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final IDocumentRequest documentRequest = getDocumentRequest(request);

        if (!(DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE.equals(documentRequest.getDocumentRequestType()) || DocumentRequestType.DIPLOMA_REQUEST
                .equals(documentRequest.getDocumentRequestType()))) {
            return mapping.findForward("notValidDocument");
        }

        String conlusionDate = "";
        String degreeDescription = "";
        String graduatedTitle = "";
        if (DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE.equals(documentRequest.getDocumentRequestType())) {
            AdministrativeOfficeDocument document =
                    AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(documentRequest).iterator().next();
            conlusionDate = (String) document.getParameters().get("degreeFinalizationDate");
            degreeDescription = (String) document.getParameters().get("degreeDescription");
            graduatedTitle = (String) document.getParameters().get("graduateTitle");
        } else if (DocumentRequestType.DIPLOMA_REQUEST.equals(documentRequest.getDocumentRequestType())) {
            AdministrativeOfficeDocument document =
                    AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(documentRequest).iterator().next();
            conlusionDate =
                    (String) AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(documentRequest).iterator()
                            .next().getParameters().get("conclusionDate");
            degreeDescription = (String) document.getParameters().get("degreeFilteredName");
            graduatedTitle = (String) document.getParameters().get("graduateTitle");
        }

        DocumentFieldsCustomization customization = new DocumentFieldsCustomization();
        customization.setConclusionDate(conlusionDate);
        customization.setDegreeDescription(degreeDescription);
        customization.setGraduatedTitle(graduatedTitle);

        request.setAttribute("documentFieldsCustomization", customization);
        request.setAttribute("documentRequest", documentRequest);

        return mapping.findForward("printDocument");
    }

    @Override
    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException, FenixServiceException {
        final IDocumentRequest documentRequest = getDocumentRequest(request);
        try {
            final List<AdministrativeOfficeDocument> documents =
                    AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(documentRequest);

            DocumentFieldsCustomization customization = getRenderedObject("document.fields.customization");

            if (documentRequest.isRequestForRegistration()) {
                DocumentPrintRequest.logRequest(customization.getConclusionDate(), customization.getDegreeDescription(),
                        customization.getGraduatedTitle(), (DocumentRequest) documentRequest);
            }

            if (DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE.equals(documentRequest.getDocumentRequestType())) {
                documents.iterator().next().getParameters().put("degreeFinalizationDate", customization.getConclusionDate());
                documents.iterator().next().getParameters().put("degreeDescription", customization.getDegreeDescription());
                documents.iterator().next().getParameters().put("graduateTitle", customization.getGraduatedTitle());
            } else if (DocumentRequestType.DIPLOMA_REQUEST.equals(documentRequest.getDocumentRequestType())) {
                documents.iterator().next().getParameters().put("conclusionDate", customization.getConclusionDate());
                documents.iterator().next().getParameters().put("degreeFilteredName", customization.getDegreeDescription());
                documents.iterator().next().getParameters().put("graduateTitle", customization.getGraduatedTitle());
            }

            final AdministrativeOfficeDocument[] array = {};
            byte[] data = ReportsUtils.generateReport(documents.toArray(array)).getData();

            DocumentRequestGeneratedDocument.store(documentRequest, documents.iterator().next().getReportFileName() + ".pdf",
                    data);
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + documents.iterator().next().getReportFileName()
                    + ".pdf");

            final ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();

            response.flushBuffer();
            return mapping.findForward("");
        } catch (DomainException e) {
            addActionMessage(request, e.getKey());
            if (documentRequest.isRequestForRegistration()) {
                request.setAttribute("registration", ((DocumentRequest) documentRequest).getRegistration());
            }
            return mapping.findForward("viewRegistrationDetails");
        }
    }

    private StudentCurricularPlan readStudentCurricularPlan(HttpServletRequest request) {
        if (request.getAttribute("studentCurricularPlan") != null) {
            return (StudentCurricularPlan) request.getAttribute("studentCurricularPlan");
        }

        request.setAttribute("studentCurricularPlan", getDomainObject(request, "studentCurricularPlanId"));
        return getDomainObject(request, "studentCurricularPlanId");

    }

    public static class DocumentFieldsCustomization implements java.io.Serializable {
        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private String conclusionDate;
        private String degreeDescription;
        private String graduatedTitle;

        public DocumentFieldsCustomization() {

        }

        public String getConclusionDate() {
            return this.conclusionDate;
        }

        public void setConclusionDate(final String value) {
            this.conclusionDate = value;
        }

        public String getDegreeDescription() {
            return degreeDescription;
        }

        public void setDegreeDescription(String degreeDescription) {
            this.degreeDescription = degreeDescription;
        }

        public String getGraduatedTitle() {
            return graduatedTitle;
        }

        public void setGraduatedTitle(String graduatedTitle) {
            this.graduatedTitle = graduatedTitle;
        }
    }
}
