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

			ArrayList degreeNamesList = new ArrayList();

			Iterator iterator = executionDegreeList.iterator();

			int index = 0;
			while (iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree =
					(InfoExecutionDegree) iterator.next();
				String name =
					infoExecutionDegree
						.getInfoDegreeCurricularPlan()
						.getInfoDegree()
						.getNome();

				name =
					infoExecutionDegree
						.getInfoDegreeCurricularPlan()
						.getInfoDegree()
						.getTipoCurso()
						.toString()
						+ " em "
						+ name;

				name
					+= duplicateInfoDegree(
						executionDegreeList,
						infoExecutionDegree)
					? "-"
						+ infoExecutionDegree
							.getInfoDegreeCurricularPlan()
							.getName()
					: "";

				degreeNamesList.add(name);
			}

			request.setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_LIST,
				executionDegreeList);

			request.setAttribute(
				SessionConstants.DEGREE_NAMES_LIST,
				degreeNamesList);

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

			InfoExecutionPeriod infoExecutionPeriod =
				setExecutionContext(request);

			Object[] args = { infoExecutionPeriod };
			List infoViewClassScheduleList =
				(List) gestor.executar(userView, "ReadAllClassesLessons", args);

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
