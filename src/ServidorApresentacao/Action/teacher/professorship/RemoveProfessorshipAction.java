/*
 * Created on Dec 11, 2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher.professorship;

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
public class RemoveProfessorshipAction extends Action {

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
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;

        Integer teacherId = Integer.valueOf((String) teacherExecutionCourseForm.get("teacherId"));
        Integer executionCourseId = Integer.valueOf((String) teacherExecutionCourseForm
                .get("executionCourseId"));

        IUserView userView = SessionUtils.getUserView(request);
        Object[] arguments = { executionCourseId, teacherId };
        ServiceUtils.executeService(userView, "RemoveProfessorshipByDepartment", arguments);
        return mapping.findForward("successfull-delete");
    }
}