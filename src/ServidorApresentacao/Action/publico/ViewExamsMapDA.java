/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteExamMap;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDA extends FenixContextDispatchAction {

	public ActionForward view(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(true);

		if (session != null) {
			List curricularYears =
				(List) request.getAttribute("curricularYearList");

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.EXECUTION_PERIOD);

			String degreeInitials =
				(String) request.getAttribute("degreeInitials");
			String nameDegreeCurricularPlan =
				(String) request.getAttribute("nameDegreeCurricularPlan");

			if (degreeInitials == null) {
				degreeInitials = request.getParameter("degreeInitials");
			}
			if (nameDegreeCurricularPlan == null) {
				nameDegreeCurricularPlan =
					request.getParameter("nameDegreeCurricularPlan");
			}
			SiteView siteView = null;
			ISiteComponent bodyComponent = new InfoSiteExamMap();
			Object[] args =
				{
					bodyComponent,
					infoExecutionPeriod.getInfoExecutionYear().getYear(),
					infoExecutionPeriod.getName(),
					degreeInitials,
					nameDegreeCurricularPlan,
					curricularYears };

			try {
				siteView =
					(SiteView) ServiceManagerServiceFactory.executeService(
						null,
						"ExamSiteComponentService",
						args);
			} catch (FenixServiceException e1) {
				throw new FenixActionException(e1);
			}

			request.setAttribute("siteView", siteView);
		} else {
			throw new FenixActionException();
		}

		return mapping.findForward("viewExamsMap");
	}

}
