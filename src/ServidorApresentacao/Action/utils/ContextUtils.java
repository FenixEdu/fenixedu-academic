/*
 * Created on 2003/07/28
 *
 */
package ServidorApresentacao.Action.utils;

import javax.servlet.http.HttpServletRequest;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
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
		System.out.println("ExecutionPeriod from request: " + executionPeriodOIDString);
		if (executionPeriodOIDString == null) {
			executionPeriodOIDString =
				request.getParameter(SessionConstants.EXECUTION_PERIOD_OID);
			System.out.println("ExecutionPeriod from parameter: " + executionPeriodOIDString);
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

	/**
	 * @param request
	 */
	public static void setExecutionDegreeContext(HttpServletRequest request) {
		String executionDegreeOIDString =
			(String) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE_OID);
		System.out.println("ExecutionDegree from request: " + executionDegreeOIDString);
		if (executionDegreeOIDString == null) {
			executionDegreeOIDString =
				request.getParameter(SessionConstants.EXECUTION_DEGREE_OID);
			System.out.println("ExecutionDegree from parameter: " + executionDegreeOIDString);
		}

		Integer executionDegreeOID = null;
		if (executionDegreeOIDString != null) {
			executionDegreeOID = new Integer(executionDegreeOIDString);
		}

		InfoExecutionDegree infoExecutionDegree = null;

		if (executionDegreeOID != null) {
			// Read from database
			try {
				Object[] args = { executionDegreeOID };
				infoExecutionDegree =
					(InfoExecutionDegree) ServiceUtils.executeService(
						null,
						"ReadExecutionDegreeByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			// Place it in request
			request.setAttribute(
				SessionConstants.EXECUTION_DEGREE,
				infoExecutionDegree);
		}
	}

	/**
	 * @param request
	 */
	public static void setCurricularYearContext(HttpServletRequest request) {
		String curricularYearOIDString =
			(String) request.getAttribute(
				SessionConstants.CURRICULAR_YEAR_OID);
		System.out.println("Curricular Year from request: " + curricularYearOIDString);
		if (curricularYearOIDString == null) {
			curricularYearOIDString =
				request.getParameter(SessionConstants.CURRICULAR_YEAR_OID);
			System.out.println("Curricular Year from parameter: " + curricularYearOIDString);
		}

		Integer curricularYearOID = null;
		if (curricularYearOIDString != null) {
			curricularYearOID = new Integer(curricularYearOIDString);
		}

		InfoCurricularYear infoCurricularYear = null;

		if (curricularYearOID != null) {
			// Read from database
			try {
				Object[] args = { curricularYearOID };
				infoCurricularYear =
					(InfoCurricularYear) ServiceUtils.executeService(
						null,
						"ReadCurricularYearByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			// Place it in request
			request.setAttribute(
				SessionConstants.CURRICULAR_YEAR,
				infoCurricularYear);
		}
	}

}