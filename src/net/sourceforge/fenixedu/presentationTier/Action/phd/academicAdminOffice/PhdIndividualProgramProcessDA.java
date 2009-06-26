package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramGuidingBean;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddAssistantGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddJobInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddQualification;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteAssistantGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteGuiding;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteJobInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.DeleteQualification;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditIndividualProcessInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlertBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

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

	@Forward(name = "manageAlerts", path = "/phd/academicAdminOffice/manageAlerts.jsp"),

	@Forward(name = "createCustomAlert", path = "/phd/academicAdminOffice/createCustomAlert.jsp"),

	@Forward(name = "viewAlertMessages", path = "/phd/academicAdminOffice/viewAlertMessages.jsp")

})
public class PhdIndividualProgramProcessDA extends PhdProcessDA {

    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");

	if (searchBean == null) {
	    searchBean = new SearchPhdIndividualProgramProcessBean();
	    searchBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	}

	request.setAttribute("searchProcessBean", searchBean);

	request.setAttribute("processes", PhdIndividualProgramProcess.search(searchBean));

	return mapping.findForward("manageProcesses");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
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

    // End pf Phd guiding information

    // Alerts Management

    public ActionForward manageAlerts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("alerts", getAlertsToShow(request));

	return mapping.findForward("manageAlerts");
    }

    public ActionForward prepareCreateCustomAlert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCustomAlertBean", new PhdCustomAlertBean());

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

    public ActionForward markTaskAsPerformed(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getAlertMessage(request).setTaskPerformed(true);

	request.setAttribute("alertMessages", getLoggedPerson(request).getPhdAlertMessages());

	return mapping.findForward("viewAlertMessages");
    }

    public ActionForward deleteAlertMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    getAlertMessage(request).delete();

	    addSuccessMessage(request, "message.alertMessage.deleted.successfuly");

	} catch (DomainException ex) {
	    addErrorMessage(request, ex.getKey(), ex.getArgs());
	}

	request.setAttribute("alertMessages", getLoggedPerson(request).getPhdAlertMessages());

	return mapping.findForward("viewAlertMessages");
    }

    private PhdAlertMessage getAlertMessage(HttpServletRequest request) {
	return getDomainObject(request, "alertMessageId");
    }

    // End of Alerts Management
}
