/**
 * 
 * Project sop 
 * Package ServidorApresentacao.Action.publico 
 * Created on 1/Fev/2003
 */
package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSiteTimetable;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author João Mota
 *
 */
public class ViewClassTimeTableAction extends Action {

	/**
	 * Constructor for ViewClassTimeTableAction.
	 */

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(true);
		String className = request.getParameter("className");
		String degreeInitials = (String) request.getAttribute("degreeInitials");
		String nameDegreeCurricularPlan =
			(String) request.getAttribute("nameDegreeCurricularPlan");
		String year = request.getParameter("eYName");
		String period = request.getParameter("ePName");

		if (period == null) {
			period = (String) request.getAttribute("ePName");
		}
		if (year == null) {
			year = (String) request.getAttribute("eYName");
		}
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
				year,
				period,
				degreeInitials,
				nameDegreeCurricularPlan,
				className, null };
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
		request.setAttribute("className",className);
		request.setAttribute("ePName", period);
		request.setAttribute("eYName", year);
		return mapping.findForward("Sucess");
	}
}
