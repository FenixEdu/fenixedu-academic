package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 *
 */

public class SetStartModeDispatchAction extends DispatchAction {

	public ActionForward withRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		session.setAttribute(SessionConstants.ENROLMENT_MODE_KEY, "withRules");
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
		return mapping.findForward("chooseStudentAndTypeDegree");
	}

	public ActionForward withoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		session.setAttribute(SessionConstants.ENROLMENT_MODE_KEY, "withoutRules");
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
		return mapping.findForward("chooseStudentAndTypeDegree");
	}
}