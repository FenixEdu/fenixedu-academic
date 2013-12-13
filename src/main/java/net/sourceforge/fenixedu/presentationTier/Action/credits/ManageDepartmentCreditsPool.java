package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean.DepartmentExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@Forwards(value = { @Forward(name = "manageUnitCredits", path = "/credits/creditsPool/manageUnitCredits.jsp",
        tileProperties = @Tile(title = "private.department.coursestypes")) })
public class ManageDepartmentCreditsPool extends FenixDispatchAction {

    public ActionForward prepareManageUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("manageUnitCredits");
    }

    protected DepartmentCreditsBean getDepartmentCreditsBean() {
        User userView = Authenticate.getUser();
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(userView.getPerson()
                .getManageableDepartmentCredits()));
        return departmentCreditsBean;
    }

    public ActionForward viewDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        if (departmentCreditsBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        DepartmentCreditsPoolBean departmentCreditsPoolBean = new DepartmentCreditsPoolBean(departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }

    public ActionForward postBackUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsPoolBean departmentCreditsPoolBean = getRenderedObject("departmentCreditsPoolBean");
        if (departmentCreditsPoolBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        departmentCreditsBean.setDepartment(departmentCreditsPoolBean.getDepartment());
        departmentCreditsBean.setExecutionYear(departmentCreditsPoolBean.getAnnualCreditsState().getExecutionYear());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }

    public ActionForward editUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsPoolBean departmentCreditsPoolBean = getRenderedObject("departmentCreditsPoolBean");
        RenderUtils.invalidateViewState();
        if (departmentCreditsPoolBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        try {
            departmentCreditsPoolBean.editUnitCredits();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        departmentCreditsBean.setDepartment(departmentCreditsPoolBean.getDepartment());
        departmentCreditsBean.setExecutionYear(departmentCreditsPoolBean.getAnnualCreditsState().getExecutionYear());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }

    public ActionForward exportDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        RenderUtils.invalidateViewState();
        if (departmentCreditsBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        DepartmentCreditsPoolBean departmentCreditsPoolBean = new DepartmentCreditsPoolBean(departmentCreditsBean);

        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();

        getExecutionCoursesSheet(departmentCreditsPoolBean.getDepartmentSharedExecutionCourses(), spreadsheet,
                "Disciplinas_Partilhadas");
        getExecutionCoursesSheet(departmentCreditsPoolBean.getOtherDepartmentSharedExecutionCourses(), spreadsheet,
                "Disciplinas_Partilhadas_Outros_Dep");
        getExecutionCoursesSheet(departmentCreditsPoolBean.getDepartmentExecutionCourses(), spreadsheet, "Restantes_Disciplinas");

        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename="
                + departmentCreditsPoolBean.getDepartment().getAcronym() + ".xls");
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    protected void getExecutionCoursesSheet(Set<DepartmentExecutionCourse> executionCourses, StyledExcelSpreadsheet spreadsheet,
            String sheetname) {
        spreadsheet.getSheet(sheetname);
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", "label.course"),
                10000);
        spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", "label.degrees"));
        spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                "label.execution-period"));
        spreadsheet.addHeader(BundleUtil
                .getStringFromResourceBundle("resources.TeacherCreditsSheetResources", "label.effortRate"));
        spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                "label.departmentEffectiveLoad"));
        spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                "label.totalEffectiveLoad"));
        spreadsheet.addHeader(BundleUtil
                .getStringFromResourceBundle("resources.TeacherCreditsSheetResources", "label.unitCredit"));
        for (DepartmentExecutionCourse departmentExecutionCourse : executionCourses) {
            spreadsheet.newRow();
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getName());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getDegreePresentationString());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getExecutionPeriod().getSemester());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getEffortRate());
            spreadsheet.addCell(departmentExecutionCourse.getDepartmentEffectiveLoad());
            spreadsheet.addCell(departmentExecutionCourse.getTotalEffectiveLoad());
            spreadsheet.addCell(departmentExecutionCourse.getExecutionCourse().getUnitCreditValue());
        }
    }
}
