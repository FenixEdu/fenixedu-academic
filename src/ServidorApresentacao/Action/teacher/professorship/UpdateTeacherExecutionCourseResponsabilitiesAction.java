/*
 * Created on Dec 16, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.teacher.professorship;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class UpdateTeacherExecutionCourseResponsabilitiesAction extends Action {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm teacherExecutionYearResponsabilitiesForm = (DynaActionForm) form;
        Integer[] executionCourseResponsabilities = (Integer[]) teacherExecutionYearResponsabilitiesForm
                .get("executionCourseResponsability");

        Integer teacherId = (Integer) teacherExecutionYearResponsabilitiesForm.get("teacherId");
        Integer executionYearId = (Integer) teacherExecutionYearResponsabilitiesForm
                .get("executionYearId");
        Object args[] = { teacherId, executionYearId, Arrays.asList(executionCourseResponsabilities) };

        IUserView userView = SessionUtils.getUserView(request);
        ServiceUtils.executeService(userView, "UpdateTeacherExecutionYearResponsabilities", args);

        return mapping.findForward("successfull-update");
    }
}