/*
 * Created on 2003/10/04
 *
 */
package ServidorApresentacao.Action.sop.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.sop.utils.RequestContextUtil;

/**
 * @author Luis Cruz
 */
public abstract class FenixDateAndTimeCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction
	extends FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
	/**
	 * Tests if the session is valid.
	 * @see SessionUtils#validSessionVerification(HttpServletRequest, ActionMapping)
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		RequestContextUtil.setExamDateAndTimeContext(request);

		ActionForward actionForward =
			super.execute(mapping, actionForm, request, response);

		return actionForward;
	}

}
