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
		System.out.println("#####################################################");
			
		HttpSession session = request.getSession();
		
		System.out.println("1");
		
		String degreeInitials = request.getParameter("degreeInitials");
		String nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");

		System.out.println("2");

		if (degreeInitials == null)
			return mapping.getInputForward();
			
		System.out.println("3");

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);

		System.out.println("4");

		Object[] args = { infoExecutionPeriod.getInfoExecutionYear(),
						  degreeInitials,
						  nameDegreeCurricularPlan };
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) ServiceUtils.executeService(
				null,
				"ReadExecutionDegreesByExecutionYearAndDegreeInitials",
				args);
				
			System.out.println("5");

		session.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);

		System.out.println("Chegou ao Fim da Action nova... :o)");
		System.out.println("Vai passar para a outra Action");

		return mapping.findForward("Sucess");
	}
}
