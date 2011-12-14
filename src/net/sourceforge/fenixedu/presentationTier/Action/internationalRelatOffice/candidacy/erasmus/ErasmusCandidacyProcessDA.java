package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.candidacy.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinator;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancy;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancyBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.SendReceptionEmailBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;


import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingErasmusCandidacyProcess", module = "internationalRelatOffice", formBeanClass = ErasmusCandidacyProcessDA.ErasmusCandidacyProcessForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/candidacy/erasmus/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
	@Forward(name = "view-university-agreements", path = "/candidacy/erasmus/viewErasmusVacancies.jsp"),
	@Forward(name = "insert-university-agreement", path = "/candidacy/erasmus/insertErasmusVacancy.jsp"),
	@Forward(name = "view-erasmus-coordinators", path = "/candidacy/erasmus/viewErasmusCoordinators.jsp"),
	@Forward(name = "assign-coordinator", path = "/candidacy/erasmus/assignCoordinator.jsp"),
	@Forward(name = "send-reception-email-present-individual-processes", path = "/candidacy/erasmus/reception/sendReceptionEmailPresentIndividualProcesses.jsp"),
	@Forward(name = "send-reception-email-edit-individual-candidacies", path = "/candidacy/erasmus/reception/sendReceptionEmailEditIndividualCandidacies.jsp"),
	@Forward(name = "edit-reception-email-message", path = "/candidacy/erasmus/reception/editReceptionEmailMessage.jsp"),
	@Forward(name = "view-email-to-send", path = "/candidacy/erasmus/reception/viewEmailToSend.jsp"),
	@Forward(name = "email-sent-with-success", path = "/candidacy/erasmus/reception/emailSentWithSuccess.jsp") })
public class ErasmusCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.ErasmusCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	I18NFilter.setLocale(request, request.getSession(true), Locale.ENGLISH);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected ErasmusCandidacyProcess getProcess(HttpServletRequest request) {
	return (ErasmusCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
	List<IndividualCandidacyProcess> processes = process.getChildProcesses();
	List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses = new ArrayList<IndividualCandidacyProcess>();
	Degree selectedDegree = getChooseDegreeBean(request).getDegree();

	for (IndividualCandidacyProcess child : processes) {
	    if ((selectedDegree == null)
		    || ((ErasmusIndividualCandidacyProcess) child).getCandidacy().getSelectedDegree() == selectedDegree) {
		selectedDegreesIndividualCandidacyProcesses.add(child);
	    }
	}

	return selectedDegreesIndividualCandidacyProcesses;
    }

    public ActionForward prepareExecuteViewErasmusVancacies(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("view-university-agreements");
    }

    public ActionForward prepareExecuteInsertErasmusVacancy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("erasmusVacancyBean", new ErasmusVacancyBean());

	return mapping.findForward("insert-university-agreement");
    }

    public ActionForward prepareExecuteInsertErasmusVacancyInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareExecuteInsertErasmusVacancy(mapping, form, request, response);
    }

    public ActionForward executeInsertErasmusVacancy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusCandidacyProcess process = getProcess(request);
	ErasmusVacancyBean bean = getErasmusVacancyBean();

	if (process.getCandidacyPeriod().existsFor(bean.getUniversity(), bean.getDegree())) {
	    addActionMessage(request, "error.erasmus.insert.vacancy.already.exists");
	    return mapping.findForward("insert-university-agreement");
	}

	executeActivity(getProcess(request), "InsertErasmusVacancy", bean);

	return prepareExecuteViewErasmusVancacies(mapping, form, request, response);
    }

    public ActionForward selectCountryForVacancyInsertion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusVacancyBean bean = getErasmusVacancyBean();

	RenderUtils.invalidateViewState();

	request.setAttribute("erasmusVacancyBean", bean);

	return mapping.findForward("insert-university-agreement");
    }

    public ActionForward executeRemoveVacancy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusVacancy vacancy = getDomainObject(request, "vacancyExternalId");

	if (vacancy.hasAnyCandidacies()) {
	    addActionMessage(request, "error.erasmus.vacancy.has.associated.candidacies");
	    return prepareExecuteViewErasmusVancacies(mapping, form, request, response);
	}

	executeActivity(getProcess(request), "RemoveErasmusVacancy", new ErasmusVacancyBean(vacancy));

	return prepareExecuteViewErasmusVancacies(mapping, form, request, response);
    }

    public ActionForward prepareExecuteViewErasmusCoordinators(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("view-erasmus-coordinators");
    }

    public ActionForward prepareExecuteAssignCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("erasmusCoordinatorBean", new ErasmusCoordinatorBean());

	return mapping.findForward("assign-coordinator");
    }

    public ActionForward executeAssignCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusCoordinatorBean bean = getErasmusCoordinatorBean();

	try {
	    executeActivity(getProcess(request), "AssignCoordinator", bean);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());

	    return prepareExecuteAssignCoordinator(mapping, form, request, response);
	}

	return prepareExecuteViewErasmusCoordinators(mapping, form, request, response);
    }

    public ActionForward executeAssignCoordinatorInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareExecuteAssignCoordinator(mapping, form, request, response);
    }

    public ActionForward executeRemoveTeacherFromCoordinators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ErasmusCoordinator coordinator = getErasmusCoordinator(request);

	executeActivity(getProcess(request), "RemoveTeacherFromCoordinators", new ErasmusCoordinatorBean(coordinator));

	return prepareExecuteViewErasmusCoordinators(mapping, form, request, response);
    }

    public ActionForward executeRemoveTeacherFromCoordinatorsInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareExecuteViewErasmusCoordinators(mapping, form, request, response);
    }

    public ActionForward searchTeacherByNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusCoordinatorBean bean = getErasmusCoordinatorBean();
	Teacher teacher = Teacher.readByIstId(bean.getTeacherId());

	bean.setTeacher(teacher);
	request.setAttribute("erasmusCoordinatorBean", bean);

	return mapping.findForward("assign-coordinator");
    }

    public ActionForward executeSendEmailToMissingRequiredDocumentsProcesses(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	executeActivity(getProcess(request), "SendEmailToMissingRequiredDocumentsProcesses", null);

	return prepareExecuteViewChildProcessWithMissingRequiredDocumentFiles(mapping, form, request, response);
    }

    private ErasmusCoordinator getErasmusCoordinator(HttpServletRequest request) {
	return (ErasmusCoordinator) getDomainObject(request, "erasmusCoordinatorExternalId");
    }

    private ErasmusCoordinatorBean getErasmusCoordinatorBean() {
	return getRenderedObject("erasmus.coordinator.bean");
    }

    private ErasmusVacancyBean getErasmusVacancyBean() {
	return getRenderedObject("erasmus.vacancy.bean");
    }

    protected List<Activity> getAllowedActivities(final CandidacyProcess process) {
	List<Activity> activities = process.getAllowedActivities(AccessControl.getUserView());
	ArrayList<Activity> resultActivities = new ArrayList<Activity>();

	for (Activity activity : activities) {
	    if (activity.isVisibleForGriOffice()) {
		resultActivities.add(activity);
	    }
	}

	return resultActivities;
    }

    public ActionForward prepareExecuteSendReceptionEmail(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();

	if (bean == null) {
	    bean = new SendReceptionEmailBean(getProcess(request));
	    bean.retrieveProcesses();
	}

	RenderUtils.invalidateViewState("send.reception.email.bean");
	RenderUtils.invalidateViewState("send.reception.email.bean.edit");

	request.setAttribute("sendReceptionEmailBean", bean);

	return mapping.findForward("send-reception-email-present-individual-processes");
    }

    public ActionForward prepareEditReceptionEmailMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();
	request.setAttribute("sendReceptionEmailBean", bean);

	return mapping.findForward("edit-reception-email-message");
    }

    public ActionForward editReceptionEmailMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	executeActivity(getProcess(request), "EditReceptionEmailMessage", getRenderedSendReceptionEmailBean());

	return prepareExecuteSendReceptionEmail(mapping, form, request, response);
    }

    public ActionForward editReceptionEmailMessageInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();
	request.setAttribute("sendReceptionEmailBean", bean);

	return mapping.findForward("edit-reception-email-message");
    }

    public ActionForward viewEmailToSend(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();
	request.setAttribute("sendReceptionEmailBean", bean);

	return mapping.findForward("view-email-to-send");
    }

    public ActionForward sendReceptionEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();
	executeActivity(getProcess(request), "SendReceptionEmail", bean);

	return mapping.findForward("email-sent-with-success");
    }

    public ActionForward sendReceptionEmailSetFilter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();
	bean.retrieveProcesses();

	return prepareExecuteSendReceptionEmail(mapping, actionForm, request, response);
    }

    public ActionForward sendReceptionEmailEditIndividualCandidacies(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();

	RenderUtils.invalidateViewState("send.reception.email.bean");
	RenderUtils.invalidateViewState("send.reception.email.bean.edit");

	request.setAttribute("validIndividualProcesses", getProcess(request).getValidErasmusIndividualCandidacies());
	request.setAttribute("sendReceptionEmailBean", bean);

	setSelectedIndividualProcesses((ErasmusCandidacyProcessForm) actionForm, bean);

	return mapping.findForward("send-reception-email-edit-individual-candidacies");
    }

    public ActionForward sendReceptionEmailSetSelectedIndividualProcesses(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	SendReceptionEmailBean bean = getRenderedSendReceptionEmailBean();

	List<ErasmusIndividualCandidacyProcess> selectedIndividual = retrieveSelectedProcesses((ErasmusCandidacyProcessForm) actionForm);

	bean.setSubjectProcesses(selectedIndividual);

	return prepareExecuteSendReceptionEmail(mapping, actionForm, request, response);
    }

    private void setSelectedIndividualProcesses(ErasmusCandidacyProcessForm actionForm, SendReceptionEmailBean bean) {
	List<String> externalIdList = new ArrayList<String>();

	for (ErasmusIndividualCandidacyProcess individualProcess : bean.getSubjectProcesses()) {
	    externalIdList.add(individualProcess.getExternalId());
	}

	actionForm.setSelectedProcesses(externalIdList.toArray(new String[0]));
    }

    private List<ErasmusIndividualCandidacyProcess> retrieveSelectedProcesses(ErasmusCandidacyProcessForm actionForm) {
	List<ErasmusIndividualCandidacyProcess> processList = new ArrayList<ErasmusIndividualCandidacyProcess>();

	for (String externalId : actionForm.getSelectedProcesses()) {
	    processList.add((ErasmusIndividualCandidacyProcess) ErasmusIndividualCandidacyProcess.fromExternalId(externalId));
	}

	return processList;
    }

    private SendReceptionEmailBean getRenderedSendReceptionEmailBean() {
	return getRenderedObject("send.reception.email.bean");
    }

    public static class UniversityUnitsProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    ErasmusVacancyBean bean = (ErasmusVacancyBean) source;

	    CountryUnit selectedCountryUnit = CountryUnit.getCountryUnitByCountry(bean.getCountry());

	    if (selectedCountryUnit == null) {
		return new ArrayList<Unit>();
	    }

	    java.util.ArrayList<Unit> associatedUniversityUnits = new java.util.ArrayList<Unit>(selectedCountryUnit
		    .getSubUnits(PartyTypeEnum.UNIVERSITY));

	    Collections.sort(associatedUniversityUnits, new BeanComparator("nameI18n"));

	    return associatedUniversityUnits;
	}

    }

    public static class ActiveErasmusIndividualCandidaciesForSendReceptionProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    SendReceptionEmailBean bean = (SendReceptionEmailBean) source;
	    ErasmusCandidacyProcess candidacyProcess = bean.getErasmusCandidacyProcess();
	    return null;
	}

    }

}
