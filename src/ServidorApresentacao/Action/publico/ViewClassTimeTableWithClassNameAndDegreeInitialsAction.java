/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 24/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 *
 */
public class ViewClassTimeTableWithClassNameAndDegreeInitialsAction extends Action {

	/**
	 * Constructor for ViewShiftTimeTableWithClassNameAndDegreeInitialsAction.
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		
		SessionUtils.validSessionVerification(request, mapping);
			
		HttpSession session = request.getSession(false);
		session.removeAttribute(SessionConstants.INFO_SECTION);
		String degreeInitials = request.getParameter("degreeInitials");
		String nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");


		if (degreeInitials == null)
			return mapping.getInputForward();
			

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);


		Object[] args = { infoExecutionPeriod.getInfoExecutionYear(),
						  degreeInitials,
						  nameDegreeCurricularPlan };
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) ServiceUtils.executeService(
				null,
				"ReadExecutionDegreesByExecutionYearAndDegreeInitials",
				args);
				
		session.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);

		return mapping.findForward("Sucess");
	}
}
