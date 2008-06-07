/*
 * Created on Dec 11, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author jpvl
 */
public class RemoveProfessorshipAction extends Action {


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;

        Integer teacherId = Integer.valueOf((String) teacherExecutionCourseForm.get("teacherId"));
        Integer executionCourseId = Integer.valueOf((String) teacherExecutionCourseForm
                .get("executionCourseId"));

        IUserView userView = UserView.getUser();
        Object[] arguments = { executionCourseId, teacherId };
        try {
            ServiceUtils.executeService("RemoveProfessorshipByDepartment", arguments);            
        } catch (DomainException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError(e.getMessage()));
            saveErrors(request, errors);
        }
        
        return mapping.findForward("successfull-delete");
    }
}