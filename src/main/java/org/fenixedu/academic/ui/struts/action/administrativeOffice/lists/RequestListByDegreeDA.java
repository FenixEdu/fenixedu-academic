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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestYear;
import org.fenixedu.academic.domain.serviceRequests.RegistrationAcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.dto.academicAdministration.DegreeByExecutionYearBean;
import org.fenixedu.academic.dto.academicAdministration.DocumentRequestSearchBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminServicesApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.StyledExcelSpreadsheet;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = AcademicAdminServicesApp.class, path = "requests-by-degree",
        titleKey = "lists.serviceRequestsByDegree", accessGroup = "academic(SERVICE_REQUESTS)")
@Mapping(path = "/requestListByDegree", module = "academicAdministration")
@Forwards({ @Forward(name = "searchRequests", path = "/academicAdminOffice/lists/searchRequestsByDegree.jsp") })
public class RequestListByDegreeDA extends FenixDispatchAction {

    private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";

    @EntryPoint
    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("degreeByExecutionYearBean", getOrCreateDegreeSearchBean());
        request.setAttribute("documentRequestSearchBean", getOrCreateRequestSearchBean());

        return mapping.findForward("searchRequests");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DegreeByExecutionYearBean degreeSearchBean = getOrCreateDegreeSearchBean();
        final DocumentRequestSearchBean requestSearchBean = getOrCreateRequestSearchBean();
        RenderUtils.invalidateViewState();
        request.setAttribute("degreeByExecutionYearBean", degreeSearchBean);
        request.setAttribute("documentRequestSearchBean", requestSearchBean);

        return mapping.findForward("searchRequests");
    }

    private DegreeByExecutionYearBean getOrCreateDegreeSearchBean() {
        DegreeByExecutionYearBean bean = getRenderedObject("degreeByExecutionYearBean");
        return (bean != null) ? bean : new DegreeByExecutionYearBean(AcademicAccessRule.getDegreeTypesAccessibleToFunction(
                AcademicOperationType.SERVICE_REQUESTS, Authenticate.getUser()).collect(Collectors.toSet()), AcademicAccessRule
                .getDegreesAccessibleToFunction(AcademicOperationType.SERVICE_REQUESTS, Authenticate.getUser()).collect(
                        Collectors.toSet()));
    }

    private DocumentRequestSearchBean getOrCreateRequestSearchBean() {
        DocumentRequestSearchBean bean = getRenderedObject("documentRequestSearchBean");
        return (bean != null) ? bean : new DocumentRequestSearchBean();
    }

    public ActionForward runSearchAndShowResults(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DegreeByExecutionYearBean degreeSearchBean = getOrCreateDegreeSearchBean();
        final DocumentRequestSearchBean requestSearchBean = getOrCreateRequestSearchBean();

        final Set<RegistrationAcademicServiceRequest> requestList = search(degreeSearchBean, requestSearchBean);

        request.setAttribute("degreeByExecutionYearBean", degreeSearchBean);
        request.setAttribute("documentRequestSearchBean", requestSearchBean);
        request.setAttribute("registrationAcademicServiceRequestList", requestList);

        return mapping.findForward("searchRequests");
    }

    private Set<RegistrationAcademicServiceRequest> search(DegreeByExecutionYearBean degreeSearchBean,
            DocumentRequestSearchBean requestSearchBean) {
        final ExecutionYear chosenExecutionYear = degreeSearchBean.getExecutionYear();
        Set<RegistrationAcademicServiceRequest> resultList =
                new TreeSet<RegistrationAcademicServiceRequest>(
                        RegistrationAcademicServiceRequest.COMPARATOR_BY_SERVICE_REQUEST_NUMBER_AND_ID);

        ArrayList<AcademicServiceRequest> requestList = new ArrayList<AcademicServiceRequest>();
        requestList.addAll(AcademicServiceRequestYear.getAcademicServiceRequests(chosenExecutionYear.getBeginCivilYear()));
        requestList.addAll(AcademicServiceRequestYear.getAcademicServiceRequests(chosenExecutionYear.getEndCivilYear()));

        return filterResults(degreeSearchBean, requestSearchBean, resultList, requestList);
    }

    private Set<RegistrationAcademicServiceRequest> filterResults(final DegreeByExecutionYearBean degreeSearchBean,
            final DocumentRequestSearchBean requestSearchBean, Set<RegistrationAcademicServiceRequest> resultList,
            final ArrayList<AcademicServiceRequest> requestList) {
        Set<AcademicProgram> accessiblePrograms =
                AcademicAccessRule
                        .getProgramsAccessibleToFunction(AcademicOperationType.SERVICE_REQUESTS, Authenticate.getUser()).collect(
                                Collectors.toSet());
        Set<DegreeType> accessibleDegreeTypes =
                AcademicAccessRule.getDegreeTypesAccessibleToFunction(AcademicOperationType.SERVICE_REQUESTS,
                        Authenticate.getUser()).collect(Collectors.toSet());

        final Degree chosenDegree = degreeSearchBean.getDegree();
        final DegreeType chosenDegreeType = degreeSearchBean.getDegreeType();
        final ExecutionYear chosenExecutionYear = degreeSearchBean.getExecutionYear();

        final AcademicServiceRequestType chosenServiceRequestType = requestSearchBean.getAcademicServiceRequestType();
        final DocumentRequestType chosenDocumentRequestType = requestSearchBean.getChosenDocumentRequestType();
        final AcademicServiceRequestSituationType chosenRequestSituation =
                requestSearchBean.getAcademicServiceRequestSituationType();

        for (final AcademicServiceRequest academicServiceRequest : requestList) {
            if (!academicServiceRequest.isRequestForRegistration()) {
                continue;
            }
            RegistrationAcademicServiceRequest request = (RegistrationAcademicServiceRequest) academicServiceRequest;

            DegreeCurricularPlan degreeCurricularPlan =
                    getMostRecentDegreeCurricularPlanForYear(request.getRegistration().getDegree(), chosenExecutionYear);
            if ((chosenDegreeType != null)
                    && (degreeCurricularPlan == null || chosenDegreeType != degreeCurricularPlan.getDegreeType())) {
                continue;
            }
            if ((degreeCurricularPlan != null) && (!degreeCurricularPlan.getDegreeType().isEmpty())
                    && (!accessibleDegreeTypes.contains(degreeCurricularPlan.getDegreeType()))) {
                continue;
            }

            if (chosenDegree != null && chosenDegree != request.getRegistration().getDegree()) {
                continue;
            }
            if (!accessiblePrograms.contains(request.getAcademicProgram())) {
                continue;
            }
            if (chosenServiceRequestType != null && chosenServiceRequestType != request.getAcademicServiceRequestType()) {
                continue;
            }
            if (request.getAcademicServiceRequestType() == AcademicServiceRequestType.DOCUMENT) {
                DocumentRequestType documentType = ((DocumentRequest) request).getDocumentRequestType();
                if ((chosenDocumentRequestType != null) && (chosenDocumentRequestType != documentType)) {
                    continue;
                }
            }
            if (chosenRequestSituation != null
                    && chosenRequestSituation != request.getActiveSituation().getAcademicServiceRequestSituationType()) {
                continue;
            }
            if (requestSearchBean.isUrgentRequest() && !request.isUrgentRequest()) {
                continue;
            }
            resultList.add(request);
        }

        return resultList;
    }

    private DegreeCurricularPlan getMostRecentDegreeCurricularPlanForYear(Degree degree, final ExecutionYear executionYear) {
        DegreeCurricularPlan degreeCurricularPlan = null;
        for (DegreeCurricularPlan plan : degree.getDegreeCurricularPlansForYear(executionYear)) {
            if ((degreeCurricularPlan == null)
                    || (degreeCurricularPlan.getMostRecentExecutionDegree().isBefore(plan.getMostRecentExecutionDegree()))) {
                degreeCurricularPlan = plan;
            }
        }
        if (degreeCurricularPlan == null) {
            degreeCurricularPlan = degree.getMostRecentDegreeCurricularPlan();
        }
        return degreeCurricularPlan;
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final DegreeByExecutionYearBean degreeSearchBean = getOrCreateDegreeSearchBean();
        final DocumentRequestSearchBean requestSearchBean = getOrCreateRequestSearchBean();
        if ((degreeSearchBean == null) || (requestSearchBean == null)) {
            return null;
        }
        final Set<RegistrationAcademicServiceRequest> requestList = search(degreeSearchBean, requestSearchBean);

        try {
            String filename = getResourceMessage("label.requests");

            Degree degree = degreeSearchBean.getDegree();
            DegreeType degreeType = degreeSearchBean.getDegreeType();
            ExecutionYear executionYear = degreeSearchBean.getExecutionYear();
            if (degree != null) {
                filename += "_" + degree.getNameFor(executionYear).getContent().replace(' ', '_');
            } else if (degreeType != null) {
                filename += "_" + degreeType.getName().getContent().replace(' ', '_');
            }
            filename += "_" + executionYear.getYear();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
            ServletOutputStream writer = response.getOutputStream();

            exportToXls(requestList, writer, degreeSearchBean, requestSearchBean);
            writer.flush();
            response.flushBuffer();
            return null;

        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private void exportToXls(Set<RegistrationAcademicServiceRequest> requestList, OutputStream outputStream,
            DegreeByExecutionYearBean degreeSearchBean, DocumentRequestSearchBean requestSearchBean) throws IOException {

        final StyledExcelSpreadsheet spreadsheet =
                new StyledExcelSpreadsheet(getResourceMessage("label.requestByDegree.unspaced"));

        fillSpreadSheetFilters(degreeSearchBean, requestSearchBean, spreadsheet);
        fillSpreadSheetResults(requestList, spreadsheet);
        spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheetFilters(DegreeByExecutionYearBean degreeSearchBean, DocumentRequestSearchBean requestSearchBean,
            final StyledExcelSpreadsheet spreadsheet) {
        AcademicServiceRequestType requestType = requestSearchBean.getAcademicServiceRequestType();
        DocumentRequestType documentType = requestSearchBean.getChosenDocumentRequestType();
        AcademicServiceRequestSituationType situationType = requestSearchBean.getAcademicServiceRequestSituationType();
        spreadsheet.newHeaderRow();
        if (requestType != null) {
            spreadsheet.addHeader(getResourceMessage("label.type") + ": " + requestType.getLocalizedName());
        }
        if (documentType != null) {
            spreadsheet
                    .addHeader(getResourceMessage("label.documentRequestsManagement.searchDocumentRequests.documentRequestType")
                            + ": " + documentType.getQualifiedName());
        }
        spreadsheet.newHeaderRow();
        if (situationType != null) {
            spreadsheet.addHeader(getResourceMessage("label.state") + ": " + situationType.getLocalizedName());
        }
        spreadsheet.newHeaderRow();
        if (requestSearchBean.isUrgentRequest()) {
            spreadsheet.addHeader(getResourceMessage("label.urgent.plural"));
        }
    }

    private void fillSpreadSheetResults(Set<RegistrationAcademicServiceRequest> requestList,
            final StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newRow();
        spreadsheet.newRow();
        spreadsheet.addCell(requestList.size() + " " + getResourceMessage("label.requests"));

        setHeaders(spreadsheet);
        for (RegistrationAcademicServiceRequest request : requestList) {
            spreadsheet.newRow();
            spreadsheet.addCell(request.getServiceRequestNumber());
            spreadsheet.addCell(request.getRequestDate().toString(DATETIME_FORMAT));
            spreadsheet.addCell(request.getDescription());
            spreadsheet.addCell(request.getStudent().getNumber());
            spreadsheet.addCell(request.getStudent().getName());
            spreadsheet.addCell(request.getRegistration().getDegree().getPresentationName());
            spreadsheet.addCell("-");
        }
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(getResourceMessage("label.serviceRequestNumber"));
        spreadsheet.addHeader(getResourceMessage("label.requestDate"));
        spreadsheet
                .addHeader(getResourceMessage("label.org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest.description"));
        spreadsheet.addHeader(getResourceMessage("label.studentNumber"));
        spreadsheet.addHeader(getResourceMessage("label.student.name"));
        spreadsheet.addHeader(getResourceMessage("degree.concluded"));
        spreadsheet.addHeader(getResourceMessage("conclusion.date"));

    }

    static private String getResourceMessage(String key) {
        return BundleUtil.getString(Bundle.ACADEMIC, key);
    }
}