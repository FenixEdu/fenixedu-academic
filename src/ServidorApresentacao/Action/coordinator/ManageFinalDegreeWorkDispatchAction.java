/*
 * Created on 2004/03/09
 *  
 */
package ServidorApresentacao.Action.coordinator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionDegree;
import DataBeans.finalDegreeWork.InfoProposal;
import DataBeans.finalDegreeWork.InfoScheduleing;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz
 */
public class ManageFinalDegreeWorkDispatchAction extends FenixDispatchAction
{

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);

		HttpSession session = request.getSession(false);
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);

		Object args[] = { infoExecutionDegree.getIdInternal()};
		try
		{
			List finalDegreeWorkProposalHeaders =
				(List) ServiceUtils.executeService(
					userView,
					"ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan",
					args);

			if (finalDegreeWorkProposalHeaders != null && !finalDegreeWorkProposalHeaders.isEmpty())
			{
				Collections.sort(finalDegreeWorkProposalHeaders, new BeanComparator("proposalNumber"));
				
				request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}

		try
		{
			InfoScheduleing infoScheduleing =
				(InfoScheduleing) ServiceUtils.executeService(
					userView,
					"ReadFinalDegreeWorkProposalSubmisionPeriod",
					args);

			if (infoScheduleing != null)
			{
				SimpleDateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:mm");

				DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;
				finalDegreeWorkScheduleingForm.set(
					"startOfProposalPeriodDate",
					dateFormatDate.format(infoScheduleing.getStartOfProposalPeriod()));
				finalDegreeWorkScheduleingForm.set(
					"startOfProposalPeriodHour",
					dateFormatHour.format(infoScheduleing.getStartOfProposalPeriod()));
				finalDegreeWorkScheduleingForm.set(
					"endOfProposalPeriodDate",
					dateFormatDate.format(infoScheduleing.getEndOfProposalPeriod()));
				finalDegreeWorkScheduleingForm.set(
					"endOfProposalPeriodHour",
					dateFormatHour.format(infoScheduleing.getEndOfProposalPeriod()));
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}

		return mapping.findForward("show-final-degree-work-list");
	}

	public ActionForward viewFinalDegreeWorkProposal(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);

		Integer finalDegreeWorkProposalOID =
			new Integer(request.getParameter("finalDegreeWorkProposalOID"));

		Object args[] = { finalDegreeWorkProposalOID };
		try
		{
			InfoProposal infoProposal =
				(InfoProposal) ServiceUtils.executeService(
					userView,
					"ReadFinalDegreeWorkProposal",
					args);

			if (infoProposal != null)
			{
				request.setAttribute("finalDegreeWorkProposal", infoProposal);
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}

		return mapping.findForward("show-final-degree-work-proposal");
	}

	public ActionForward setFinalDegreeProposalPeriod(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		DynaActionForm finalDegreeWorkScheduleingForm = (DynaActionForm) form;

		String startOfProposalPeriodDateString =
			(String) finalDegreeWorkScheduleingForm.get("startOfProposalPeriodDate");
		String startOfProposalPeriodHourString =
			(String) finalDegreeWorkScheduleingForm.get("startOfProposalPeriodHour");
		String startOfProposalPeriodString =
			startOfProposalPeriodDateString + " " + startOfProposalPeriodHourString;
		String endOfProposalPeriodDateString =
			(String) finalDegreeWorkScheduleingForm.get("endOfProposalPeriodDate");
		String endOfProposalPeriodHourString =
			(String) finalDegreeWorkScheduleingForm.get("endOfProposalPeriodHour");
		String endOfProposalPeriodString =
			endOfProposalPeriodDateString + " " + endOfProposalPeriodHourString;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Date startOfProposalPeriod = null;
		Date endOfProposalPeriod = null;
		try
		{

			startOfProposalPeriod = dateFormat.parse(startOfProposalPeriodString);
		}
		catch (ParseException e)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"finalDegreeWorkProposal.setProposalPeriod.validator.start",
				new ActionError("finalDegreeWorkProposal.setProposalPeriod.validator.start"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

		try
		{
			endOfProposalPeriod = dateFormat.parse(endOfProposalPeriodString);
		}
		catch (ParseException e)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"finalWorkInformationForm.numberOfGroupElements",
				new ActionError("finalWorkInformationForm.numberOfGroupElements"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

		IUserView userView = SessionUtils.getUserView(request);
		HttpSession session = request.getSession(false);

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);

		Object args[] =
			{ infoExecutionDegree.getIdInternal(), startOfProposalPeriod, endOfProposalPeriod };
		try
		{
			ServiceUtils.executeService(userView, "DefineFinalDegreeWorkProposalSubmisionPeriod", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException();
		}

		request.setAttribute("sucessfulSetOfDegreeProposalPeriod", "sucessfulSetOfDegreeProposalPeriod");
		return prepare(mapping, form, request, response);
	}

}