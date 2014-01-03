package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/specialSeason/specialSeasonStatusTracker", module = "manager")
@Forwards({ @Forward(name = "selectCourse", path = "/manager/specialSeason/selectCourse.jsp"),
        @Forward(name = "listStudents", path = "/manager/specialSeason/listStudents.jsp") })
public class SpecialSeasonStatusTrackerDA extends FenixDispatchAction {

    public ActionForward selectCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        if (bean == null) {
            bean = new SpecialSeasonStatusTrackerBean();
        }
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("selectCourse");
    }

    public ActionForward updateDepartmentSelection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("selectCourse");
    }

    public ActionForward listStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        bean.clearEntries();
        List<Enrolment> enrolments = new ArrayList<Enrolment>();
        List<CompetenceCourse> courses = new ArrayList<CompetenceCourse>();
        List<Department> departments = new ArrayList<Department>();
        if (getDepartment() == null) {
            departments.addAll(Bennu.getInstance().getDepartmentsSet());
        } else {
            departments.add(getDepartment());
        }
        for (Department department : departments) {
            if (bean.getCompetenceCourse() == null) {
                courses.addAll(department.getBolonhaCompetenceCourses());
            } else {
                courses.add(bean.getCompetenceCourse());
            }
            for (CompetenceCourse competence : courses) {
                for (CurricularCourse course : competence.getAssociatedCurricularCourses()) {
                    for (Enrolment enrolment : course.getActiveEnrollments(bean.getExecutionSemester())) {
                        if (enrolment.isSpecialSeason()) {
                            enrolments.add(enrolment);
                            bean.addEntry(enrolment.getRegistration().getNumber(), enrolment.getRegistration().getPerson()
                                    .getName(), enrolment.getRegistration().getDegree().getSigla(), enrolment
                                    .getCurricularCourse().getName(bean.getExecutionSemester()));
                        }
                    }
                }
            }
            courses.clear();
        }
        bean.setEnrolments(enrolments);
        Collections.sort(bean.getEntries(), SpecialSeasonStatusTrackerRegisterBean.COMPARATOR_STUDENT_NUMBER);
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("listStudents");
    }

    public ActionForward exportXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        final Spreadsheet spreadsheet = generateSpreadsheet(bean);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getFilename(bean) + ".xls");
        spreadsheet.exportToXLSSheet(response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
        return null;
    }

    private String getFilename(SpecialSeasonStatusTrackerBean bean) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale()).getString(
                "special.season.filename"));
        if (bean.getCompetenceCourse() != null) {
            strBuilder.append("_");
            strBuilder.append(bean.getCompetenceCourse().getAcronym());
        } else if (getDepartment() != null) {
            strBuilder.append("_");
            strBuilder.append(bean.getDepartment().getAcronym());
        }
        strBuilder.append("_");
        strBuilder.append(bean.getExecutionSemester().getSemester());
        strBuilder.append("_");
        strBuilder.append(ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale()).getString(
                "special.season.semester"));
        strBuilder.append("_");
        strBuilder.append(bean.getExecutionSemester().getExecutionYear().getName());
        return strBuilder.toString();
    }

    private Spreadsheet generateSpreadsheet(SpecialSeasonStatusTrackerBean bean) {
        final Spreadsheet spreadsheet = createSpreadSheet();
        for (final Enrolment enrolment : bean.getEnrolments()) {
            final Row row = spreadsheet.addRow();

            row.setCell(enrolment.getRegistration().getPerson().getUsername());
            row.setCell(enrolment.getRegistration().getNumber());
            row.setCell(enrolment.getRegistration().getPerson().getName());
            row.setCell(enrolment.getRegistration().getPerson().getInstitutionalOrDefaultEmailAddressValue());
            row.setCell(enrolment.getRegistration().getDegree().getSigla());
            row.setCell(enrolment.getRegistration().getStudentCurricularPlan(bean.getExecutionSemester()).getName());
            row.setCell(enrolment.getCurricularCourse().getAcronym());
            row.setCell(enrolment.getCurricularCourse().getName());
        }

        return spreadsheet;
    }

    private Spreadsheet createSpreadSheet() {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
        final Spreadsheet spreadsheet = new Spreadsheet(bundle.getString("list.students"));

        spreadsheet.setHeaders(new String[] {

        bundle.getString("label.istid"),

        bundle.getString("label.number"),

        bundle.getString("label.name"),

        bundle.getString("label.email"),

        bundle.getString("label.Degree"),

        bundle.getString("label.curricularPlan"),

        bundle.getString("label.curricular.course.name"),

        bundle.getString("label.curricular.course.name"),

        " ", " " });

        return spreadsheet;
    }

    protected Department getDepartment() {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        if (bean != null && bean.getDepartment() != null) {
            return bean.getDepartment();
        }
        return null;
    }

}
