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
		System.out.println("## setExecutionPeriodContext - IN");
		System.out.println("## executionPeriodOID in parameter - "+request.getParameter(SessionConstants.EXECUTION_PERIOD_OID));
		System.out.println("## executionPeriodOID in attribute - "+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID));
		
		String executionPeriodOIDString =
			(String) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD_OID);
		if (executionPeriodOIDString == null) {
			executionPeriodOIDString =
				request.getParameter(SessionConstants.EXECUTION_PERIOD_OID);
		}
		
		Integer executionPeriodOID = null;
		if (executionPeriodOIDString != null) {
			executionPeriodOID = new Integer(executionPeriodOIDString);
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
			System.out.println("## executionPeriodOID nao em lado nenhum, vai ler current");				
			// Read current execution period from database
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
		request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
		System.out.println("## setExecutionPeriodContext - OUT");			
	}

}