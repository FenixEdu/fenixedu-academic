package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 */

public class FunctionRedirectDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "chooseStudentAndDegreeTypeForEnrolmentWithRules", "chooseStudentAndDegreeTypeForEnrolmentWithoutRules", "chooseStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules", "chooseStudentAndDegreeTypeForManualEquivalence" };

	public ActionForward chooseStudentAndDegreeTypeForEnrolmentWithRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward chooseStudentAndDegreeTypeForEnrolmentWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
		return mapping.findForward(forwards[1]);
	}

	public ActionForward chooseStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
		return mapping.findForward(forwards[2]);
	}

	public ActionForward chooseStudentAndDegreeTypeForManualEquivalence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
		return mapping.findForward(forwards[3]);
	}
}