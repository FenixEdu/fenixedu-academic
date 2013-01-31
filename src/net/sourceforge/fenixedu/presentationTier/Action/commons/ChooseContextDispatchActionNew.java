package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixDateAndTimeDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author jpvl
 */
public class ChooseContextDispatchActionNew extends FenixDateAndTimeDispatchAction {

	protected static final String INFO_DEGREE_INITIALS_PARAMETER = "degreeInitials";

	protected static final String SEMESTER_PARAMETER = "semester";

	protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String inputPage = request.getParameter(PresentationConstants.INPUT_PAGE);
		if (inputPage != null) {
			request.setAttribute(PresentationConstants.INPUT_PAGE, inputPage);
		}

		String nextPage = request.getParameter(PresentationConstants.NEXT_PAGE);
		if (nextPage != null) {
			request.setAttribute(PresentationConstants.NEXT_PAGE, nextPage);
		}

		setExecutionContext(request);

		Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

		Integer degreeId = getFromRequest("degreeID", request);
		request.setAttribute("degreeID", degreeId);

		// lista
		List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
		if (executionPeriodsLabelValueList.size() > 1) {
			request.setAttribute("lista", executionPeriodsLabelValueList);
		} else {
			request.removeAttribute("lista");
		}

		return mapping.findForward("prepare");
	}

	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {
		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY);
		if (infoExecutionPeriod == null) {
			IUserView userView = UserView.getUser();
			infoExecutionPeriod = ReadCurrentExecutionPeriod.run();
			request.setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
		}
		return infoExecutionPeriod;
	}

	public ActionForward preparePublic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String inputPage = request.getParameter(PresentationConstants.INPUT_PAGE);

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

		// TODO: this semester and curricular year list needs to be refactored
		// in order to incorporate masters
		/* Criar o bean de semestres */
		List<LabelValueBean> semestres = new ArrayList<LabelValueBean>();
		semestres.add(new LabelValueBean("escolher", ""));
		semestres.add(new LabelValueBean("1 �", "1"));
		semestres.add(new LabelValueBean("2 �", "2"));
		request.setAttribute("semestres", semestres);

		/* Criar o bean de anos curricutares */
		List<LabelValueBean> anosCurriculares = new ArrayList<LabelValueBean>();
		anosCurriculares.add(new LabelValueBean("escolher", ""));
		anosCurriculares.add(new LabelValueBean("1 �", "1"));
		anosCurriculares.add(new LabelValueBean("2 �", "2"));
		anosCurriculares.add(new LabelValueBean("3 �", "3"));
		anosCurriculares.add(new LabelValueBean("4 �", "4"));
		anosCurriculares.add(new LabelValueBean("5 �", "5"));
		request.setAttribute("curricularYearList", anosCurriculares);

		/* Cria o form bean com as licenciaturas em execucao. */

		List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

		List<LabelValueBean> licenciaturas = new ArrayList<LabelValueBean>();

		licenciaturas.add(new LabelValueBean("escolher", ""));

		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

		Iterator iterator = executionDegreeList.iterator();

		int index = 0;
		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

			name +=
					duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
							+ infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

			licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
		}

		request.setAttribute("degreeList", licenciaturas);

		if (inputPage != null) {
			return mapping.findForward(inputPage);
		}

		// TODO : throw a proper exception
		throw new Exception("SomeOne is messing around with the links");

	}

	private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
		InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext()) {
			InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
			if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
					&& !(infoExecutionDegree.equals(infoExecutionDegree2))) {
				return true;
			}

		}
		return false;
	}

	public ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm escolherContextoForm = (DynaActionForm) form;

		IUserView userView = UserView.getUser();

		String nextPage = (String) request.getAttribute(PresentationConstants.NEXT_PAGE);
		if (nextPage == null) {
			nextPage = request.getParameter(PresentationConstants.NEXT_PAGE);
		}

		Integer semestre = ((InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD)).getSemester();
		Integer anoCurricular = (Integer) escolherContextoForm.get("curricularYear");

		int index = Integer.parseInt((String) escolherContextoForm.get("index"));

		request.setAttribute("anoCurricular", anoCurricular);
		request.setAttribute("semestre", semestre);

		List infoExecutionDegreeList =
				ReadExecutionDegreesByExecutionYear.run(((InfoExecutionPeriod) request
						.getAttribute(PresentationConstants.EXECUTION_PERIOD)).getInfoExecutionYear());
		List<LabelValueBean> licenciaturas = new ArrayList<LabelValueBean>();
		licenciaturas.add(new LabelValueBean("escolher", ""));
		Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(index);

		if (infoExecutionDegree != null) {
			CurricularYearAndSemesterAndInfoExecutionDegree cYSiED =
					new CurricularYearAndSemesterAndInfoExecutionDegree(anoCurricular, semestre, infoExecutionDegree);
			request.setAttribute(PresentationConstants.CONTEXT_KEY, cYSiED);

			request.setAttribute(PresentationConstants.CURRICULAR_YEAR_KEY, anoCurricular);
			request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, anoCurricular.toString());
			request.setAttribute(PresentationConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
			request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
			request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, infoExecutionDegree.getIdInternal().toString());
		} else {
			return mapping.findForward("Licenciatura execucao inexistente");
		}

		if (nextPage != null) {
			return mapping.findForward(nextPage);
		}

		// TODO : throw a proper exception
		throw new Exception("SomeOne is messing around with the links");
	}

	public ActionForward nextPagePublic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final ActionErrors errors = new ActionErrors();
		final DynaActionForm escolherContextoForm = (DynaActionForm) form;

		// degreeID
		final Integer degreeId = getFromRequest("degreeID", request);
		request.setAttribute("degreeID", degreeId);

		// degreeCurricularPlanID
		final Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

		// lista
		final List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
		if (executionPeriodsLabelValueList.size() > 1) {
			request.setAttribute("lista", executionPeriodsLabelValueList);
		} else {
			request.removeAttribute("lista");
		}

		// curYear
		final Integer anoCurricular = (Integer) escolherContextoForm.get("curYear");
		request.setAttribute("curYear", anoCurricular);

		// infoDegreeCurricularPlan
		if (degreeCurricularPlanId != null) {
			final DegreeCurricularPlan degreeCurricularPlan =
					rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
			request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
		}

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
		Integer executionPeriodID = (Integer) escolherContextoForm.get("indice");
		if (executionPeriodID == null) {
			executionPeriodID = getFromRequest("indice", request);
		}
		if (executionPeriodID != null) {
			infoExecutionPeriod = ReadExecutionPeriodByOID.run(executionPeriodID);
		}
		request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
		escolherContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
		RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
		request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
		request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
		request.setAttribute("semester", infoExecutionPeriod.getSemester());

		final ExecutionSemester executionSemester =
				rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod.getIdInternal());
		final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
		ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionSemester.getExecutionYear());
		if (executionDegree == null) {
			executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

			if (executionDegree != null) {
				infoExecutionPeriod =
						InfoExecutionPeriod.newInfoFromDomain(executionDegree.getExecutionYear().getExecutionSemesterFor(1));
				request.setAttribute("indice", infoExecutionPeriod.getIdInternal());
				escolherContextoForm.set("indice", infoExecutionPeriod.getIdInternal());
				RequestUtils.setExecutionPeriodToRequest(request, infoExecutionPeriod);
				request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
				request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
				request.setAttribute("semester", infoExecutionPeriod.getSemester());
			}
		}

		if (executionDegree != null) {
			InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
			request.setAttribute(PresentationConstants.EXECUTION_DEGREE, infoExecutionDegree);
			request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal().toString());
			RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);

			request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree.getInfoDegreeCurricularPlan());
			request.setAttribute(PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN,
					infoExecutionDegree.getInfoDegreeCurricularPlan());
		}

		String nextPage = request.getParameter("nextPage");
		if (nextPage != null) {
			return mapping.findForward(nextPage);
		} else {
			throw new FenixActionException();
		}

	}

}