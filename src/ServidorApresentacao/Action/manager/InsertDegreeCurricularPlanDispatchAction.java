/*
 * Created on 31/Jul/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author lmac1
 */

public class InsertDegreeCurricularPlanDispatchAction extends FenixDispatchAction
{

	public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{

		return mapping.findForward("insertDegreeCurricularPlan");
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException
	{

		IUserView userView = SessionUtils.getUserView(request);

		Integer degreeId = new Integer(request.getParameter("degreeId"));

		DynaActionForm dynaForm = (DynaValidatorForm) form;

		String name = (String) dynaForm.get("name");
		Integer stateInt = new Integer((String) dynaForm.get("state"));
		String initialDateString = (String) dynaForm.get("initialDate");
		String endDateString = (String) dynaForm.get("endDate");
		Integer degreeDuration = new Integer((String) dynaForm.get("degreeDuration"));
		Integer minimalYearForOptionalCourses = new Integer((String) dynaForm.get("minimalYearForOptionalCourses"));
		String neededCreditsString = (String) dynaForm.get("neededCredits");
		String markTypeString = (String) dynaForm.get("markType");
		String numerusClaususString = (String) dynaForm.get("numerusClausus");

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		DegreeCurricularPlanState state = new DegreeCurricularPlanState(stateInt);

		Calendar initialDate = Calendar.getInstance();
		if (initialDateString.compareTo("") != 0)
		{
			String[] initialDateTokens = initialDateString.split("/");
			initialDate.set(Calendar.DAY_OF_MONTH, (new Integer(initialDateTokens[0])).intValue());
			initialDate.set(Calendar.MONTH, (new Integer(initialDateTokens[1])).intValue() - 1);
			initialDate.set(Calendar.YEAR, (new Integer(initialDateTokens[2])).intValue());
			infoDegreeCurricularPlan.setInitialDate(initialDate.getTime());
		}

		Calendar endDate = Calendar.getInstance();
		if (endDateString.compareTo("") != 0)
		{
			String[] endDateTokens = endDateString.split("/");
			endDate.set(Calendar.DAY_OF_MONTH, (new Integer(endDateTokens[0])).intValue());
			endDate.set(Calendar.MONTH, (new Integer(endDateTokens[1])).intValue() - 1);
			endDate.set(Calendar.YEAR, (new Integer(endDateTokens[2])).intValue());
			infoDegreeCurricularPlan.setEndDate(endDate.getTime());
		}

		if (endDate.before(initialDate))
			throw new InvalidArgumentsActionException("message.manager.date.restriction");

		if (neededCreditsString.compareTo("") != 0)
		{
			Double neededCredits = new Double(neededCreditsString);
			infoDegreeCurricularPlan.setNeededCredits(neededCredits);
		}

		MarkType markType = new MarkType(new Integer(markTypeString));
		infoDegreeCurricularPlan.setMarkType(markType);

		if (numerusClaususString.compareTo("") != 0)
		{
			Integer numerusClausus = new Integer(numerusClaususString);
			infoDegreeCurricularPlan.setNumerusClausus(numerusClausus);
		}

		infoDegreeCurricularPlan.setName(name);
		infoDegreeCurricularPlan.setState(state);
		infoDegreeCurricularPlan.setDegreeDuration(degreeDuration);
		infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);

		InfoDegree infoDegree = new InfoDegree();
		infoDegree.setIdInternal(degreeId);
		infoDegreeCurricularPlan.setInfoDegree(infoDegree);

		Object args[] = { infoDegreeCurricularPlan };

		try
		{
			ServiceUtils.executeService(userView, "InsertDegreeCurricularPlan", args);

		} catch (ExistingServiceException ex)
		{
			throw new ExistingActionException(ex.getMessage(), ex);
		} catch (NonExistingServiceException exception)
		{
			throw new NonExistingActionException("message.nonExistingDegree", mapping.findForward("readDegree"));
		} catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		return mapping.findForward("readDegree");
	}
}
