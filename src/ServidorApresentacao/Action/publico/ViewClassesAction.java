package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoClass;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoSiteClasses;
import DataBeans.SiteView;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author João Mota
 */
public class ViewClassesAction extends FenixAction {

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(true);

		GestorServicos gestor = GestorServicos.manager();
		InfoClass infoClass = new InfoClass();

		String year = request.getParameter("eYName");
		String period = request.getParameter("ePName");

		if (period == null) {
			period = (String) request.getAttribute("ePName");
		}
		if (year == null) {
			year = (String) request.getAttribute("eYName");
		}

		String degreeInitials =
			(String) request.getAttribute("degreeInitials");
		String nameDegreeCurricularPlan =
			(String) request.getAttribute("nameDegreeCurricularPlan");
		Integer curricularYear = (Integer) request.getAttribute("curYear");
		
		ISiteComponent component = new InfoSiteClasses();
		SiteView siteView = null;
		Object[] args =
			{
				component,
				year,
				period,
				degreeInitials,
				nameDegreeCurricularPlan,
				null,
				curricularYear };
				
		InfoExecutionDegree infoExecutionDegree;
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
		request.setAttribute("ePName", period);
		request.setAttribute("eYName", year);
		request.setAttribute("degreeInitials", degreeInitials);
		request.setAttribute("nameDegreeCurricularPlan", nameDegreeCurricularPlan);
		return mapping.findForward("Sucess");

	}

}
