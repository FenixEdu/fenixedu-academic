package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
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

@Mapping(path = "/courseLoadOverview", module = "academicAdministration")
@Forwards({ @Forward(name = "viewInconsistencies", path = "/academicAdministration/courseLoadOverview/viewInconsistencies.jsp") })
public class CourseLoadOverviewDA extends FenixDispatchAction {

    public ActionForward viewInconsistencies(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        CourseLoadOverviewBean bean = getRenderedObject("courseLoadOverviewBean");
        if (bean == null) {
            bean = new CourseLoadOverviewBean();
        }
        request.setAttribute("courseLoadOverviewBean", bean);

        RenderUtils.invalidateViewState();

        return mapping.findForward("viewInconsistencies");
    }

    public ActionForward downloadInconsistencies(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterOid");
        final CourseLoadOverviewBean bean = new CourseLoadOverviewBean(executionSemester);
        final StyledExcelSpreadsheet spreadsheet = bean.getInconsistencySpreadsheet();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader(
                "Content-disposition",
                "attachment; filename="
                        + BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice",
                                "label.course.load.inconsistency.filename") + ".xls");

        try {
            final ServletOutputStream writer = response.getOutputStream();
            spreadsheet.getWorkbook().write(writer);
            writer.close();
        } catch (final IOException e) {
            throw new Error(e);
        }

        return null;
    }

}
