package ServidorApresentacao.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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

public class GetStudentByNumberAndDegreeTypeDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithRules", "startCurricularCourseEnrolmentWithoutRules" };
//	private final String[] modes = { "withRules", "withoutRules" };

	public ActionForward withRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		doIt(mapping, form, request, response);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward withoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		doIt(mapping, form, request, response);
		return mapping.findForward(forwards[1]);
	}

	public void doIt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm getStudentByNumberAndDegreeTypeForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeType = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("degreeType"));
		Integer studentNumber = new Integer((String) getStudentByNumberAndDegreeTypeForm.get("studentNumber"));
		Object args[] = { degreeType, studentNumber };

		IUserView actor = null;
		try {
			actor = (IUserView) ServiceUtils.executeService(userView, "GetUserViewFromStudentNumberAndDegreeType", args);
			// TODO DAVID-RICARDO: Fazer qq coisa se o actor não existir, isto é, se o aluno que se quer inscrever não se encontrar na base de dados.
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
//		String mode = (String) session.getAttribute(SessionConstants.ENROLMENT_MODE_KEY);
//		String forward = null;
//		if(mode.equals(modes[0])) {
//			forward = new String(forwards[0]);
//		} else if(mode.equals(modes[1])) {
//			forward = new String(forwards[1]);
//		}

		// FIXME DAVID-RICARDO: Devido a remover este atributo, se for feito um refresh á página e o código desta
		// action for executado sem executar o código da action que coloca este atributo na sessão, vai dar uma excepção
		// (java.lang.NullPointerException) no if acima.
//		session.removeAttribute(SessionConstants.ENROLMENT_MODE_KEY);
		session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, actor);
	}
}