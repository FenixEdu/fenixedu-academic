/*
 * Created on 30/Jun/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.credits;

import java.util.List;

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
			actionForward = getNoTeachersFoundForward(mapping, request);
		}else {
			request.setAttribute("infoTeacher", infoTeacher);
			actionForward = mapping.findForward("one");
		}
		return actionForward;
	}
	
	private ActionForward getNoTeachersFoundForward(ActionMapping mapping, HttpServletRequest request) {
		ActionForward actionForward;
		ActionErrors errors = new ActionErrors();
		ActionError actionError = new ActionError("error.teacher.not.found");
		errors.add("error.teacher.not.found",actionError);
		saveErrors(request, errors);
		actionForward = mapping.getInputForward();
		return actionForward;
	}

	public ActionForward searchByDepartment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		//DynaActionForm teacherSearchForm = (DynaActionForm) form;
//		IUserView userView = SessionUtils.getUserView(request);
//		
//		String departmentCode = (String) teacherSearchForm.get("departamentCode");
		
		List departmentTeachers = null;
		
//		TODO Executa o serviço


		ActionForward actionForward = null;
		if ((departmentTeachers == null) || (departmentTeachers.isEmpty())) {
			actionForward = getNoTeachersFoundForward(mapping, request);
		}else if (departmentTeachers.size() == 1) {
			request.setAttribute("infoTeacher", departmentTeachers.get(0));
			actionForward = mapping.findForward("one");
		}else  {
			request.setAttribute("infoTeacherList", departmentTeachers);
			actionForward = mapping.findForward("list");
		}
		
		return actionForward;
	}

}
