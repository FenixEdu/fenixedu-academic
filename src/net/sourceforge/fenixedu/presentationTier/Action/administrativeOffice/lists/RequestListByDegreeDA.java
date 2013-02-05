package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.academicAdministration.DegreeByExecutionYearBean;
import net.sourceforge.fenixedu.dataTransferObject.academicAdministration.DocumentRequestSearchBean;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestYear;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@Mapping(path = "/requestListByDegree", module = "academicAdministration")
@Forwards({ @Forward(name = "searchRequests", path = "/academicAdminOffice/lists/searchRequestsByDegree.jsp") })
public class RequestListByDegreeDA extends FenixDispatchAction {

    private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";

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
        return (bean != null) ? bean : new DegreeByExecutionYearBean(AcademicAuthorizationGroup.getDegreeTypesForOperation(
                AccessControl.getPerson(), AcademicOperationType.SERVICE_REQUESTS),
                AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
                        AcademicOperationType.SERVICE_REQUESTS));
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
                AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
                        AcademicOperationType.SERVICE_REQUESTS);
        Set<DegreeType> accessibleDegreeTypes =
                AcademicAuthorizationGroup.getDegreeTypesForOperation(AccessControl.getPerson(),
                        AcademicOperationType.SERVICE_REQUESTS);

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
            if ((degreeCurricularPlan != null) && (degreeCurricularPlan.getDegreeType() != DegreeType.EMPTY)
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
                filename += "_" + BundleUtil.getEnumName(degreeType).replace(' ', '_');
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
            spreadsheet.addHeader(getResourceMessage("label.type") + ": " + BundleUtil.getEnumName(requestType));
        }
        if (documentType != null) {
            spreadsheet
                    .addHeader(getResourceMessage("label.documentRequestsManagement.searchDocumentRequests.documentRequestType")
                            + ": " + BundleUtil.getEnumName(documentType));
        }
        spreadsheet.newHeaderRow();
        if (situationType != null) {
            spreadsheet.addHeader(getResourceMessage("label.state") + ": " + BundleUtil.getEnumName(situationType));
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
        }
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(getResourceMessage("label.serviceRequestNumber"));
        spreadsheet.addHeader(getResourceMessage("label.requestDate"));
        spreadsheet
                .addHeader(getResourceMessage("label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.description"));
        spreadsheet.addHeader(getResourceMessage("label.studentNumber"));
        spreadsheet.addHeader(getResourceMessage("label.student.name"));
    }

    static private String getResourceMessage(String key) {
        return BundleUtil.getMessageFromModuleOrApplication("academicAdminOffice", key);
    }
}