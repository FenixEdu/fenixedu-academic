package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ChooseContextDA extends FenixContextDispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

		return mapping.findForward("ShowChooseForm");
	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		System.out.println("In ChooseContext DA");

		DynaActionForm chooseScheduleContext = (DynaActionForm) form;

		IUserView userView = SessionUtils.getUserView(request);

		/* Determine Selected Curricular Year */
		Integer anoCurricular =
			new Integer((String) chooseScheduleContext.get("curricularYear"));

		Object argsReadCurricularYearByOID[] = { anoCurricular };
		InfoCurricularYear infoCurricularYear =
			(InfoCurricularYear) ServiceUtils.executeService(
				userView,
				"ReadCurricularYearByOID",
				argsReadCurricularYearByOID);

		request.setAttribute(
			SessionConstants.CURRICULAR_YEAR,
			infoCurricularYear);

		/* Determine Selected Execution Degree */
		Integer executionDegreeOID =
			new Integer(
				(String) chooseScheduleContext.get("executionDegreeOID"));

		Object argsReadExecutionDegreeByOID[] = { executionDegreeOID };
		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) ServiceUtils.executeService(
				userView,
				"ReadExecutionDegreeByOID",
				argsReadExecutionDegreeByOID);

		if (infoExecutionDegree == null) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"errors.invalid.execution.degree",
				new ActionError("errors.invalid.execution.degree"));
			saveErrors(request, actionErrors);
			System.out.println("Out ChooseContext DA");
			return mapping.getInputForward();
		} else {
			request.setAttribute(
				SessionConstants.EXECUTION_DEGREE,
				infoExecutionDegree);
			System.out.println("Out ChooseContext DA");
			return mapping.findForward("ManageSchedules");
		}

	}

}