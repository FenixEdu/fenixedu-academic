/*
 * Created on 2003/07/28
 *
 */
package ServidorApresentacao.Action.utils;

import javax.servlet.http.HttpServletRequest;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class ContextUtils {

	public static final void setExecutionPeriodContext(HttpServletRequest request) {
		String executionPeriodOIDString = (String) request.getAttribute("executionPeriodOID");
		if (executionPeriodOIDString == null) {
			executionPeriodOIDString = request.getParameter("executionPeriodOID");
		}
		Integer executionPeriodOID = null;		
		if (executionPeriodOIDString != null) {
			executionPeriodOID =
				new Integer(executionPeriodOIDString);
		}
		
		InfoExecutionPeriod infoExecutionPeriod = null;

		if (executionPeriodOID != null) {
			// Read from database
			try {
				Object[] args = { executionPeriodOID };
				infoExecutionPeriod =
					(InfoExecutionPeriod) ServiceUtils.executeService(
						null,
						"ReadExecutionPeriodByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}
		} else {
			// Read currente execution period from database
			try {
				infoExecutionPeriod =
					(InfoExecutionPeriod) ServiceUtils.executeService(
						null,
						"ReadCurrentExecutionPeriod",
						new Object[0]);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}
		}
		// Place it in request
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
	}

}
