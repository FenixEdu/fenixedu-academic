package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoDegreeInfo;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão Create on 11/Nov/2003
 */
public class ShowDegreeSiteAction extends FenixContextDispatchAction
{

	public ActionForward showDescription(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
		//request.setAttribute("executionPeriodOID", executionPeriodOId);

		Integer degreeId = getFromRequest("degreeID", request);
		request.setAttribute("degreeID", degreeId);

		Integer executionDegreeId = getFromRequest("executionDegreeID", request);
		request.setAttribute("executionDegreeID", executionDegreeId);

		Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
		request.setAttribute("inEnglish", inEnglish);

		//If degreeId is null then this was call by coordinator
		//Don't have a degreeId but a executionDegreeId
		//It's necessary read the executionDegree and obtain the correspond degree
		if (degreeId == null)
		{

			//degree information
			Object[] args = { executionDegreeId };

			InfoExecutionDegree infoExecutionDegree = null;
			try
			{
				infoExecutionDegree =
					(InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
						null,
						"ReadExecutionDegreeByOID",
						args);
			}
			catch (FenixServiceException e)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			}
			if (infoExecutionDegree == null
				|| infoExecutionDegree.getInfoDegreeCurricularPlan() == null
				|| infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree() == null)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			}
			if (!errors.isEmpty())
			{
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}

			degreeId = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();
			request.setAttribute("degreeID", degreeId);

			//Read execution period
			InfoExecutionYear infoExecutionYear = infoExecutionDegree.getInfoExecutionYear();
			if (infoExecutionYear == null)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}

			Object[] args2 = { infoExecutionYear };

			List executionPeriods = null;
			try
			{
				executionPeriods =
					(List) ServiceManagerServiceFactory.executeService(
						null,
						"ReadExecutionPeriodsByExecutionYear",
						args2);
			}
			catch (FenixServiceException e)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			}
			if (executionPeriods == null || executionPeriods.size() <= 0)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			}
			if (!errors.isEmpty())
			{
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}

			Collections.sort(executionPeriods, new BeanComparator("endDate"));

			InfoExecutionPeriod infoExecutionPeriod =
				((InfoExecutionPeriod) executionPeriods.get(executionPeriods.size() - 1));
			executionPeriodOId = infoExecutionPeriod.getIdInternal();

			request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
			request.setAttribute(
				SessionConstants.EXECUTION_PERIOD_OID,
				infoExecutionPeriod.getIdInternal().toString());
			request.setAttribute("schoolYear", infoExecutionYear.getYear());

		}

		//degree information
		Object[] args = { executionPeriodOId, degreeId };

		InfoDegreeInfo infoDegreeInfo = null;
		try
		{
			infoDegreeInfo =
				(InfoDegreeInfo) ServiceManagerServiceFactory.executeService(
					null,
					"ReadDegreeInfoByDegreeAndExecutionPeriod",
					args);
		}
		catch (FenixServiceException e)
		{
			errors.add("impossibleDegreeSite", new ActionError("error.public.DegreeInfoNotPresent"));
			saveErrors(request, errors);
			//return (new ActionForward(mapping.getInput()));
		}

		//execution degrees of this degree
		List executionDegreeList = null;
		try
		{
			executionDegreeList =
				(List) ServiceManagerServiceFactory.executeService(
					null,
					"ReadExecutionDegreesByDegreeAndExecutionPeriod",
					args);
		}
		catch (FenixServiceException e)
		{
			errors.add("impossibleDegreeSite", new ActionError("error.impossibleExecutionDegreeList"));
			saveErrors(request, errors);
		}

		request.setAttribute("infoDegreeInfo", infoDegreeInfo);
		request.setAttribute("infoExecutionDegrees", executionDegreeList);

		if (inEnglish == null || inEnglish.booleanValue() == false)
		{
			return mapping.findForward("showDescription");
		}
		else
		{
			return mapping.findForward("showDescriptionEnglish");
		}
	}

	public ActionForward showAccessRequirements(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		Integer executionPeriodOId = getFromRequest("executionPeriodOID", request);
		//request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, executionPeriodOId);

		Integer degreeId = getFromRequest("degreeID", request);
		request.setAttribute("degreeID", degreeId);

		Integer executionDegreeId = getFromRequest("executionDegreeID", request);
		request.setAttribute("executionDegreeID", executionDegreeId);

		Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
		request.setAttribute("inEnglish", inEnglish);

		//degree information
		Object[] args = { executionPeriodOId, degreeId };

		InfoDegreeInfo infoDegreeInfo = null;
		try
		{
			infoDegreeInfo =
				(InfoDegreeInfo) ServiceManagerServiceFactory.executeService(
					null,
					"ReadDegreeInfoByDegreeAndExecutionPeriod",
					args);
		}
		catch (FenixServiceException e)
		{
			errors.add("impossibleDegreeSite", new ActionError("error.public.DegreeInfoNotPresent"));
			saveErrors(request, errors);
			//            return (new ActionForward(mapping.getInput()));

		}

		request.setAttribute("infoDegreeInfo", infoDegreeInfo);
		if (inEnglish == null || inEnglish.booleanValue() == false)
		{
			return mapping.findForward("showAccessRequirements");
		}
		else
		{
			return mapping.findForward("showAccessRequirementsEnglish");
		}
	}

	public ActionForward showCurricularPlan(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		Integer degreeId = getFromRequest("degreeID", request);
		request.setAttribute("degreeID", degreeId);

		Integer executionDegreeId = getFromRequest("executionDegreeID", request);
		request.setAttribute("executionDegreeID", executionDegreeId);

		Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

		Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
		request.setAttribute("inEnglish", inEnglish);

		//if came in the request a executionDegreeId that it is necessary
		//find the correpond degree curricular plan
		if (executionDegreeId != null)
		{
			Object[] args = { executionDegreeId };

			InfoExecutionDegree infoExecutionDegree = null;
			try
			{
				infoExecutionDegree =
					(InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
						null,
						"ReadExecutionDegreeByOID",
						args);
			}
			catch (FenixServiceException e)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			}
			if (infoExecutionDegree == null
				|| infoExecutionDegree.getInfoDegreeCurricularPlan() == null)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
			}
			if (!errors.isEmpty())
			{
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}
			request.setAttribute(
				"infoDegreeCurricularPlan",
				infoExecutionDegree.getInfoDegreeCurricularPlan());
		}
		else
		{
			Object[] args = { degreeId };

			List infoDegreeCurricularPlanList = null;
			try
			{
				infoDegreeCurricularPlanList =
					(List) ServiceManagerServiceFactory.executeService(
						null,
						"ReadPublicDegreeCurricularPlansByDegree",
						args);
			}
			catch (FenixServiceException e)
			{
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}
			//order the list by state and next by begin date
			ComparatorChain comparatorChain = new ComparatorChain();
			comparatorChain.addComparator(new BeanComparator("state.degreeState"));
			comparatorChain.addComparator(new BeanComparator("initialDate"), true);

			Collections.sort(infoDegreeCurricularPlanList, comparatorChain);

			request.setAttribute("infoDegreeCurricularPlanList", infoDegreeCurricularPlanList);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan =
				(InfoDegreeCurricularPlan) infoDegreeCurricularPlanList.get(0);
			request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);

			//if came in the request a degreeCurricularPlanId that it is necessary
			//find information about this degree curricular plan
			if (degreeCurricularPlanId != null)
			{
				Iterator iterator = infoDegreeCurricularPlanList.iterator();
				while (iterator.hasNext())
				{
					InfoDegreeCurricularPlan infoDegreeCurricularPlanElem =
						(InfoDegreeCurricularPlan) iterator.next();
					if (infoDegreeCurricularPlanElem.getIdInternal().equals(degreeCurricularPlanId))
					{
						request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlanElem);
						break;
					}
				}
			}
		}

		if (inEnglish == null || inEnglish.booleanValue() == false)
		{
			return mapping.findForward("showCurricularPlans");
		}
		else
		{
			return mapping.findForward("showCurricularPlansEnglish");
		}
	}

	private Integer getFromRequest(String parameter, HttpServletRequest request)
	{
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null)
		{
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null)
		{
			try
			{
				parameterCode = new Integer(parameterCodeString);
			}
			catch (Exception exception)
			{
				return null;
			}
		}
		return parameterCode;
	}

	private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request)
	{
		Boolean parameterBoolean = null;

		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null)
		{
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null)
		{
			try
			{
				parameterBoolean = new Boolean(parameterCodeString);
			}
			catch (Exception exception)
			{
				return null;
			}
		}

		return parameterBoolean;
	}
}
