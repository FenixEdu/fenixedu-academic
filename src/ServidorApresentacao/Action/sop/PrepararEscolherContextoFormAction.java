package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tfc130
 */
public class PrepararEscolherContextoFormAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		if (session != null) {
			IUserView userView = SessionUtils.getUserView(request);

			InfoExecutionPeriod infoExecutionPeriod =
				setExecutionContext(request);

			/* Criar o bean de semestres */
			ArrayList semestres = new ArrayList();
			semestres.add(new LabelValueBean("escolher", ""));
			semestres.add(new LabelValueBean("1 º", "1"));
			semestres.add(new LabelValueBean("2 º", "2"));
			session.setAttribute("semestres", semestres);

			/* Criar o bean de anos curricutares */
			ArrayList anosCurriculares = new ArrayList();
			anosCurriculares.add(new LabelValueBean("escolher", ""));
			anosCurriculares.add(new LabelValueBean("1 º", "1"));
			anosCurriculares.add(new LabelValueBean("2 º", "2"));
			anosCurriculares.add(new LabelValueBean("3 º", "3"));
			anosCurriculares.add(new LabelValueBean("4 º", "4"));
			anosCurriculares.add(new LabelValueBean("5 º", "5"));
			session.setAttribute("anosCurriculares", anosCurriculares);

			/* Cria o form bean com as licenciaturas em execucao.*/
			Object argsLerLicenciaturas[] =
				{ infoExecutionPeriod.getInfoExecutionYear()};

			List executionDegreeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYear",
					argsLerLicenciaturas);

			ArrayList licenciaturas = new ArrayList();

			licenciaturas.add(new LabelValueBean("escolher", ""));

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
				String value =
					infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla();

				name
					+= duplicateInfoDegree(
						executionDegreeList,
						infoExecutionDegree)
					? "-"
						+ infoExecutionDegree
							.getInfoDegreeCurricularPlan()
							.getName()
					: "";

				licenciaturas.add(
					new LabelValueBean(name, String.valueOf(index++)));
			}

			session.setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY,
				executionDegreeList);

			request.setAttribute("licenciaturas", licenciaturas);

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
	/**
	 * Method existencesOfInfoDegree.
	 * @param executionDegreeList
	 * @param infoExecutionDegree
	 * @return int
	 */
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
	/**
	 * Method setExecutionContext.
	 * @param request
	 */
	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request)
		throws Exception {
		IUserView userView = SessionUtils.getUserView(request);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) ServiceUtils.executeService(
				userView,
				"ReadActualExecutionPeriod",
				new Object[0]);
		HttpSession session = request.getSession();
		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			infoExecutionPeriod);
		return infoExecutionPeriod;
	}
}