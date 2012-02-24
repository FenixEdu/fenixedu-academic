package net.sourceforge.fenixedu.presentationTier.Action.phd.program;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.domain.phd.PhdProgramInformation;
import net.sourceforge.fenixedu.domain.phd.PhdProgramInformationBean;
import net.sourceforge.fenixedu.domain.phd.ThesisSubject;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(path = "/phdProgramInformation", module = "academicAdminOffice")
@Forwards({
	@Forward(name = "listPhdPrograms", path = "/phd/academicAdminOffice/program/information/listPhdPrograms.jsp"),
	@Forward(name = "listPhdProgramInformations", path = "/phd/academicAdminOffice/program/information/listPhdProgramInformations.jsp"),
	@Forward(name = "manageFocusAreas", path = "/phd/academicAdminOffice/program/information/manageFocusAreas.jsp"),
	@Forward(name = "manageThesisSubjects", path = "/phd/academicAdminOffice/program/information/manageThesisSubjects.jsp"),
	@Forward(name = "createPhdInformation", path = "/phd/academicAdminOffice/program/information/createPhdInformation.jsp"),
	@Forward(name = "editPhdProgramInformation", path = "/phd/academicAdminOffice/program/information/editPhdProgramInformation.jsp") })
public class PhdProgramInformationDA extends FenixDispatchAction {

    public static class ThesisSubjectBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private MultiLanguageString name;
	private MultiLanguageString description;
	private String teacherId;

	public MultiLanguageString getName() {
	    return name;
	}

	public void setName(MultiLanguageString name) {
	    this.name = name;
	}

	public MultiLanguageString getDescription() {
	    return description;
	}

	public void setDescription(MultiLanguageString description) {
	    this.description = description;
	}

	public String getTeacherId() {
	    return teacherId;
	}

	public void setTeacherId(String teacherId) {
	    this.teacherId = teacherId;
	}
    }

    public ActionForward listPhdPrograms(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("phdPrograms", RootDomainObject.getInstance().getPhdPrograms());
	return mapping.findForward("listPhdPrograms");
    }

    public ActionForward listPhdProgramInformations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("phdProgram", readPhdProgram(request));
	return mapping.findForward("listPhdProgramInformations");
    }

    public ActionForward manageFocusAreas(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("focusAreas", RootDomainObject.getInstance().getPhdProgramFocusAreas());

	return mapping.findForward("manageFocusAreas");
    }

    public ActionForward manageThesisSubjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	PhdProgramFocusArea focusArea = (PhdProgramFocusArea) AbstractDomainObject.fromExternalId((String) getFromRequest(
		request, "focusAreaId"));

	request.setAttribute("focusArea", focusArea);
	request.setAttribute("thesisSubjectBean", new ThesisSubjectBean());
	request.setAttribute("thesisSubjects", focusArea.getThesisSubjects());

	return mapping.findForward("manageThesisSubjects");
    }

    @Service
    public ActionForward addThesisSubject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ThesisSubjectBean bean = getRenderedObject("thesisSubjectBean");
	PhdProgramFocusArea focusArea = (PhdProgramFocusArea) AbstractDomainObject.fromExternalId((String) getFromRequest(
		request, "focusAreaId"));

	Person person = Person.readPersonByUsername(bean.getTeacherId());
	if (person != null) {
	    focusArea.addThesisSubjects(ThesisSubject.createThesisSubject(bean.getName(), bean.getDescription(),
		    person.getTeacher()));
	} else {
	    addErrorMessage(request, "error", "error.thesisSubject.no.teacher.found");
	}

	request.setAttribute("focusArea", focusArea);
	request.setAttribute("thesisSubjectBean", new ThesisSubjectBean());
	request.setAttribute("thesisSubjects", focusArea.getThesisSubjects());

	return mapping.findForward("manageThesisSubjects");
    }

    public ActionForward removeThesisSubject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	PhdProgramFocusArea focusArea = (PhdProgramFocusArea) AbstractDomainObject.fromExternalId((String) getFromRequest(
		request, "focusAreaId"));
	ThesisSubject thesisSubject = (ThesisSubject) AbstractDomainObject.fromExternalId((String) getFromRequest(request,
		"thesisSubjectId"));

	thesisSubject.removePhdProgramFocusArea();

	request.setAttribute("focusArea", focusArea);
	request.setAttribute("thesisSubjectBean", new ThesisSubjectBean());
	request.setAttribute("thesisSubjects", focusArea.getThesisSubjects());

	return mapping.findForward("manageThesisSubjects");
    }

    private PhdProgram readPhdProgram(final HttpServletRequest request) {
	return getDomainObject(request, "phdProgramId");
    }

    private PhdProgramInformationBean readPhdInformationBean() {
	return getRenderedObject("phdProgramInformationBean");
    }

    public ActionForward prepareCreatePhdInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgram phdProgram = readPhdProgram(request);
	request.setAttribute("phdProgram", phdProgram);
	request.setAttribute("phdProgramInformationBean", new PhdProgramInformationBean(phdProgram));

	return mapping.findForward("createPhdInformation");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	PhdProgramInformationBean readPhdInformationBean = readPhdInformationBean();
	try {
	    PhdProgramInformation.createInformation(readPhdInformationBean);
	} catch (PhdDomainOperationException e) {
	    request.setAttribute("phdProgramInformationBean", readPhdInformationBean);
	    setError(request, mapping, null, null, e);
	    return mapping.findForward("createPhdInformation");
	}

	return listPhdProgramInformations(mapping, form, request, response);
    }

    public ActionForward createInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgram phdProgram = readPhdProgram(request);
	request.setAttribute("phdProgram", phdProgram);
	request.setAttribute("phdProgramInformationBean", readPhdInformationBean());

	return mapping.findForward("createPhdInformation");
    }

    public ActionForward prepareEditPhdProgramInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgramInformation phdProgramInformation = readPhdProgramInformation(request);

	request.setAttribute("phdProgramInformation", phdProgramInformation);
	request.setAttribute("phdProgramInformationBean", new PhdProgramInformationBean(phdProgramInformation));

	return mapping.findForward("editPhdProgramInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	PhdProgramInformation phdProgramInformation = readPhdProgramInformation(request);
	PhdProgramInformationBean readPhdInformationBean = readPhdInformationBean();

	try {
	    phdProgramInformation.edit(readPhdInformationBean);
	} catch (PhdDomainOperationException e) {
	    request.setAttribute("phdProgramInformation", phdProgramInformation);
	    request.setAttribute("phdProgramInformationBean", readPhdInformationBean);
	    setError(request, mapping, null, null, e);
	    return mapping.findForward("editPhdProgramInformation");
	}

	return listPhdProgramInformations(mapping, form, request, response);
    }

    public ActionForward editInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgramInformation phdProgramInformation = readPhdProgramInformation(request);

	request.setAttribute("phdProgramInformation", phdProgramInformation);
	request.setAttribute("phdProgramInformationBean", readPhdInformationBean());

	return mapping.findForward("editPhdProgramInformation");
    }

    private PhdProgramInformation readPhdProgramInformation(HttpServletRequest request) {
	return getDomainObject(request, "phdProgramInformationId");
    }
}
