/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMap extends DispatchAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			HttpSession session = request.getSession(false);

			if (session != null) {
				GestorServicos gestor = GestorServicos.manager();
				IUserView userView =
					(IUserView) session.getAttribute(SessionConstants.U_VIEW);
				
				List curricularYears = (List) session.getAttribute(SessionConstants.CURRICULAR_YEARS_LIST);

				InfoExecutionPeriod infoExecutionPeriod =
					(InfoExecutionPeriod) session.getAttribute(
						SessionConstants.INFO_EXECUTION_PERIOD_KEY);
				InfoExecutionDegree infoExecutionDegree =
					(InfoExecutionDegree) session.getAttribute(
						SessionConstants.INFO_EXECUTION_DEGREE_KEY);

				Object[] args = { infoExecutionDegree, curricularYears, infoExecutionPeriod };
				InfoExamsMap infoExamsMap =
					(InfoExamsMap) gestor.executar(userView, "ReadExamsMap", args);

				session.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
			} else {
				throw new Exception();
			}

		return mapping.findForward("viewExamsMap");		
	}

}
