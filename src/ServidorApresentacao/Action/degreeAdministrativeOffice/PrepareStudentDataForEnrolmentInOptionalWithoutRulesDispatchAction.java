package ServidorApresentacao.Action.degreeAdministrativeOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 */

public class PrepareStudentDataForEnrolmentInOptionalWithoutRulesDispatchAction extends PrepareStudentDataAction {
	
	private final String[] forwards = { "startOptionalCurricularCourseEnrolmentWithoutRules", "error" };

	public ActionForward getStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isSuccess = super.getUserViewFromStudentNumberAndDegreeType(form, request);

		if(isSuccess) {
			request.getSession().removeAttribute(SessionConstants.DEGREE_TYPE);
			return mapping.findForward(forwards[0]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[1]);
	}
}