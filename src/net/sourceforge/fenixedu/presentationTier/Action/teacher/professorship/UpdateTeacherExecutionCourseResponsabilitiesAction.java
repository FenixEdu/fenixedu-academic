/*
 * Created on Dec 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author jpvl
 */
public class UpdateTeacherExecutionCourseResponsabilitiesAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaActionForm teacherExecutionYearResponsabilitiesForm = (DynaActionForm) form;
	Integer[] executionCourseResponsabilities = (Integer[]) teacherExecutionYearResponsabilitiesForm
		.get("executionCourseResponsability");

	Integer teacherId = (Integer) teacherExecutionYearResponsabilitiesForm.get("teacherId");
	Integer executionYearId = (Integer) teacherExecutionYearResponsabilitiesForm.get("executionYearId");
	Object args[] = { teacherId, executionYearId, Arrays.asList(executionCourseResponsabilities) };

	IUserView userView = UserView.getUser();
	ServiceUtils.executeService("UpdateTeacherExecutionYearResponsabilities", args);

	return mapping.findForward("successfull-update");
    }
}