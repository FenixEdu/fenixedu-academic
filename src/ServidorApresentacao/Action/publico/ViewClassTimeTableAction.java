/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteTimetable;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 */
public class ViewClassTimeTableAction extends FenixContextAction {

	/**
	 * Constructor for ViewClassTimeTableAction.
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		try {
			super.execute(mapping, form, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String className = request.getParameter("className");
		String degreeInitials = (String) request.getAttribute("degreeInitials");
		String nameDegreeCurricularPlan =
			(String) request.getAttribute("nameDegreeCurricularPlan");

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		if (degreeInitials == null) {
			degreeInitials = request.getParameter("degreeInitials");
		}
		if (nameDegreeCurricularPlan == null) {
			nameDegreeCurricularPlan =
				request.getParameter("nameDegreeCurricularPlan");
		}
		if (className == null)
			return mapping.getInputForward();

		InfoSiteTimetable component = new InfoSiteTimetable();

		Object[] args =
			{
				component,
				infoExecutionPeriod.getInfoExecutionYear().getYear(),
				infoExecutionPeriod.getName(),
				degreeInitials,
				nameDegreeCurricularPlan,
				className,
				null };
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					null,
					"ClassSiteComponentService",
					args);
		} catch (FenixServiceException e1) {
			throw new FenixActionException(e1);
		}
		request.setAttribute("siteView", siteView);
		request.setAttribute("className", className);
		return mapping.findForward("Sucess");
	}
}
