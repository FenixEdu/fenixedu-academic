/*
 * Created on 29/Mai/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoCredits;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class TeacherCreditsDispatchAction extends DispatchAction {
	
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession();
		
		InfoTeacher infoTeacher = (InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);
		IUserView userView = SessionUtils.getUserView(request);
		
		Object[] args = {infoTeacher};
		InfoCredits infoCredits = (InfoCredits) ServiceUtils.executeService(userView, "ReadCreditsTeacher", args);
		
		if (infoCredits != null){
			DynaActionForm creditsTeacherForm = (DynaActionForm) form;
			creditsTeacherForm.set("tfcStudentsNumber", infoCredits.getTfcStudentsNumber());
		}
		return mapping.findForward("showForm");
	}

	public ActionForward processForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession();
		InfoTeacher infoTeacher = (InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);
		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm creditsTeacherForm = (DynaActionForm) form;
		Integer tfcTfcStudentsNumber = (Integer) creditsTeacherForm.get("tfcStudentsNumber");
		
		Object[] args = {infoTeacher, tfcTfcStudentsNumber};
		
		ServiceUtils.executeService(userView, "WriteCreditsTeacher", args);
			
		return mapping.findForward("showForm");
	}

}
