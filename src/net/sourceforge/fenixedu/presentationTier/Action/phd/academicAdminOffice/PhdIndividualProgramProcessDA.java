package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

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

	@Forward(name = "editQualificationsAndJobsInformation", path = "/phd/academicAdminOffice/editQualificationsAndJobsInformation.jsp")

})
public class PhdIndividualProgramProcessDA extends PhdProcessDA {

    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("processes", ExecutionYear.readCurrentExecutionYear().getPhdIndividualProgramProcesses());

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

	request.setAttribute("editPersonalInformationBean", new PersonBean(getProcess(request).getPerson()));
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

	request.setAttribute("editPersonalInformationBean", getEditPersonalInformationBean());
	return executeActivity(PhdIndividualProgramProcess.EditPersonalInformation.class, getEditPersonalInformationBean(),
		request, mapping, "editPersonalInformation", "viewProcess", "message.personal.data.edited.with.success");
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
	    ExecuteProcessActivity.run(getProcess(request), "AddQualification", bean);
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
	return executeActivity(PhdIndividualProgramProcess.DeleteQualification.class,
		getDomainObject(request, "qualificationId"), request, mapping, "editQualificationsAndJobsInformation",
		"editQualificationsAndJobsInformation", "message.qualification.information.delete.success");
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
	    ExecuteProcessActivity.run(getProcess(request), "AddJobInformation", bean);
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
	return executeActivity(PhdIndividualProgramProcess.DeleteJobInformation.class, getDomainObject(request, "jobId"), request, mapping,
		"editQualificationsAndJobsInformation", "editQualificationsAndJobsInformation",
		"message.job.information.delete.success");
    }
    // End of Qualifications and Jobs information
}
