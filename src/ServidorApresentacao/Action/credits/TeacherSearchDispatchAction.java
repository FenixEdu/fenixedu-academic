/*
 * Created on 30/Jun/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class TeacherSearchDispatchAction extends DispatchAction {
	
	public ActionForward searchByNumber(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm teacherSearchForm = (DynaActionForm) form;
		IUserView userView = SessionUtils.getUserView(request);
		
		InfoTeacher infoTeacher = null;
		try {
			Integer teacherNumber = new Integer((String) teacherSearchForm.get("teacherNumber"));	
			infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,"ReadTeacherByNumber",new Object [] {teacherNumber});
		} catch (NumberFormatException e) {
		}
		
		ActionForward actionForward = null;
		if (infoTeacher == null) {
			ActionErrors errors = new ActionErrors();
			ActionError actionError = new ActionError("error.teacher.not.found");
			errors.add("error.teacher.not.found",actionError);
			saveErrors(request, errors);
			actionForward = mapping.getInputForward();
		}else {
			request.setAttribute("infoTeacher", infoTeacher);
			actionForward = mapping.findForward("one");
		}
		return actionForward;
	}

}
