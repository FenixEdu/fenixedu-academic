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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.gradeSubmission;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.MarkSheetState;
import org.fenixedu.academic.domain.SignedMarkSheet;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetSearchResultBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.administrativeOffice.gradeSubmission.SearchMarkSheets;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.manager.RemoveGradesFromConfirmedMarkSheet;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminMarksheetApp;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@StrutsFunctionality(app = AcademicAdminMarksheetApp.class, path = "search", titleKey = "link.markSheet.management")
@Mapping(path = "/markSheetManagement", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/markSheetManagement.jsp")
@Forward(name = "searchMarkSheet", path = "/academicAdministration/gradeSubmission/markSheetManagement.jsp")
@Forward(name = "viewMarkSheet", path = "/academicAdministration/gradeSubmission/viewMarkSheet.jsp")
@Forward(name = "removeMarkSheet", path = "/academicAdministration/gradeSubmission/removeMarkSheet.jsp")
@Forward(name = "searchMarkSheetFilled",
        path = "/academicAdministration/markSheetManagement.do?method=prepareSearchMarkSheetFilled")
@Forward(name = "confirmMarkSheet", path = "/academicAdministration/gradeSubmission/confirmMarkSheet.jsp")
@Forward(name = "listMarkSheet", path = "/manager/markSheet/viewMarkSheet.jsp")
@Forward(name = "choosePrinterMarkSheetsWeb", path = "/academicAdministration/gradeSubmission/choosePrinterMarkSheetsWeb_bd.jsp")
public class MarkSheetSearchDispatchAction extends MarkSheetDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
        markSheetManagementSearchBean.setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
        request.setAttribute("edit", markSheetManagementSearchBean);

        return mapping.findForward("searchMarkSheet");
    }

    public ActionForward prepareSearchMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        MarkSheetManagementSearchBean markSheetBean = new MarkSheetManagementSearchBean();
        fillMarkSheetSearchBean(actionForm, request, markSheetBean);

        if (markSheetBean.getCurricularCourse() == null) {
            return prepareSearchMarkSheet(mapping, actionForm, request, response);
        } else {
            return searchMarkSheets(mapping, actionForm, request, response, markSheetBean);
        }
    }

    public ActionForward searchMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        MarkSheetManagementSearchBean searchBean =
                (MarkSheetManagementSearchBean) RenderUtils.getViewState().getMetaObject().getObject();
        return searchMarkSheets(mapping, actionForm, request, response, searchBean);
    }

    private ActionForward searchMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, MarkSheetManagementSearchBean searchBean) throws FenixServiceException {

        ActionMessages actionMessages = createActionMessages();
        try {
            Map<EvaluationSeason, MarkSheetSearchResultBean> result = SearchMarkSheets.run(searchBean);

            request.setAttribute("edit", searchBean);
            request.setAttribute("searchResult", result);
            request.setAttribute("url", buildSearchUrl(searchBean));

        } catch (InvalidArgumentsServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        }
        return mapping.getInputForward();
    }

    protected void fillMarkSheetSearchBean(ActionForm actionForm, HttpServletRequest request,
            MarkSheetManagementSearchBean markSheetBean) {
        DynaActionForm form = (DynaActionForm) actionForm;
        fillMarkSheetBean(actionForm, request, markSheetBean);

        if (form.getString("tn") != null && form.getString("tn").length() != 0) {
            markSheetBean.setTeacherId(form.getString("tn"));
        }
        try {
            markSheetBean.setEvaluationDate(DateFormatUtil.parse("dd/MM/yyyy", form.getString("ed")));
        } catch (ParseException e) {
            markSheetBean.setEvaluationDate(null);
        }
        if (form.getString("mss") != null && form.getString("mss").length() != 0) {
            markSheetBean.setMarkSheetState(MarkSheetState.valueOf(form.getString("mss")));
        }
        if (form.getString("mst") != null && form.getString("mst").length() != 0) {
            markSheetBean.setEvaluationSeason(FenixFramework.<EvaluationSeason> getDomainObject(form.getString("mst")));
        }
    }

    private String buildSearchUrl(MarkSheetManagementSearchBean searchBean) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("&epID=").append(searchBean.getExecutionPeriod().getExternalId());
        stringBuilder.append("&dID=").append(searchBean.getDegree().getExternalId());
        stringBuilder.append("&dcpID=").append(searchBean.getDegreeCurricularPlan().getExternalId());
        stringBuilder.append("&ccID=").append(searchBean.getCurricularCourse().getExternalId());

        if (searchBean.getTeacherId() != null) {
            stringBuilder.append("&tn=").append(searchBean.getTeacherId());
        }
        if (searchBean.getEvaluationDate() != null) {
            stringBuilder.append("&ed=").append(DateFormatUtil.format("dd/MM/yyyy", searchBean.getEvaluationDate()));
        }
        if (searchBean.getMarkSheetState() != null) {
            stringBuilder.append("&mss=").append(searchBean.getMarkSheetState().getName());
        }
        if (searchBean.getEvaluationSeason() != null) {
            stringBuilder.append("&mst=").append(searchBean.getEvaluationSeason().getExternalId());
        }
        return stringBuilder.toString();
    }

    public ActionForward prepareViewRectificationMarkSheet(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        EnrolmentEvaluation enrolmentEvaluation = getDomainObject(form, "evaluationID");
        MarkSheet markSheet = enrolmentEvaluation.getRectificationMarkSheet();

        request.setAttribute("markSheet", markSheet);
        request.setAttribute("url", buildUrl(form));

        return mapping.findForward("viewMarkSheet");
    }

    public ActionForward searchConfirmedMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetManagementSearchBean searchBean = getRenderedObject();

        Collection<MarkSheet> result = new ArrayList<MarkSheet>();
        for (MarkSheet markSheet : searchBean.getCurricularCourse().getMarkSheetsSet()) {
            if (markSheet.getExecutionPeriod() == searchBean.getExecutionPeriod() && markSheet.isConfirmed()) {
                result.add(markSheet);
            }
        }

        request.setAttribute("edit", searchBean);
        request.setAttribute("searchResult", result);
        request.setAttribute("url", buildSearchUrl(searchBean));

        return mapping.findForward("searchMarkSheet");
    }

    public ActionForward listMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;

        MarkSheet markSheet = getDomainObject(form, "msID");

        request.setAttribute("markSheet", markSheet);
        request.setAttribute("url", buildUrl(form));

        return mapping.findForward("listMarkSheet");
    }

    public ActionForward searchConfirmedMarkSheetsFilled(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        MarkSheetManagementSearchBean searchBean = new MarkSheetManagementSearchBean();
        fillMarkSheetBean(actionForm, request, searchBean);

        Collection<MarkSheet> result = new ArrayList<MarkSheet>();
        for (MarkSheet markSheet : searchBean.getCurricularCourse().getMarkSheetsSet()) {
            if (markSheet.getExecutionPeriod() == searchBean.getExecutionPeriod() && markSheet.isConfirmed()) {
                result.add(markSheet);
            }
        }

        request.setAttribute("edit", searchBean);
        request.setAttribute("searchResult", result);
        request.setAttribute("url", buildSearchUrl(searchBean));

        return mapping.findForward("searchMarkSheet");
    }

    public ActionForward removeGrades(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = getDomainObject(form, "msID");
        List<EnrolmentEvaluation> evaluations = getEvaluationsToRemove(form);
        try {
            RemoveGradesFromConfirmedMarkSheet.run(markSheet, evaluations);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return listMarkSheet(mapping, actionForm, request, response);
        }

        return prepareSearchMarkSheetFilled(mapping, actionForm, request, response);
    }

    private List<EnrolmentEvaluation> getEvaluationsToRemove(DynaActionForm actionForm) {
        List<EnrolmentEvaluation> res = new ArrayList<EnrolmentEvaluation>();
        String[] evaluationsToRemove = (String[]) actionForm.get("evaluationsToRemove");
        for (String eeID : evaluationsToRemove) {
            EnrolmentEvaluation enrolmentEvaluation = FenixFramework.getDomainObject(eeID);
            if (enrolmentEvaluation != null) {
                res.add(enrolmentEvaluation);
            }
        }
        return res;
    }

    public ActionForward choosePrinterMarkSheetsWeb(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response,
                ExecutionSemester.readActualExecutionSemester(), null);
    }

    public ActionForward choosePrinterMarkSheetsWebPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final DynaActionForm form = (DynaActionForm) actionForm;

        final ExecutionSemester executionSemester = getExecutionSemester(form);
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(form);

        return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response, executionSemester, degreeCurricularPlan);
    }

    private ExecutionSemester getExecutionSemester(DynaActionForm form) {
        return getDomainObject(form, "ecID");
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(DynaActionForm form) {
        return getDomainObject(form, "dcpID");
    }

    private ActionForward choosePrinterMarkSheetsWeb(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, ExecutionSemester executionSemester, DegreeCurricularPlan degreeCurricularPlan) {

        DynaActionForm form = (DynaActionForm) actionForm;

        final Collection<MarkSheet> webMarkSheetsNotPrinted =
                executionSemester.getWebMarkSheetsNotPrinted(AccessControl.getPerson(), degreeCurricularPlan);

        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("curricularCourseMap", buildMapWithCurricularCoursesAndNumberOfMarkSheets(webMarkSheetsNotPrinted));
        request.setAttribute("totalMarkSheetsCount", webMarkSheetsNotPrinted.size());

        buildPeriods(request);
        buildDegreeCurricularPlans(request, executionSemester);

        form.set("ecID", executionSemester.getExternalId().toString());
        if (degreeCurricularPlan != null) {
            form.set("dcpID", degreeCurricularPlan.getExternalId().toString());
        }

        return mapping.findForward("choosePrinterMarkSheetsWeb");
    }

    private void buildDegreeCurricularPlans(HttpServletRequest request, ExecutionSemester semester) {

        final List<DegreeCurricularPlan> dcps =
                new ArrayList<DegreeCurricularPlan>(semester.getExecutionYear().getDegreeCurricularPlans());
        Collections.sort(dcps, DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);

        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        Set<Degree> degreesForMarksheets =
                AcademicAccessRule
                        .getDegreesAccessibleToFunction(AcademicOperationType.MANAGE_MARKSHEETS, Authenticate.getUser()).collect(
                                Collectors.toSet());

        for (final DegreeCurricularPlan dcp : dcps) {
            if (degreesForMarksheets.contains(dcp.getDegree())) {
                result.add(new LabelValueBean(dcp.getPresentationName(semester.getExecutionYear()), dcp.getExternalId()
                        .toString()));
            }
        }

        request.setAttribute("degreeCurricularPlans", result);
    }

    private void buildPeriods(HttpServletRequest request) {
        final List<ExecutionSemester> notClosedExecutionPeriods = ExecutionSemester.readNotClosedExecutionPeriods();
        Collections.sort(notClosedExecutionPeriods, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

        final List<LabelValueBean> periods = new ArrayList<LabelValueBean>();
        for (final ExecutionSemester period : notClosedExecutionPeriods) {
            periods.add(new LabelValueBean(period.getExecutionYear().getYear() + " - " + period.getName(), period.getExternalId()
                    .toString()));
        }

        request.setAttribute("periods", periods);
    }

    private Map<CurricularCourse, Integer> buildMapWithCurricularCoursesAndNumberOfMarkSheets(
            Collection<MarkSheet> webMarkSheetsNotPrinted) {
        final Map<CurricularCourse, Integer> result = new TreeMap<CurricularCourse, Integer>(new Comparator<CurricularCourse>() {
            @Override
            public int compare(CurricularCourse o1, CurricularCourse o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (final MarkSheet markSheet : webMarkSheetsNotPrinted) {
            Integer markSheetNumber = result.get(markSheet.getCurricularCourse());
            result.put(markSheet.getCurricularCourse(),
                    (markSheetNumber == null) ? Integer.valueOf(1) : Integer.valueOf(markSheetNumber.intValue() + 1));
        }
        return result;
    }

    public ActionForward printMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        if (form.get("msID") != null) {
            form.set("markSheet", form.get("msID"));
        }
        if (form.get("markSheet") == null || form.getString("markSheet").length() == 0) {
            form.set("markSheet", request.getParameter("markSheet"));
        }
        String markSheet = form.getString("markSheet");
        if (markSheet.equals("all")) {
            return printWebMarkSheets(mapping, actionForm, request, response);
        } else {
            return printMarkSheet(mapping, actionForm, request, response);
        }
    }

    private ActionForward printMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        String markSheetString = form.getString("markSheet");
        MarkSheet markSheet = getDomainObject(form, "markSheet");
        ActionMessages actionMessages = new ActionMessages();

        try (ServletOutputStream writer = response.getOutputStream()) {
            final SignedMarkSheet signedMarkSheet = markSheet.getSignedMarkSheet();
            final byte[] data;
            final String filename;
            if (signedMarkSheet == null) {
                MarkSheetDocument document = new MarkSheetDocument(markSheet);
                data = ReportsUtils.generateReport(document).getData();
                filename = document.getReportFileName();
            } else {
                data = signedMarkSheet.getContent();
                filename = signedMarkSheet.getFilename();
            }
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", filename));
            writer.write(data);
            markSheet.markAsPrinted();
            return null;
        } catch (Exception e) {
            request.setAttribute("markSheet", markSheetString);
            addMessage(request, actionMessages, e.getMessage());
            return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response);
        }
    }

    private ActionForward printWebMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final ActionMessages actionMessages = new ActionMessages();

        try (ServletOutputStream writer = response.getOutputStream()) {
            Collection<MarkSheet> markSheets =
                    getExecutionSemester(form).getWebMarkSheetsNotPrinted(AccessControl.getPerson(),
                            getDegreeCurricularPlan(form));
            List<MarkSheetDocument> reports = markSheets.stream()
                    .filter(markSheet -> markSheet.getSignedMarkSheet() == null)
                    .map(MarkSheetDocument::new).collect(Collectors.toList());
            final Stream<byte[]> signedData = markSheets.stream()
                    .map(markSheet -> markSheet.getSignedMarkSheet())
                    .filter(signedMarkSheet -> signedMarkSheet != null)
                    .map(signedMarkSheet -> signedMarkSheet.getContent());
            final byte[] reportData = reports.isEmpty() ? null : ReportsUtils.generateReport(reports.toArray(new MarkSheetDocument[0])).getData();
            final byte[] data = merge(Stream.concat(Stream.of(reportData), signedData));

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition",
                    String.format("attachment; filename=%s.pdf", "marksheets-" + new DateTime().toString()));
            writer.write(data);
            markAsPrinted(markSheets);
            return null;
        } catch (Exception e) {
            addMessage(request, actionMessages, e.getMessage());
            return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response);
        }
    }

    private byte[] merge(final Stream<byte[]> data) {
        final List<byte[]> list = data.collect(Collectors.toList());
        return list.size() == 0 ? null : list.size() == 1 ? list.get(0) : mergePdfFiles(list);
    }

    static byte[] mergePdfFiles(final List<byte[]> inputList) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final Document document = new Document();
        final List<PdfReader> readers = new ArrayList<PdfReader>();

        try {
            for (final byte[] data : inputList) {
                final ByteArrayInputStream pdf = new ByteArrayInputStream(data);
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
            }

            final PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            final PdfContentByte pageContentByte = writer.getDirectContent();

            PdfImportedPage pdfImportedPage;
            for (final PdfReader pdfReader : readers) {
                for (int currentPdfReaderPage = 1; currentPdfReaderPage <= pdfReader.getNumberOfPages(); currentPdfReaderPage++) {
                    document.newPage();
                    pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                    pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                }
            }
        } catch (final IOException | DocumentException ex) {
            throw new Error(ex);
        }
        document.close();
        return outputStream.toByteArray();
    }

    @Atomic(mode = TxMode.WRITE)
    private void markAsPrinted(Collection<MarkSheet> markSheets) {
        markSheets.forEach(MarkSheet::markAsPrinted);
    }

}