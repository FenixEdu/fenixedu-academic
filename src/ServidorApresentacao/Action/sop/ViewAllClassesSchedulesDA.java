package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllClassesSchedulesDA extends DispatchAction {

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			InfoExecutionPeriod infoExecutionPeriod =
				setExecutionContext(request);
			/* Cria o form bean com as licenciaturas em execucao.*/
			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};

			List executionDegreeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);

			Collections.sort(
				executionDegreeList,
				new ComparatorByNameForInfoExecutionDegree());

			request.setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_LIST,
				executionDegreeList);

			return mapping.findForward("choose");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}

	public ActionForward list(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);
			DynaActionForm chooseViewAllClassesSchedulesContextForm =
				(DynaActionForm) form;

			InfoExecutionPeriod infoExecutionPeriod =
				setExecutionContext(request);

			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};
			List infoExecutionDegreeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);
			Collections.sort(
				infoExecutionDegreeList,
				new ComparatorByNameForInfoExecutionDegree());

			Boolean selectAllDegrees =
				(Boolean) chooseViewAllClassesSchedulesContextForm.get(
					"selectAllDegrees");
			List selectedInfoExecutionDegrees = null;
			if (selectAllDegrees != null && selectAllDegrees.booleanValue()) {
				selectedInfoExecutionDegrees = infoExecutionDegreeList;
			} else {
				String[] selectedDegreesIndexes =
					(String[]) chooseViewAllClassesSchedulesContextForm.get(
						"selectedDegrees");
				selectedInfoExecutionDegrees = new ArrayList();						
				for (int i = 0; i < selectedDegreesIndexes.length; i++) {
					//System.out.println("### "+selectedDegreesIndexes[i]);
					Integer index = new Integer("" + selectedDegreesIndexes[i]);
					selectedInfoExecutionDegrees.add(
						(InfoExecutionDegree) infoExecutionDegreeList.get(
							index.intValue()));
				}
			}

			Object[] args =
				{ selectedInfoExecutionDegrees, infoExecutionPeriod };
			List infoViewClassScheduleList =
				(List) gestor.executar(
					userView,
					"ReadDegreesClassesLessons",
					args);

			if (infoViewClassScheduleList != null
				&& infoViewClassScheduleList.isEmpty()) {
				request.removeAttribute(
					SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE);
			} else {
				request.setAttribute(
					SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE,
					infoViewClassScheduleList);
			}

			return mapping.findForward("list");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}

	/**
	 * Method setExecutionContext.
	 * @param request
	 */
	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request)
		throws Exception {

		HttpSession session = request.getSession(false);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		if (infoExecutionPeriod == null) {
			IUserView userView = SessionUtils.getUserView(request);
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					userView,
					"ReadCurrentExecutionPeriod",
					new Object[0]);

			session.setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				infoExecutionPeriod);
		}
		return infoExecutionPeriod;
	}

	private boolean duplicateInfoDegree(
		List executionDegreeList,
		InfoExecutionDegree infoExecutionDegree) {
		InfoDegree infoDegree =
			infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree2 =
				(InfoExecutionDegree) iterator.next();
			if (infoDegree
				.equals(
					infoExecutionDegree2
						.getInfoDegreeCurricularPlan()
						.getInfoDegree())
				&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;

		}
		return false;
	}
}
