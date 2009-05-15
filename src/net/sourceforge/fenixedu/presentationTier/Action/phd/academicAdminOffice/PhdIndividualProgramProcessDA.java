package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdIndividualProgramProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "manageProcesses", path = "/phd/academicAdminOffice/manageProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/academicAdminOffice/viewProcess.jsp"),

@Forward(name = "editPersonalInformation", path = "/phd/academicAdminOffice/editPersonalInformation.jsp")

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

}
