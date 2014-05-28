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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.UnableToPrintServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/printMarkSheet", module = "academicAdministration", formBean = "printMarkSheetForm",
        input = "/printMarkSheet.do?method=choosePrinterMarkSheet&amp;page=0",
        functionality = MarkSheetSearchDispatchAction.class)
@Forwards({
        @Forward(name = "choosePrinterMarkSheet", path = "/academicAdministration/gradeSubmission/choosePrinterMarkSheet_bd.jsp"),
        @Forward(name = "choosePrinterMarkSheetsWeb",
                path = "/academicAdministration/gradeSubmission/choosePrinterMarkSheetsWeb_bd.jsp"),
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/markSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "searchMarkSheet", path = "/academicAdministration/markSheetManagement.do?method=prepareSearchMarkSheet") })
public class PrintMarkSheetDispatchAction extends MarkSheetDispatchAction {

    public ActionForward searchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("searchMarkSheet");
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
        setPrinterNames(request);

        final Employee employee = AccessControl.getPerson().getEmployee();

        final Collection<MarkSheet> webMarkSheetsNotPrinted =
                executionSemester.getWebMarkSheetsNotPrinted(AccessControl.getPerson(), degreeCurricularPlan);

        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("curricularCourseMap", buildMapWithCurricularCoursesAndNumberOfMarkSheets(webMarkSheetsNotPrinted));
        request.setAttribute("totalMarkSheetsCount", webMarkSheetsNotPrinted.size());

        buildPeriods(request);
        buildDegreeCurricularPlans(request, employee, executionSemester);

        form.set("ecID", executionSemester.getExternalId().toString());
        if (degreeCurricularPlan != null) {
            form.set("dcpID", degreeCurricularPlan.getExternalId().toString());
        }

        return mapping.findForward("choosePrinterMarkSheetsWeb");
    }

    private void buildDegreeCurricularPlans(HttpServletRequest request, Employee employee, ExecutionSemester semester) {

        final List<DegreeCurricularPlan> dcps =
                new ArrayList<DegreeCurricularPlan>(semester.getExecutionYear().getDegreeCurricularPlans());
        Collections.sort(dcps, DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME);

        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        Set<Degree> degreesForMarksheets =
                AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_MARKSHEETS);

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

    private void setPrinterNames(HttpServletRequest request) {
        String[] printerNames =
                AccessControl.getPerson().getEmployee().getCurrentWorkingPlace().getPrinterNamesByFunctionalityName("markSheet");
        request.setAttribute("printerNames", Arrays.asList(printerNames));
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

    public ActionForward choosePrinterMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        setPrinterNames(request);
        if (form.get("markSheet") == null || form.getString("markSheet").length() == 0) {
            form.set("markSheet", request.getAttribute("markSheet"));
        }
        return mapping.findForward("choosePrinterMarkSheet");
    }

    public ActionForward printMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
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
        String printerName = form.getString("printerName");
        String markSheetString = form.getString("markSheet");
        MarkSheet markSheet = getDomainObject(form, "markSheet");
        ActionMessages actionMessages = new ActionMessages();

        try {
            MarkSheet.printMarksheet(markSheet, printerName);
        } catch (UnableToPrintServiceException e) {
            request.setAttribute("markSheet", markSheetString);
            addMessage(request, actionMessages, e.getMessage());
            return choosePrinterMarkSheet(mapping, actionForm, request, response);
        }
        return mapping.findForward("searchMarkSheetFilled");
    }

    private ActionForward printWebMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String printerName = form.getString("printerName");

        final ActionMessages actionMessages = new ActionMessages();

        try {
            MarkSheet.printMarksheets(
                    getExecutionSemester(form).getWebMarkSheetsNotPrinted(AccessControl.getPerson(),
                            getDegreeCurricularPlan(form)), AccessControl.getPerson(), printerName);
        } catch (UnableToPrintServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
            return choosePrinterMarkSheetsWeb(mapping, actionForm, request, response);
        }
        return mapping.getInputForward();
    }

}
