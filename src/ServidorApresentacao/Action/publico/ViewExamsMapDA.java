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
import DataBeans.InfoSiteExamMap;
import DataBeans.SiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDA extends FenixDispatchAction {

	public ActionForward view(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(true);

		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();

			List curricularYears =
				(List) request.getAttribute("curricularYearList");

			String executionPeriodName =
				(String) request.getAttribute("ePName");
			String executionYearName = (String) request.getAttribute("eYName");
						
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
				{ bodyComponent,
					 executionYearName,
					 executionPeriodName,
					 degreeInitials,
					 nameDegreeCurricularPlan,
					 curricularYears };
			
			try {
				siteView =
					(SiteView) gestor.executar(null, "ExamSiteComponentService", args);
			} catch (FenixServiceException e1) {
				throw new FenixActionException(e1);
			}

			request.setAttribute("siteView", siteView);
			request.setAttribute("ePName",executionPeriodName);
			request.setAttribute("eYName",executionYearName);
			
		} else {
			throw new FenixActionException();
		}

		return mapping.findForward("viewExamsMap");
	}

}
