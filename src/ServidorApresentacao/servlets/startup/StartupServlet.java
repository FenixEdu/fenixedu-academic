package ServidorApresentacao.servlets.startup;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import DataBeans.InfoExecutionPeriod;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 *   17/Fev/2003
 *   @author     jpvl
 */
public class StartupServlet extends HttpServlet {

	/**
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod)ServiceUtils.executeService(null, "ReadCurrentExecutionPeriod", null);
			config.getServletContext().setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
		} catch (Throwable e) {
			e.printStackTrace(System.out);
			throw new ServletException("Error reading actual execution period!",e);
		}
	}

}
