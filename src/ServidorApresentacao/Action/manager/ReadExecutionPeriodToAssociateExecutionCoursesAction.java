/*
 * Created on 8/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.comparators.ExecutionPeriodComparator;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class ReadExecutionPeriodToAssociateExecutionCoursesAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		try {
			List infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);

			if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

				Collections.sort(infoExecutionPeriods, new ExecutionPeriodComparator());

				if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
					request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, infoExecutionPeriods);
				}

			}
		} catch (FenixServiceException ex) {
			throw new FenixActionException("Problemas de comunicação com a base de dados.", ex);
		}

		return mapping.findForward("viewExecutionPeriods");
	}
}
