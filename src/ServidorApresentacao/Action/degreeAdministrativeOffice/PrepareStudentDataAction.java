package ServidorApresentacao.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 */

public class PrepareStudentDataAction extends DispatchAction {

	public boolean getUserViewFromStudentNumberAndDegreeType(ActionForm form, HttpServletRequest request) throws Exception {

		boolean result = false;

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
		Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));

		IUserView actor = null;

		Object args[] = { degreeType, studentNumber };
		try {
			actor = (IUserView) ServiceUtils.executeService(userView, "GetUserViewFromStudentNumberAndDegreeType", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if(actor == null) {
			this.setNoStudentError(studentNumber, request);
			result = false;
		} else {
			session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, actor);
			result = true;
		}
		
		return result;
	}

	public void setNoStudentError(Integer studentNumber, HttpServletRequest request) {
		ActionErrors actionErrors = new ActionErrors();
		ActionError actionError = new ActionError("error.no.student.in.database", studentNumber.toString());
		actionErrors.add("error.no.student.in.database", actionError);
		saveErrors(request, actionErrors);
	}
}