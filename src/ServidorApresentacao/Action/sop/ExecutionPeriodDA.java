package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExecutionPeriodDA extends DispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);

		IUserView userView = (IUserView) session.getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();

		Object argsReadExecutionPeriods[] = {
		};
		ArrayList executionPeriods =
			(ArrayList) gestor.executar(
				userView,
				"ReadExecutionPeriods",
				argsReadExecutionPeriods);

		// if executionPeriod was previously selected,form has that
		// value as default
		InfoExecutionPeriod selectedExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		if (selectedExecutionPeriod != null) {
			DynaActionForm indexForm = (DynaActionForm) form;
			indexForm.set(
				"index",
				new Integer(executionPeriods.indexOf(selectedExecutionPeriod)));
		}
		//----------------------------------------------

		ArrayList executionPeriodsLabelValueList = new ArrayList();
		for (int i = 0; i < executionPeriods.size(); i++) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) executionPeriods.get(i);
			executionPeriodsLabelValueList.add(
				new LabelValueBean(
					infoExecutionPeriod.getName()
						+ " - "
						+ infoExecutionPeriod.getInfoExecutionYear().getYear(),
					"" + i));
		}

		session.setAttribute(
			SessionConstants.LIST_INFOEXECUTIONPERIOD,
			executionPeriods);

		request.setAttribute(
			SessionConstants.LABELLIST_EXECUTIONPERIOD,
			executionPeriodsLabelValueList);

		return mapping.findForward("showForm");
	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm indexForm = (DynaActionForm) form;

		ArrayList infoExecutionPeriodList =
			(ArrayList) session.getAttribute(
				SessionConstants.LIST_INFOEXECUTIONPERIOD);
		Integer index = (Integer) indexForm.get("index");

		if (infoExecutionPeriodList != null && index != null) {
			session.setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				infoExecutionPeriodList.get(index.intValue()));
		}
		return mapping.findForward("choose");
	}

	public ActionForward chooseForViewRoom(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm indexForm = (DynaActionForm) form;

		IUserView userView = (IUserView) session.getAttribute("UserView");
		GestorServicos gestor = GestorServicos.manager();

		ArrayList infoExecutionPeriodList =
			(ArrayList) session.getAttribute(
				SessionConstants.LIST_INFOEXECUTIONPERIOD);
		Integer index = (Integer) indexForm.get("index");

		if (infoExecutionPeriodList != null && index != null) {
			session.setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				infoExecutionPeriodList.get(index.intValue()));
		}

		Object argsReadExecutionPeriods[] = {
		};
		ArrayList executionPeriods =
			(ArrayList) gestor.executar(
				userView,
				"ReadExecutionPeriods",
				argsReadExecutionPeriods);

		// if executionPeriod was previously selected,form has that
		// value as default
		InfoExecutionPeriod selectedExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		if (selectedExecutionPeriod != null) {
			indexForm = (DynaActionForm) form;
			indexForm.set(
				"index",
				new Integer(executionPeriods.indexOf(selectedExecutionPeriod)));
		}
		//----------------------------------------------

		ArrayList executionPeriodsLabelValueList = new ArrayList();
		for (int i = 0; i < executionPeriods.size(); i++) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) executionPeriods.get(i);
			executionPeriodsLabelValueList.add(
				new LabelValueBean(
					infoExecutionPeriod.getName()
						+ " - "
						+ infoExecutionPeriod.getInfoExecutionYear().getYear(),
					"" + i));
		}

		session.setAttribute(
			SessionConstants.LIST_INFOEXECUTIONPERIOD,
			executionPeriods);

		request.setAttribute(
			SessionConstants.LABELLIST_EXECUTIONPERIOD,
			executionPeriodsLabelValueList);
			
		InfoRoom infoRoom = (InfoRoom)session.getAttribute("publico.infoRoom");
		
		Object argsReadLessons[] = { selectedExecutionPeriod, infoRoom };

			try {
				List lessons;
				lessons =
					(List) ServiceUtils.executeService(
						null,
						"LerAulasDeSalaEmSemestre",
						argsReadLessons);

				if (lessons != null) {
					session.setAttribute(
						SessionConstants.LESSON_LIST_ATT,
						lessons);
				}

			} catch (FenixServiceException e) {
				throw new FenixActionException();
			}
			

		return mapping.findForward("viewRoom");
	}

}
