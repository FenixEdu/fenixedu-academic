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
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

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
		
		
			
		HttpSession session = request.getSession(true);		
		String degreeInitials = request.getParameter("degreeInitials");
		String nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");


		if (degreeInitials == null)
			return mapping.getInputForward();
			
		InfoExecutionPeriod infoExecutionPeriod = RequestUtils.getExecutionPeriodFromRequest(request);
			


		Object[] args = { infoExecutionPeriod.getInfoExecutionYear(),
						  degreeInitials,
						  nameDegreeCurricularPlan };
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) ServiceUtils.executeService(
				null,
				"ReadExecutionDegreesByExecutionYearAndDegreeInitials",
				args);
				
		request.setAttribute("exeDegree", infoExecutionDegree);
		RequestUtils.setExecutionPeriodToRequest(request,infoExecutionPeriod);

		return mapping.findForward("Sucess");
	}
}
