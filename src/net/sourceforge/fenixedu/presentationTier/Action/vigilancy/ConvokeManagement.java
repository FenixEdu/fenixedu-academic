package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategySugestion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

public class ConvokeManagement extends FenixDispatchAction {

	public ActionForward prepareEditConvoke(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		recoverBeanFromRequest(request);
		return mapping.findForward("prepareEditConvoke");
	}

	public ActionForward changeTemporalInformation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ConvokeBean bean = (ConvokeBean) RenderUtils.getViewState(
				"temporalInfo").getMetaObject().getObject();
		VigilantGroup group = bean.getSelectedVigilantGroup();
		putInformationOnRequest(request, group, bean.getTemporalInformation(),
				bean.isShowInformationByVigilant());
		request.setAttribute("bean", bean);
		return mapping.findForward("prepareEditConvoke");
	}

	public ActionForward convokeAttended(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String id = request.getParameter("oid");
		Integer idInternal = Integer.valueOf(id);
		String bool = request.getParameter("bool");
		Boolean value = Boolean.valueOf(bool);
		try {
			Object[] args = { idInternal, value };
			executeService(request, "ConvokesAttended", args);
		} catch (DomainException exception) {
			addActionMessage(request, exception.getMessage(), null);
		}

		return prepareEditConvoke(mapping, form, request, response);
	}

	public ActionForward convokeActive(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String id = request.getParameter("oid");
		Integer idInternal = Integer.valueOf(id);
		String bool = request.getParameter("bool");
		Boolean value = Boolean.valueOf(bool);

		ExamCoordinator coordinator = getCoordinatorForCurrentYear(request);
		try {
			Object[] args = { idInternal, value, coordinator };
			executeService(request, "ChangeConvokeActive", args);
		} catch (DomainException exception) {
			addActionMessage(request, exception.getMessage(), null);
		}

		return prepareEditConvoke(mapping, form, request, response);
	}

	public ActionForward prepareConvoke(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ConvokeBean bean = new ConvokeBean();
		ExamCoordinator coordinator = getCoordinatorForCurrentYear(request);
		Person person = getLoggedPerson(request);
		ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

		Unit unit = coordinator.getUnit();

		bean.setPerson(person);
		bean.setUnit(unit);
		bean.setExecutionYear(executionYear);
		bean.setExamCoordinator(coordinator);

		request.setAttribute("bean", bean);
		return mapping.findForward("prepareGenerateConvokes");

	}

	public ActionForward generateConvokesSugestion(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ConvokeBean bean = (ConvokeBean) RenderUtils.getViewState(
				"selectEvaluation").getMetaObject().getObject();
		VigilantGroup vigilantGroup = bean.getSelectedVigilantGroup();
		WrittenEvaluation evaluation = bean.getWrittenEvaluation();

		if (vigilantGroup != null && evaluation != null) {
			if (vigilantGroup.getAllAssocitedWrittenEvaluations().contains(
					evaluation)) {
				StrategySugestion vigilantSugestion = vigilantGroup
						.sugestVigilantsToConvoke(evaluation);
				bean.setTeachersForAGivenCourse(vigilantSugestion.getVigilantsThatAreTeachersSugestion());
				bean.setSelectedTeachers(bean.getTeachersForAGivenCourse());
				bean.setVigilantsSugestion(vigilantSugestion
						.getVigilantSugestion());
				bean.setUnavailableVigilants(vigilantSugestion
						.getUnavailableVigilants());
				bean.setUnavailableInformation(vigilantSugestion
						.getUnavailableVigilantsWithInformation());
			} else {
				bean.setWrittenEvaluation(null);
			}
		}
		request.setAttribute("bean", bean);
		RenderUtils.invalidateViewState("selectEvaluation");
		return mapping.findForward("prepareGenerateConvokes");
	}

	public ActionForward createConvokes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ConvokeBean bean = (ConvokeBean) RenderUtils.getViewState(
				"confirmConvokes").getMetaObject().getObject();
		List<Vigilant> vigilantSugestion = bean.getVigilants();
		WrittenEvaluation writtenEvaluation = bean.getWrittenEvaluation();

		Object[] args = { vigilantSugestion, writtenEvaluation,
				bean.getSelectedVigilantGroup(), bean.getExamCoordinator(),
				bean.getEmailMessage() };
		try {
			executeService(request, "CreateConvokes", args);
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage(), null);
		}
		recoverBeanFromRequest(request);
		return mapping.findForward("prepareEditConvoke");
	}

	public ActionForward confirmConvokes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ConvokeBean beanWithTeachers = (ConvokeBean) RenderUtils.getViewState(
				"selectVigilantsThatAreTeachers").getMetaObject().getObject();
		
		ConvokeBean beanWithVigilants = (ConvokeBean) RenderUtils.getViewState(
				"selectVigilants").getMetaObject().getObject();

		ConvokeBean beanWithUnavailables = (ConvokeBean) RenderUtils
				.getViewState("selectVigilantsThatAreUnavailable")
				.getMetaObject().getObject();
		List<Vigilant> teachers, vigilants, unavailables;

		teachers = beanWithTeachers.getSelectedTeachers();
		vigilants = beanWithVigilants.getVigilants();
		unavailables = beanWithUnavailables.getSelectedUnavailableVigilants();

		vigilants.addAll(teachers);
		vigilants.addAll(unavailables);
		beanWithVigilants.setVigilants(vigilants);

		beanWithVigilants.setEmailMessage(RenderUtils.getResourceString(
				"VIGILANCY_RESOURCES", "label.vigilancy.emailConvoke"));
		request.setAttribute("bean", beanWithVigilants);
		return mapping.findForward("confirmConvokes");
	}

	public ActionForward revertConvokes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return forwardBean(mapping, request, "convoke",
				"prepareGenerateConvokes");

	}

	public ActionForward selectVigilantGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return forwardBean(mapping, request, "selectGroup",
				"prepareGenerateConvokes");
	}

	public ActionForward prepareAddMoreVigilants(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String writtenEvalatuionId = request
				.getParameter("writtenEvaluationId");
		WrittenEvaluation writtenEvaluation = (WrittenEvaluation) RootDomainObject
				.readDomainObjectByOID(WrittenEvaluation.class, Integer
						.valueOf(writtenEvalatuionId));

		Person person = getLoggedPerson(request);
		ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
		ExamCoordinator coordinator = person
				.getExamCoordinatorForGivenExecutionYear(executionYear);
		Unit unit = coordinator.getUnit();

		ConvokeBean bean = new ConvokeBean();

		bean.setPerson(person);
		bean.setUnit(unit);
		bean.setExecutionYear(executionYear);
		bean.setExamCoordinator(coordinator);
		bean.setWrittenEvaluation(writtenEvaluation);

		List<VigilantGroup> allGroups = coordinator.getVigilantGroups();

		bean.setVigilantGroups(allGroups);
		VigilantGroup group = writtenEvaluation.getAssociatedExecutionCourses()
				.get(0).getVigilantGroup();
		StrategySugestion sugestion = group
				.sugestVigilantsToConvoke(writtenEvaluation);

		bean.setVigilantsSugestion(sugestion.getVigilantSugestion());
		bean.setUnavailableVigilants(sugestion.getUnavailableVigilants());
		bean.setUnavailableInformation(sugestion
				.getUnavailableVigilantsWithInformation());
		bean.setTeachersForAGivenCourse(sugestion.getVigilantsThatAreTeachersSugestion());
		bean.setSelectedVigilantGroup(group);

		request.setAttribute("bean", bean);
		return mapping.findForward("prepareGenerateConvokes");

	}

	public ActionForward showConvokesByVigilants(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		recoverBeanFromRequest(request);
		return mapping.findForward("prepareEditConvoke");
	}

	public ActionForward showConvokesByEvaluation(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		recoverBeanFromRequest(request);
		return mapping.findForward("prepareEditConvoke");
	}

	private ExamCoordinator getCoordinatorForCurrentYear(
			HttpServletRequest request) throws FenixFilterException,
			FenixServiceException {
		ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
		Person person = getLoggedPerson(request);
		ExamCoordinator coordinator = person
				.getExamCoordinatorForGivenExecutionYear(currentYear);
		return coordinator;
	}

	private ActionForward forwardBean(ActionMapping mapping,
			HttpServletRequest request, String viewStateName, String forwardTo) {
		ConvokeBean bean = (ConvokeBean) RenderUtils
				.getViewState(viewStateName).getMetaObject().getObject();

		RenderUtils.invalidateViewState(viewStateName);
		request.setAttribute("bean", bean);
		return mapping.findForward(forwardTo);
	}

	private void recoverBeanFromRequest(HttpServletRequest request)
			throws FenixFilterException, FenixServiceException {
		ConvokeBean bean = new ConvokeBean();
		String temporalInformation = request
				.getParameter("temporalInformation");
		String vigilantGroup = request.getParameter("gid");
		String incompatiblities = request.getParameter("showIncompatibilities");
		String unavailables = request.getParameter("showUnavailables");
		String convokeInfo = request.getParameter("showConvokeInfo");
		String whatToShow = request.getParameter("whatToShow");
		String bounds = request.getParameter("showBoundsJustification");
		bean
				.setTemporalInformation((temporalInformation != null) ? TemporalInformationType
						.valueOf(temporalInformation)
						: TemporalInformationType.ALL);
		bean.setShowInformationByVigilant((whatToShow != null) ? whatToShow
				.equals("vigilants") : Boolean.TRUE);
		bean.setShowIncompatibilities((incompatiblities != null) ? Boolean
				.valueOf(incompatiblities) : Boolean.FALSE);
		bean.setShowUnavailables((unavailables != null) ? Boolean
				.valueOf(unavailables) : Boolean.FALSE);
		bean.setShowBoundsJustification( (bounds!=null) ? Boolean.valueOf(bounds) : Boolean.FALSE);
		bean.setShowAllVigilancyInfo((convokeInfo != null) ? Boolean
				.valueOf(convokeInfo) : Boolean.FALSE);

		ExamCoordinator coordinator = getCoordinatorForCurrentYear(request);
		bean.setExamCoordinator(coordinator);
		bean.setVigilantGroups(coordinator.getVigilantGroups());

		VigilantGroup group = null;
		if (vigilantGroup != null) {
			group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(
					VigilantGroup.class, Integer.valueOf(vigilantGroup));
			bean.setSelectedVigilantGroup(group);
		}

		putInformationOnRequest(request, group, bean.getTemporalInformation(),
				bean.isShowInformationByVigilant());
		request.setAttribute("bean", bean);
	}

	private void putInformationOnRequest(HttpServletRequest request,
			TemporalInformationType temporalInformation, boolean showVigilants)
			throws FenixFilterException, FenixServiceException {
		ExamCoordinator coordinator = getCoordinatorForCurrentYear(request);
		if (showVigilants) {
			request.setAttribute("vigilants", coordinator
					.getVigilantsThatCanManage());
		} else {
			if (temporalInformation.equals(TemporalInformationType.PAST)) {
				request
						.setAttribute(
								"writtenEvaluations",
								coordinator
										.getAssociatedWrittenEvaluationsBeforeDate(new DateTime()));
			} else {
				if (temporalInformation.equals(TemporalInformationType.FUTURE)) {
					request
							.setAttribute(
									"writtenEvaluations",
									coordinator
											.getAssociatedWrittenEvaluationsAfterDate(new DateTime()));
				} else {
					request.setAttribute("writtenEvaluations", coordinator
							.getAssociatedWrittenEvaluations());
				}
			}
		}

	}

	private void putInformationOnRequest(HttpServletRequest request,
			VigilantGroup group, TemporalInformationType temporalInformation,
			boolean showVigilants) throws FenixFilterException,
			FenixServiceException {
		if (group == null) {
			putInformationOnRequest(request, temporalInformation, showVigilants);
		} else {
			if (showVigilants) {
				request.setAttribute("vigilants", group.getVigilants());
			} else {
				if (temporalInformation.equals(TemporalInformationType.PAST)) {
					request.setAttribute("writtenEvaluations", group
							.getWrittenEvaluationsBeforeDate(new DateTime()));
				} else {
					if (temporalInformation
							.equals(TemporalInformationType.FUTURE)) {
						request
								.setAttribute(
										"writtenEvaluations",
										group
												.getWrittenEvaluationsAfterDate(new DateTime()));
					} else {
						request.setAttribute("writtenEvaluations", group
								.getWrittenEvaluations());
					}
				}
			}
		}
	}
}