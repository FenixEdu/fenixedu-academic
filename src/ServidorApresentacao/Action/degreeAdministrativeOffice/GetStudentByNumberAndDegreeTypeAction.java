package ServidorApresentacao.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 *
 */

public class GetStudentByNumberAndDegreeTypeAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

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
		
		String mode = (String) session.getAttribute(SessionConstants.ENROLMENT_MODE_KEY);
		String forward = null;
		if(mode.equals("withRules")) {
			forward = new String("startCurricularCourseEnrolmentWithRules");
		} else if(mode.equals("withoutRules")) {
			forward = new String("startCurricularCourseEnrolmentWithoutRules");
		}

		session.removeAttribute(SessionConstants.ENROLMENT_MODE_KEY);
		session.setAttribute(SessionConstants.ENROLMENT_ACTOR_KEY, actor);
		return mapping.findForward(forward);
	}
}