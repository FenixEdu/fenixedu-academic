/*
 * Created on 24/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesAction extends FenixAction
{

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);

		Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
		List executionCourses = null;
		Object args[] = { executionPeriodId };
		try
		{
			executionCourses =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionCoursesByExecutionPeriod",
					args);

			if (executionCourses != null && executionCourses.size() > 0)
			{
				Collections.sort(executionCourses, new BeanComparator("nome"));
				InfoExecutionPeriod infoExecutionPeriod =
					((InfoExecutionCourse) executionCourses.get(0)).getInfoExecutionPeriod();

				String executionPeriodNameAndYear =
					new String(
						infoExecutionPeriod.getName()
							+ "-"
							+ infoExecutionPeriod.getInfoExecutionYear().getYear());
				request.setAttribute("executionPeriodNameAndYear", executionPeriodNameAndYear);
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("infoExecutionCoursesList", executionCourses);

		//		InfoExecutionPeriod infoExecutionPeriod = null;
		//		
		//		try
		//		{
		//			infoExecutionPeriod =
		//				(InfoExecutionPeriod) ServiceUtils.executeService(
		//					userView,
		//					"ReadExecutionPeriodForExecutionCourseAssociation",
		//					args);
		//
		//		}
		//		catch (NonExistingServiceException e)
		//		{
		//			throw new NonExistingActionException(
		//				e.getMessage(),
		//				mapping.findForward("readAvailableExecutionPeriods"));
		//		}
		//		catch (FenixServiceException fenixServiceException)
		//		{
		//			throw new FenixActionException(fenixServiceException);
		//		}
		//		String ExecutionPeriodNameAndYear =
		//			new String(
		//				infoExecutionPeriod.getName()
		//					+ "-"
		//					+ infoExecutionPeriod.getInfoExecutionYear().getYear());

		return mapping.findForward("readExecutionCourses");
	}
}