package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.candidacy.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinatorBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancyBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.SendReceptionEmailBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityCoordinator;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplate;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateType;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityQuota;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingMobilityApplicationProcess", module = "internationalRelatOffice", formBeanClass = ErasmusCandidacyProcessDA.ErasmusCandidacyProcessForm.class)
@Forwards({
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
	@Forward(name = "email-sent-with-success", path = "/candidacy/erasmus/reception/emailSentWithSuccess.jsp"),
	@Forward(name = "manageEmailTemplates", path = "/candidacy/erasmus/emailTemplates/manageEmailTemplates.jsp"),
	@Forward(name = "previewEmailTemplate", path = "/candidacy/erasmus/emailTemplates/previewEmailTemplate.jsp") })
public class ErasmusCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.ErasmusCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	I18NFilter.setLocale(request, request.getSession(true), Locale.ENGLISH);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected MobilityApplicationProcess getProcess(HttpServletRequest request) {
	return (MobilityApplicationProcess) super.getProcess(request);
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
	List<IndividualCandidacyProcess> processes = process.getChildProcesses();
	List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses = new ArrayList<IndividualCandidacyProcess>();
	Degree selectedDegree = getChooseDegreeBean(request).getDegree();
	MobilityProgram selectedProgram = getChooseMobilityProgramBean(request).getMobilityProgram();

	for (IndividualCandidacyProcess child : processes) {
	    if ((selectedDegree == null)
		    || ((MobilityIndividualApplicationProcess) child).getCandidacy().getSelectedDegree() == selectedDegree) {

		if ((selectedProgram == null)
			|| ((MobilityIndividualApplicationProcess) child).getCandidacy().getMobilityStudentData()
				.getSelectedOpening().getMobilityAgreement().getMobilityProgram() == selectedProgram) {
		    selectedDegreesIndividualCandidacyProcesses.add(child);
		}
	    }
	}

	return selectedDegreesIndividualCandidacyProcesses;
    }

    public ActionForward prepareExecuteViewMobilityQuota(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	return prepareExecuteViewMobilityQuota(mapping, form, request, response, null);
    }

    public ActionForward prepareExecuteViewMobilityQuota(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response, final MobilityProgram program) {
	ErasmusVacancyBean erasmusVacancyBean = new ErasmusVacancyBean(program);
	request.setAttribute("erasmusVacancyBean", erasmusVacancyBean);

	MobilityApplicationProcess process = getProcess(request);

	if (erasmusVacancyBean.getMobilityProgram() != null) {
	    List<MobilityQuota> mobilityQuotasByProgram = process.getApplicationPeriod().getMobilityQuotasByProgram(
		    erasmusVacancyBean.getMobilityProgram());
	    request.setAttribute("quotas", mobilityQuotasByProgram);
	}

	return mapping.findForward("view-university-agreements");
    }

    public ActionForward selectMobilityQuotaForQuotasManagementPostback(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ErasmusVacancyBean erasmusVacancyBean = getErasmusVacancyBean();

	request.setAttribute("erasmusVacancyBean", erasmusVacancyBean);

	MobilityApplicationProcess process = getProcess(request);

	if (erasmusVacancyBean.getMobilityProgram() != null) {
	    List<MobilityQuota> mobilityQuotasByProgram = process.getApplicationPeriod().getMobilityQuotasByProgram(
		    erasmusVacancyBean.getMobilityProgram());
	    request.setAttribute("quotas", mobilityQuotasByProgram);
	}

	return mapping.findForward("view-university-agreements");
    }

    public ActionForward prepareExecuteInsertMobilityQuota(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MobilityProgram program = getDomainObject(request, "mobilityProgramId");

	request.setAttribute("erasmusVacancyBean", new ErasmusVacancyBean(program));

	return mapping.findForward("insert-university-agreement");
    }

    public ActionForward prepareExecuteInsertMobilityQuotaInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("erasmusVacancyBean", getErasmusVacancyBean());

	return mapping.findForward("insert-university-agreement");
    }

    public ActionForward executeInsertMobilityQuota(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	MobilityApplicationProcess process = getProcess(request);
	ErasmusVacancyBean bean = getErasmusVacancyBean();

	if (process.getCandidacyPeriod().existsFor(bean.getMobilityAgreement(), bean.getDegree())) {
	    addActionMessage(request, "error.erasmus.insert.vacancy.already.exists");
	    return mapping.findForward("insert-university-agreement");
	}

	executeActivity(getProcess(request), "InsertMobilityQuota", bean);

	return prepareExecuteViewMobilityQuota(mapping, form, request, response, bean.getMobilityProgram());
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
	MobilityQuota quota = getDomainObject(request, "vacancyExternalId");

	if (quota.hasAnyApplications()) {
	    addActionMessage(request, "error.erasmus.vacancy.has.associated.candidacies");
	    return prepareExecuteViewMobilityQuota(mapping, form, request, response);
	}

	executeActivity(getProcess(request), "RemoveMobilityQuota", new ErasmusVacancyBean(quota));

	return prepareExecuteViewMobilityQuota(mapping, form, request, response);
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
	MobilityCoordinator coordinator = getErasmusCoordinator(request);

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

    private MobilityCoordinator getErasmusCoordinator(HttpServletRequest request) {
	return (MobilityCoordinator) getDomainObject(request, "erasmusCoordinatorExternalId");
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

	if (bean == null) {
	    bean = new SendReceptionEmailBean(getProcess(request));
	}

	List<MobilityIndividualApplicationProcess> selectedIndividual = retrieveSelectedProcesses((ErasmusCandidacyProcessForm) actionForm);

	if (selectedIndividual.isEmpty()) {
	    bean.retrieveProcesses();
	} else {
	    bean.setSubjectProcesses(selectedIndividual);
	}

	return prepareExecuteSendReceptionEmail(mapping, actionForm, request, response);
    }

    private void setSelectedIndividualProcesses(ErasmusCandidacyProcessForm actionForm, SendReceptionEmailBean bean) {
	List<String> externalIdList = new ArrayList<String>();

	for (MobilityIndividualApplicationProcess individualProcess : bean.getSubjectProcesses()) {
	    externalIdList.add(individualProcess.getExternalId());
	}

	actionForm.setSelectedProcesses(externalIdList.toArray(new String[0]));
    }

    private List<MobilityIndividualApplicationProcess> retrieveSelectedProcesses(ErasmusCandidacyProcessForm actionForm) {
	List<MobilityIndividualApplicationProcess> processList = new ArrayList<MobilityIndividualApplicationProcess>();

	if (actionForm.getSelectedProcesses() != null) {
	    for (String externalId : actionForm.getSelectedProcesses()) {
		processList.add((MobilityIndividualApplicationProcess) MobilityIndividualApplicationProcess
			.fromExternalId(externalId));
	    }
	}

	return processList;
    }

    private SendReceptionEmailBean getRenderedSendReceptionEmailBean() {
	return getRenderedObject("send.reception.email.bean");
    }

    public ActionForward manageEmailTemplates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	MobilityEmailTemplateBean bean = new MobilityEmailTemplateBean();
	MobilityApplicationProcess process = getProcess(request);
	Set<MobilityProgram> mobilityPrograms = process.getCandidacyPeriod().getMobilityPrograms();

	String templateType = request.getParameter("template");
	if (templateType != null && templateType.equals("Reception")) {
	    bean.setType(MobilityEmailTemplateType.IST_RECEPTION);
	    MobilityEmailTemplate emailTemplate = process.getCandidacyPeriod().getEmailTemplateFor(bean.getType());
	    bean.setSubject(emailTemplate.getSubject());
	    bean.setBody(emailTemplate.getBody());
	}

	RenderUtils.invalidateViewState();

	request.setAttribute("mobilityEmailTemplateBean", bean);
	request.setAttribute("hasMobilityPrograms", !mobilityPrograms.isEmpty());

	return mapping.findForward("manageEmailTemplates");
    }

    public ActionForward manageEmailTemplatesPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	MobilityEmailTemplateBean bean = getMobilityEmailTemplateBean();
	MobilityApplicationProcess process = getProcess(request);
	MobilityApplicationPeriod candidacyPeriod = process.getCandidacyPeriod();
	MobilityEmailTemplate emailTemplate = candidacyPeriod.getEmailTemplateFor(bean.getType());

	if (emailTemplate != null) {
	    bean.setSubject(emailTemplate.getSubject());
	    bean.setBody(emailTemplate.getBody());
	} else {
	    bean.setSubject("");
	    bean.setBody("");
	}

	request.setAttribute("mobilityEmailTemplateBean", bean);
	request.setAttribute("hasMobilityPrograms", true);

	RenderUtils.invalidateViewState();

	return mapping.findForward("manageEmailTemplates");
    }

    public ActionForward manageEmailTemplatesInvalid(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	MobilityEmailTemplateBean bean = getMobilityEmailTemplateBean();

	request.setAttribute("mobilityEmailTemplateBean", bean);
	request.setAttribute("hasMobilityPrograms", true);

	RenderUtils.invalidateViewState();

	return mapping.findForward("manageEmailTemplates");
    }

    public ActionForward previewEmailTemplate(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	MobilityEmailTemplateBean bean = getMobilityEmailTemplateBean();

	request.setAttribute("mobilityEmailTemplateBean", bean);

	RenderUtils.invalidateViewState();

	return mapping.findForward("previewEmailTemplate");
    }

    public ActionForward editEmailTemplate(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	MobilityEmailTemplateBean bean = getMobilityEmailTemplateBean();
	MobilityApplicationProcess process = getProcess(request);

	process.getCandidacyPeriod().editEmailTemplates(bean);

	return manageEmailTemplates(mapping, actionForm, request, response);
    }

    private MobilityEmailTemplateBean getMobilityEmailTemplateBean() {
	return getRenderedObject("mobilityEmailTemplateBean");
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

	    java.util.ArrayList<Unit> associatedUniversityUnits = new java.util.ArrayList<Unit>(
		    selectedCountryUnit.getSubUnits(PartyTypeEnum.UNIVERSITY));

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
	    MobilityApplicationProcess candidacyProcess = bean.getMobilityApplicationProcess();
	    return null;
	}

    }

}
