package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseExecutionYearDispatchAction extends DispatchAction
{

	public ActionForward prepareChooseExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		HttpSession session = request.getSession(false);
		MessageResources messages = getResources(request);

		// Get Execution Year List
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		ArrayList executionYearList = null;
		try
		{
			executionYearList =
				(ArrayList) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadNotClosedExecutionYears",
					null);
		}
		catch (ExistingServiceException e)
		{
			throw new ExistingActionException(e);
		}

		if (request.getParameter("jspTitle") != null)
		{
			request.setAttribute("jspTitle", messages.getMessage(request.getParameter("jspTitle")));
		}

		List executionYearsLabels = transformIntoLabels(executionYearList);
		
		request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearsLabels);

		return mapping.findForward("PrepareSuccess");
	}

	private List transformIntoLabels(ArrayList executionYearList)
	{
		List executionYearsLabels = new ArrayList();
		CollectionUtils.collect(executionYearList, new Transformer()
		{
			public Object transform(Object input)
			{
				InfoExecutionYear infoExecutionYear = (InfoExecutionYear) input;
				LabelValueBean labelValueBean =
					new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
				return labelValueBean;
			}
		}, executionYearsLabels);
		Collections.sort(executionYearsLabels, new BeanComparator("label"));
		Collections.reverse(executionYearsLabels);
		
		return executionYearsLabels;
	}

	public ActionForward chooseExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		HttpSession session = request.getSession(false);

		if (session != null)
		{
			session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
			request.setAttribute("executionYear", request.getParameter("executionYear"));

			request.setAttribute("jspTitle", request.getParameter("jspTitle"));

			return mapping.findForward("ChooseSuccess");
		}
		else
			throw new Exception();
	}

}