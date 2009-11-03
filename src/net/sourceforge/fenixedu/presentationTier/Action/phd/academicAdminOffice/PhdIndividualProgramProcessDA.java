package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.Servico.fileManager.StorePersonalPhoto;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean.UnableToProcessTheImage;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanBean;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntry;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntryBean;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddAssistantGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddJobInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddQualification;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddStudyPlan;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddStudyPlanEntry;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.CancelPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteAssistantGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteJobInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteQualification;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteStudyPlan;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteStudyPlanEntry;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditIndividualProcessInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditQualificationExams;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditStudyPlan;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.FlunkedPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.NotAdmittedPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.RequestPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.SuspendPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlertBean;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdIndividualProgramProcess", module = "academicAdminOffice")
@Forwards( {

	@Forward(name = "manageProcesses", path = "/phd/academicAdminOffice/manageProcesses.jsp"),

	@Forward(name = "viewProcess", path = "/phd/academicAdminOffice/viewProcess.jsp"),

	@Forward(name = "editPersonalInformation", path = "/phd/academicAdminOffice/editPersonalInformation.jsp"),

	@Forward(name = "editQualificationsAndJobsInformation", path = "/phd/academicAdminOffice/editQualificationsAndJobsInformation.jsp"),

	@Forward(name = "editPhdIndividualProgramProcessInformation", path = "/phd/academicAdminOffice/editPhdIndividualProgramProcessInformation.jsp"),

	@Forward(name = "manageGuidingInformation", path = "/phd/academicAdminOffice/manageGuidingInformation.jsp"),

	@Forward(name = "managePhdIndividualProgramProcessState", path = "/phd/academicAdminOffice/managePhdIndividualProgramProcessState.jsp"),

	@Forward(name = "manageAlerts", path = "/phd/academicAdminOffice/manageAlerts.jsp"),

	@Forward(name = "createCustomAlert", path = "/phd/academicAdminOffice/createCustomAlert.jsp"),

	@Forward(name = "viewAlertMessages", path = "/phd/academicAdminOffice/viewAlertMessages.jsp"),

	@Forward(name = "viewProcessAlertMessages", path = "/phd/academicAdminOffice/viewProcessAlertMessages.jsp"),

	@Forward(name = "manageStudyPlan", path = "/phd/academicAdminOffice/manageStudyPlan.jsp"),

	@Forward(name = "createStudyPlan", path = "/phd/academicAdminOffice/createStudyPlan.jsp"),

	@Forward(name = "editStudyPlan", path = "/phd/academicAdminOffice/editStudyPlan.jsp"),

	@Forward(name = "createStudyPlanEntry", path = "/phd/academicAdminOffice/createStudyPlanEntry.jsp"),

	@Forward(name = "editQualificationExams", path = "/phd/academicAdminOffice/editQualificationExams.jsp"),

	@Forward(name = "uploadPhoto", path = "/phd/academicAdminOffice/uploadPhoto.jsp"),

	@Forward(name = "requestPublicPresentationSeminarComission", path = "/phd/academicAdminOffice/requestPublicPresentationSeminarComission.jsp")

})
public class PhdIndividualProgramProcessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final PhdIndividualProgramProcess process = getProcess(request);

	if (process != null) {
	    request.setAttribute("processAlertMessagesToNotify", process.getUnreadedAlertMessagesFor(getLoggedPerson(request)));
	}

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");

	if (searchBean == null) {
	    searchBean = new SearchPhdIndividualProgramProcessBean();
	    searchBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	    searchBean.setFilterPhdPrograms(false);
	    searchBean.setFilterPhdProcesses(false);
	}

	request.setAttribute("searchProcessBean", searchBean);
	request.setAttribute("processes", PhdIndividualProgramProcess.search(searchBean));

	return mapping.findForward("manageProcesses");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	if (process != null && process.hasRegistration()) {
	    request.setAttribute("registrationConclusionBean", new RegistrationConclusionBean(process.getRegistration(),
		    CycleType.THIRD_CYCLE));
	}

	return mapping.findForward("viewProcess");
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	return (PhdIndividualProgramProcess) super.getProcess(request);
    }

    // Edit Personal Information
    public ActionForward prepareEditPersonalInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PersonBean personBean = new PersonBean(getProcess(request).getPerson());
	setIsEmployeeAttributeAndMessage(request, personBean.getPerson());
	request.setAttribute("editPersonalInformationBean", personBean);
	return mapping.findForward("editPersonalInformation");
    }

    public ActionForward prepareEditPersonalInformationInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("editPersonalInformationBean", getEditPersonalInformationBean());
	return mapping.findForward("editPersonalInformation");
    }

    private PersonBean getEditPersonalInformationBean() {
	return (PersonBean) getRenderedObject("editPersonalInformationBean");
    }

    public ActionForward editPersonalInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (!validateAreaCodeAndAreaOfAreaCode(request, getEditPersonalInformationBean().getPerson(),
		getEditPersonalInformationBean().getCountryOfResidence(), getEditPersonalInformationBean().getAreaCode(),
		getEditPersonalInformationBean().getAreaOfAreaCode())) {

	    setIsEmployeeAttributeAndMessage(request, getEditPersonalInformationBean().getPerson());

	    request.setAttribute("editPersonalInformationBean", getEditPersonalInformationBean());

	    return mapping.findForward("editPersonalInformation");
	}

	request.setAttribute("editPersonalInformationBean", getEditPersonalInformationBean());
	return executeActivity(EditPersonalInformation.class, getEditPersonalInformationBean(), request, mapping,
		"editPersonalInformation", "viewProcess", "message.personal.data.edited.with.success");
    }

    public ActionForward cancelEditPersonalInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("viewProcess");
    }

    // End of Edit Personal Information

    // Edit Qualifications and Jobs information
    private void addQualificationsAndJobsContextInformation(HttpServletRequest request) {
	final Person person = getProcess(request).getPerson();
	request.setAttribute("qualifications", person.getAssociatedQualifications());
	request.setAttribute("jobs", person.getJobs());
    }

    public ActionForward prepareEditQualificationsAndJobsInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	addQualificationsAndJobsContextInformation(request);
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    // Qualifications
    public ActionForward prepareAddQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	addQualificationsAndJobsContextInformation(request);
	request.setAttribute("qualification", new QualificationBean());
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	addQualificationsAndJobsContextInformation(request);
	final Object bean = getRenderedObject("qualification");

	try {
	    ExecuteProcessActivity.run(getProcess(request), AddQualification.class.getSimpleName(), bean);
	    addSuccessMessage(request, "message.qualification.information.create.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("qualification", bean);
	}
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addQualificationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	addQualificationsAndJobsContextInformation(request);
	request.setAttribute("qualification", getRenderedObject("qualification"));
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward deleteQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	addQualificationsAndJobsContextInformation(request);
	return executeActivity(DeleteQualification.class, getDomainObject(request, "qualificationId"), request, mapping,
		"editQualificationsAndJobsInformation", "editQualificationsAndJobsInformation",
		"message.qualification.information.delete.success");
    }

    // Jobs
    public ActionForward prepareAddJobInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	addQualificationsAndJobsContextInformation(request);
	final JobBean bean = new JobBean();
	bean.setCountry(Country.readDefault());
	request.setAttribute("job", bean);
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addJobInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	addQualificationsAndJobsContextInformation(request);
	final Object bean = getRenderedObject("job");

	try {
	    ExecuteProcessActivity.run(getProcess(request), AddJobInformation.class.getSimpleName(), bean);
	    addSuccessMessage(request, "message.job.information.create.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("job", bean);
	}
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addJobInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	addQualificationsAndJobsContextInformation(request);
	request.setAttribute("job", getRenderedObject("job"));
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward addJobInformationPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	addQualificationsAndJobsContextInformation(request);
	request.setAttribute("job", getRenderedObject("job"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("editQualificationsAndJobsInformation");
    }

    public ActionForward deleteJobInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	addQualificationsAndJobsContextInformation(request);
	return executeActivity(DeleteJobInformation.class, getDomainObject(request, "jobId"), request, mapping,
		"editQualificationsAndJobsInformation", "editQualificationsAndJobsInformation",
		"message.job.information.delete.success");
    }

    // End of Qualifications and Jobs information

    // Phd individual program process information
    public ActionForward prepareEditPhdIndividualProgramProcessInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("phdIndividualProgramProcessBean", new PhdIndividualProgramProcessBean(getProcess(request)));
	return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("phdIndividualProgramProcessBean", getRenderedObject("phdIndividualProgramProcessBean"));
	return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformationPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("phdIndividualProgramProcessBean", getRenderedObject("phdIndividualProgramProcessBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("editPhdIndividualProgramProcessInformation");
    }

    public ActionForward editPhdIndividualProgramProcessInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) getRenderedObject("phdIndividualProgramProcessBean");
	request.setAttribute("phdIndividualProgramProcessBean", bean);

	if (!bean.isCollaborationInformationCorrect()) {
	    addErrorMessage(request, "message.phdIndividualProgramProcessInformation.invalid.collaboration");
	    return mapping.findForward("editPhdIndividualProgramProcessInformation");
	}

	try {
	    ExecuteProcessActivity.run(getProcess(request), EditIndividualProcessInformation.class.getSimpleName(), bean);
	    addSuccessMessage(request, "message.phdIndividualProgramProcessInformation.edit.success");
	    return viewProcess(mapping, actionForm, request, response);

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return mapping.findForward("editPhdIndividualProgramProcessInformation");
	}
    }

    // End of Phd individual program process information

    // Phd guiding information
    private void addGuidingsContextInformation(ActionMapping mapping, HttpServletRequest request) {
	request.setAttribute("guidings", getProcess(request).getGuidings());
	request.setAttribute("assistantGuidings", getProcess(request).getAssistantguidings());
    }

    public ActionForward prepareManageGuidingInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	addGuidingsContextInformation(mapping, request);
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddGuidingInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	addGuidingsContextInformation(mapping, request);
	request.setAttribute("guidingBean", new PhdProgramGuidingBean());
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddGuidingInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	addGuidingsContextInformation(mapping, request);
	request.setAttribute("guidingBean", getRenderedObject("guidingBean"));
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddGuidingInformationSelectType(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	addGuidingsContextInformation(mapping, request);
	request.setAttribute("guidingBean", getRenderedObject("guidingBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward addGuidingInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramGuidingBean bean = (PhdProgramGuidingBean) getRenderedObject("guidingBean");
	try {
	    ExecuteProcessActivity.run(getProcess(request), AddGuidingInformation.class.getSimpleName(), bean);
	    addSuccessMessage(request, "message.guiding.created.with.success");

	} catch (DomainException e) {
	    request.setAttribute("guidingBean", bean);
	    addErrorMessage(request, e.getKey(), e.getArgs());
	}

	addGuidingsContextInformation(mapping, request);
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward deleteGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), DeleteGuiding.class.getSimpleName(), getDomainObject(request,
		    "guidingId"));
	    addSuccessMessage(request, "message.guiding.deleted.with.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	}

	addGuidingsContextInformation(mapping, request);
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddAssistantGuidingInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	addGuidingsContextInformation(mapping, request);
	request.setAttribute("assistantGuidingBean", new PhdProgramGuidingBean());
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddAssistantGuidingInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	addGuidingsContextInformation(mapping, request);
	request.setAttribute("assistantGuidingBean", getRenderedObject("assistantGuidingBean"));
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward prepareAddAssistantGuidingInformationSelectType(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	addGuidingsContextInformation(mapping, request);
	request.setAttribute("assistantGuidingBean", getRenderedObject("assistantGuidingBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward addAssistantGuidingInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramGuidingBean bean = (PhdProgramGuidingBean) getRenderedObject("assistantGuidingBean");
	try {
	    ExecuteProcessActivity.run(getProcess(request), AddAssistantGuidingInformation.class.getSimpleName(), bean);
	    addSuccessMessage(request, "message.assistant.guiding.created.with.success");

	} catch (DomainException e) {
	    request.setAttribute("assistantGuidingBean", bean);
	    addErrorMessage(request, e.getKey(), e.getArgs());
	}

	addGuidingsContextInformation(mapping, request);
	return mapping.findForward("manageGuidingInformation");
    }

    public ActionForward deleteAssistantGuiding(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), DeleteAssistantGuiding.class.getSimpleName(), getDomainObject(
		    request, "assistantGuidingId"));
	    addSuccessMessage(request, "message.assistant.guiding.deleted.with.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	}

	addGuidingsContextInformation(mapping, request);
	return mapping.findForward("manageGuidingInformation");
    }

    // End of Phd guiding information

    // change phd individual program process state

    public ActionForward managePhdIndividualProgramProcessState(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final PhdIndividualProgramProcessBean bean = new PhdIndividualProgramProcessBean();
	bean.setIndividualProgramProcess(getProcess(request));
	request.setAttribute("processBean", bean);
	return mapping.findForward("managePhdIndividualProgramProcessState");
    }

    public ActionForward managePhdIndividualProgramProcessStateInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("processBean", getRenderedObject("processBean"));
	return mapping.findForward("managePhdIndividualProgramProcessState");
    }

    public ActionForward changePhdIndividualProgramProcessState(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

	final PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) getRenderedObject("processBean");

	try {
	    switch (bean.getProcessState()) {
	    case CANCELLED:
		ExecuteProcessActivity.run(getProcess(request), CancelPhdProgramProcess.class.getSimpleName(), bean);
		break;

	    case FLUNKED:
		ExecuteProcessActivity.run(getProcess(request), FlunkedPhdProgramProcess.class.getSimpleName(), bean);
		break;

	    case NOT_ADMITTED:
		ExecuteProcessActivity.run(getProcess(request), NotAdmittedPhdProgramProcess.class.getSimpleName(), bean);
		break;

	    case SUSPENDED:
		ExecuteProcessActivity.run(getProcess(request), SuspendPhdProgramProcess.class.getSimpleName(), bean);
		break;

	    default:
		throw new FenixActionException();
	    }
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("processBean", bean);
	    return managePhdIndividualProgramProcessState(mapping, actionForm, request, response);
	}

	return viewProcess(mapping, actionForm, request, response);
    }

    // End of change phd individual program process state

    // Alerts Management

    public ActionForward manageAlerts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("alerts", getAlertsToShow(request));

	return mapping.findForward("manageAlerts");
    }

    public ActionForward prepareCreateCustomAlert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCustomAlertBean", new PhdCustomAlertBean(getProcess(request)));

	return mapping.findForward("createCustomAlert");
    }

    public ActionForward prepareCreateCustomAlertInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCustomAlert", getCreateCustomAlertBean());

	return mapping.findForward("createCustomAlert");
    }

    public ActionForward createCustomAlert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdCustomAlertBean bean = getCreateCustomAlertBean();
	request.setAttribute("createCustomAlertBean", bean);

	final ActionForward result = executeActivity(PhdIndividualProgramProcess.AddCustomAlert.class, bean, request, mapping,
		"createCustomAlert", "manageAlerts", "message.alert.create.with.success");

	request.setAttribute("alerts", getAlertsToShow(request));

	return result;
    }

    private Set<PhdAlert> getAlertsToShow(HttpServletRequest request) {
	return getProcess(request).getActiveAlerts();
    }

    public ActionForward deleteCustomAlert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final ActionForward result = executeActivity(PhdIndividualProgramProcess.DeleteCustomAlert.class, getAlert(request),
		request, mapping, "manageAlerts", "manageAlerts", "message.alert.deleted.with.success");

	request.setAttribute("alerts", getAlertsToShow(request));

	return result;

    }

    private PhdAlert getAlert(HttpServletRequest request) {
	return getDomainObject(request, "alertId");
    }

    private PhdCustomAlertBean getCreateCustomAlertBean() {
	return (PhdCustomAlertBean) getRenderedObject("createCustomAlertBean");
    }

    public ActionForward viewAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("alertMessages", getLoggedPerson(request).getPhdAlertMessages());

	return mapping.findForward("viewAlertMessages");
    }

    public ActionForward markAlertMessageAsReaded(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getAlertMessage(request).markAsReaded(getLoggedPerson(request));

	boolean globalMessagesView = StringUtils.isEmpty(request.getParameter("global"))
		|| request.getParameter("global").equals("true") ? true : false;

	return globalMessagesView ? viewAlertMessages(mapping, form, request, response) : viewProcessAlertMessages(mapping, form,
		request, response);
    }

    private PhdAlertMessage getAlertMessage(HttpServletRequest request) {
	return getDomainObject(request, "alertMessageId");
    }

    public ActionForward viewProcessAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("alertMessages", getProcess(request).getAlertMessagesFor(getLoggedPerson(request)));

	return mapping.findForward("viewProcessAlertMessages");
    }

    // End of Alerts Management

    // Study plan management

    public ActionForward manageStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("manageStudyPlan");
    }

    public ActionForward prepareCreateStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanBean", new PhdStudyPlanBean(getProcess(request)));

	return mapping.findForward("createStudyPlan");
    }

    public ActionForward prepareCreateStudyPlanInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
	return mapping.findForward("createStudyPlan");
    }

    public ActionForward prepareCreateStudyPlanPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
	RenderUtils.invalidateViewState("studyPlanBean");

	return mapping.findForward("createStudyPlan");
    }

    public ActionForward createStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return executeActivity(AddStudyPlan.class, getRenderedObject("studyPlanBean"), request, mapping, "createStudyPlan",
		"manageStudyPlan", "message.study.plan.created.with.success");
    }

    public ActionForward prepareEditStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanBean", new PhdStudyPlanBean(getProcess(request).getStudyPlan()));
	return mapping.findForward("editStudyPlan");
    }

    public ActionForward prepareEditStudyPlanInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
	return mapping.findForward("editStudyPlan");
    }

    public ActionForward prepareEditStudyPlanPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanBean", getRenderedObject("studyPlanBean"));
	RenderUtils.invalidateViewState("studyPlanBean");

	return mapping.findForward("editStudyPlan");
    }

    public ActionForward editStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return executeActivity(EditStudyPlan.class, getRenderedObject("studyPlanBean"), request, mapping, "editStudyPlan",
		"manageStudyPlan", "message.study.plan.edited.with.success");
    }

    public ActionForward prepareCreateStudyPlanEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanEntryBean", new PhdStudyPlanEntryBean(getProcess(request).getStudyPlan()));

	return mapping.findForward("createStudyPlanEntry");

    }

    public ActionForward prepareCreateStudyPlanEntryInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanEntryBean", getStudyPlanEntryBean());

	return mapping.findForward("createStudyPlanEntry");
    }

    public ActionForward createStudyPlanEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanEntryBean", getStudyPlanEntryBean());

	return executeActivity(AddStudyPlanEntry.class, getStudyPlanEntryBean(), request, mapping, "createStudyPlanEntry",
		"manageStudyPlan", "message.study.plan.entry.created.with.success");

    }

    private Object getStudyPlanEntryBean() {
	return getRenderedObject("studyPlanEntryBean");
    }

    public ActionForward studyPlanEntryPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studyPlanEntryBean", getStudyPlanEntryBean());

	RenderUtils.invalidateViewState("studyPlanEntryBean");

	return mapping.findForward("createStudyPlanEntry");
    }

    public ActionForward deleteStudyPlanEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return executeActivity(DeleteStudyPlanEntry.class, getStudyPlanEntry(request), request, mapping, "manageStudyPlan",
		"manageStudyPlan", "message.study.plan.entry.deleted.successfuly");

    }

    private PhdStudyPlanEntry getStudyPlanEntry(HttpServletRequest request) {
	return getDomainObject(request, "studyPlanEntryId");

    }

    public ActionForward deleteStudyPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return executeActivity(DeleteStudyPlan.class, getProcess(request).getStudyPlan(), request, mapping, "manageStudyPlan",
		"manageStudyPlan", "message.study.plan.deleted.successfuly");

    }

    public ActionForward prepareEditQualificationExams(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("editQualificationExamsBean", new PhdIndividualProgramProcessBean(getProcess(request)));

	return mapping.findForward("editQualificationExams");
    }

    public ActionForward prepareEditQualificationExamsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("editQualificationExamsBean", getRenderedObject("editQualificationExamsBean"));

	return mapping.findForward("editQualificationExams");
    }

    public ActionForward editQualificationExams(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("editQualificationExamsBean", getRenderedObject("editQualificationExamsBean"));

	return executeActivity(EditQualificationExams.class, getRenderedObject("editQualificationExamsBean"), request, mapping,
		"editQualificationExams", "manageStudyPlan");

    }

    // End of study plan management

    // Photo Upload

    public ActionForward prepareUploadPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("uploadPhotoBean", new PhotographUploadBean());
	return mapping.findForward("uploadPhoto");
    }

    public ActionForward uploadPhoto(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	final PhotographUploadBean photo = (PhotographUploadBean) getRenderedObject("uploadPhotoBean");

	if (!RenderUtils.getViewState("uploadPhotoBean").isValid()) {
	    addErrorMessage(request, "error.photo.upload.invalid.information");
	    return uploadPhotoInvalid(mapping, actionForm, request, response);
	}

	if (ContentType.getContentType(photo.getContentType()) == null) {
	    addErrorMessage(request, "error.photo.upload.unsupported.file");
	    return uploadPhotoInvalid(mapping, actionForm, request, response);
	}

	try {

	    photo.processImage();

	    StorePersonalPhoto.uploadPhoto(photo, getProcess(request).getPerson());

	    addSuccessMessage(request, "message.photo.updated.with.success");

	} catch (final UnableToProcessTheImage e) {
	    addErrorMessage(request, "error.photo.upload.unable.to.process.image");
	    photo.deleteTemporaryFiles();
	    return uploadPhotoInvalid(mapping, actionForm, request, response);

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    photo.deleteTemporaryFiles();
	    return uploadPhotoInvalid(mapping, actionForm, request, response);
	}

	return viewProcess(mapping, actionForm, request, response);
    }

    public ActionForward uploadPhotoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("uploadPhotoBean", getRenderedObject("uploadPhotoBean"));
	RenderUtils.invalidateViewState("uploadPhotoBean");
	return mapping.findForward("uploadPhoto");
    }

    // End of Photo Upload

    // Request Public Presentation Seminar Comission

    public ActionForward prepareRequestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("requestPublicPresentationSeminarComissionBean", new PublicPresentationSeminarProcessBean());

	return mapping.findForward("requestPublicPresentationSeminarComission");

    }

    public ActionForward prepareRequestPublicPresentationSeminarComissionInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("requestPublicPresentationSeminarComission",
		getRenderedObject("requestPublicPresentationComissionBean"));

	return mapping.findForward("requestPublicPresentationSeminarComission");
    }

    public ActionForward requestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) getRenderedObject("requestPublicPresentationSeminarComissionBean");

	request.setAttribute("requestPublicPresentationSeminarComissionBean", bean);

	return executeActivity(RequestPublicPresentationSeminarComission.class, bean, request, mapping,
		"requestPublicPresentationSeminarComission", "viewProcess");
    }

    // End of Request Public Presentation Seminar Comission

}
