/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
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
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDA extends DispatchAction {

	protected static final String INFO_DEGREE_INITIALS_PARAMETER =
		"degreeInitials";
	protected static final String SEMESTER_PARAMETER = "semester";
	protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
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

			String[] curricularYears = { "1", "2", "3", "4", "5" };

			List curricularYearsList = new ArrayList();
			curricularYearsList.add("1");
			curricularYearsList.add("2");
			curricularYearsList.add("3");
			curricularYearsList.add("4");
			curricularYearsList.add("5");
			session.setAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY,
				curricularYearsList);

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

			request.setAttribute(SessionConstants.DEGREES, licenciaturas);

			return mapping.findForward("chooseExamsMapContext");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm chooseExamContextoForm = (DynaActionForm) form;

		SessionUtils.removeAttributtes(
			session,
			SessionConstants.CONTEXT_PREFIX);

		if (session != null) {
			/* FIXME : get semestre with executionPeriod */
			Integer semestre = new Integer(2);

			System.out.println(
				"selectedCurricularYears= "
					+ chooseExamContextoForm.get("selectedCurricularYears"));
			String[] selectedCurricularYears =
				(String[]) chooseExamContextoForm.get(
					"selectedCurricularYears");

			List curricularYears =
				new ArrayList(selectedCurricularYears.length);
			for (int i = 0; i < selectedCurricularYears.length; i++)
				curricularYears.add(new Integer(selectedCurricularYears[i]));
			
			session.setAttribute(
				SessionConstants.CURRICULAR_YEARS_LIST,
				curricularYears);

			int index =
				Integer.parseInt((String) chooseExamContextoForm.get("index"));

			List infoExecutionDegreeList =
				(List) session.getAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);

			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) infoExecutionDegreeList.get(index);

			if (infoExecutionDegree != null) {
				session.setAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_KEY,
					infoExecutionDegree);
			} else {
				return mapping.findForward("Licenciatura execucao inexistente");
			}

		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao

		return mapping.findForward("showExamsMap");

	}

	/**
	 * Method setCurricularYearList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setCurricularYearList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List curricularYearList =
			(List) request.getSession(false).getAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY);

		if (curricularYearList == null) {
			curricularYearList = new ArrayList();
			curricularYearList.add(new LabelValueBean("1º", "1"));
			curricularYearList.add(new LabelValueBean("2º", "2"));
			curricularYearList.add(new LabelValueBean("3º", "3"));
			curricularYearList.add(new LabelValueBean("4º", "4"));
			curricularYearList.add(new LabelValueBean("5º", "5"));
			request.getSession(false).setAttribute(
				SessionConstants.CURRICULAR_YEAR_LIST_KEY,
				curricularYearList);
		}
		return curricularYearList;
	}
	/**
	 * Method setSemesterList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setSemesterList(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		List semesterList =
			(List) request.getSession(false).getAttribute(
				SessionConstants.SEMESTER_LIST_KEY);

		if (semesterList == null) {

			semesterList = new ArrayList();
			semesterList.add(new LabelValueBean("1º", "1"));
			semesterList.add(new LabelValueBean("2º", "2"));
			request.getSession(false).setAttribute(
				SessionConstants.SEMESTER_LIST_KEY,
				semesterList);
		}
		return semesterList;
	}
	/**
	 * Method setInfoDegreeList.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private List setInfoDegreeList(HttpServletRequest request)
		throws Exception {

		List infoExecutionDegreeList = null;
		InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);
		Object args[] = { infoExecutionPeriod.getInfoExecutionYear()};
		infoExecutionDegreeList =
			(List) ServiceUtils.executeService(
				null,
				"ReadExecutionDegreesByExecutionYear",
				args);

		request.getSession(false).setAttribute(
			SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY,
			infoExecutionDegreeList);

		return infoExecutionDegreeList;
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
		HttpSession session = request.getSession(false);

		session.setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			infoExecutionPeriod);
		return infoExecutionPeriod;
	}

}
