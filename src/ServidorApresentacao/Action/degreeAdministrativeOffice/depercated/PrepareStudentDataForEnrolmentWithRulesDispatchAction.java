package ServidorApresentacao.Action.degreeAdministrativeOffice.depercated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.degreeAdministrativeOffice.PrepareStudentDataDispatchAction;

/**
 * @author David Santos
 */

public class PrepareStudentDataForEnrolmentWithRulesDispatchAction extends PrepareStudentDataDispatchAction {
	
	private final String[] forwards = { "startCurricularCourseEnrolmentWithRules", "error" };

	public ActionForward getStudentAndDegreeTypeForEnrolmentWithRules(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isSuccess = super.getStudentByNumberAndDegreeType(form, request);

		if(isSuccess) {
			return mapping.findForward(forwards[0]);
		} else {
			return mapping.getInputForward();
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(forwards[1]);
	}
}