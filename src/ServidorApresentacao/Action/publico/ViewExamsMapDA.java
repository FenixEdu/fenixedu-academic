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

import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

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

			InfoExecutionPeriod infoExecutionPeriod =
				RequestUtils.getExecutionPeriodFromRequest(request);

			InfoExecutionDegree infoExecutionDegree =
				RequestUtils.getExecutionDegreeFromRequest(
					request,
					infoExecutionPeriod.getInfoExecutionYear());

			
			Object[] args =
				{ infoExecutionDegree, curricularYears, infoExecutionPeriod };
			InfoExamsMap infoExamsMap;
			try {
				infoExamsMap =
					(InfoExamsMap) gestor.executar(null, "ReadExamsMap", args);
			} catch (FenixServiceException e1) {
				throw new FenixActionException(e1);
			}

			request.setAttribute("infoExamsMap", infoExamsMap);
			RequestUtils.setExecutionPeriodToRequest(
				request,
				infoExecutionPeriod);

		} else {
			throw new FenixActionException();
		}

		return mapping.findForward("viewExamsMap");
	}

}
