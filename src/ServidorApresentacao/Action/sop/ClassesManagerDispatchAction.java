/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 3/Dez/2002
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

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 *
 * 
 */
public class ClassesManagerDispatchAction extends DispatchAction {
	static public final String CLASS_LIST_KEY = "classesList";

	public ActionForward listClasses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);
		
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		
		Integer curricularYear = (Integer) session.getAttribute(SessionConstants.CURRICULAR_YEAR_KEY);
		
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY);
		
		
		Object argsLerTurmas[] = { infoExecutionDegree, infoExecutionPeriod, curricularYear};

		List classesList = (List) ServiceUtils.executeService(SessionUtils.getUserView(request),"LerTurmas", argsLerTurmas);
		
		if (classesList != null && !classesList.isEmpty())
			request.setAttribute(CLASS_LIST_KEY, classesList);

		return mapping.findForward("listClasses");
	}

}
