package ServidorApresentacao.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 */

public class PrepareStudentDataForManualEquivalenceDispatchAction extends PrepareStudentDataAction {
	
	private final String[] forwards = { "startManualEquivalence", "error" };

	public ActionForward getStudentAndDegreeTypeForManualEquivalence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoStudent infoStudent = null;

		Integer degreeTypeInt = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
		Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));

		try {
			Object args[] = { degreeTypeInt, studentNumber };
			infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByNumberAndDegreeType", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if(infoStudent != null) {
			request.getSession().removeAttribute(SessionConstants.DEGREE_TYPE);
			request.setAttribute(SessionConstants.STUDENT, infoStudent);
			return mapping.findForward(forwards[0]);
		} else {
			super.setNoStudentError(studentNumber, request);
			return mapping.getInputForward();
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[1]);
	}
}