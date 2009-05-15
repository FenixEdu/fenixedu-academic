package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "searchPerson", path = "/phd/candidacy/academicAdminOffice/searchPerson.jsp"),

@Forward(name = "createCandidacy", path = "/phd/candidacy/academicAdminOffice/createCandidacy.jsp"),

@Forward(name = "manageProcesses", path = "/phdIndividualProgramProcess.do?method=manageProcesses"),

@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/academicAdminOffice/manageCandidacyDocuments.jsp")

})
public class PhdProgramCandidacyProcessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Process process = getProcess(request);
	if (process != null) {
	    request.setAttribute("processId", process.getExternalId());
	    request.setAttribute("process", process);
	}
	;
	request.setAttribute("process", getProcess(request));

	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected PhdProgramCandidacyProcess getProcess(HttpServletRequest request) {
	return (PhdProgramCandidacyProcess) super.getProcess(request);
    }

    // Create Candidacy Steps

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCandidacyBean", new PhdProgramCandidacyProcessBean());
	request.setAttribute("persons", Collections.emptyList());

	return mapping.findForward("searchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final PhdProgramCandidacyProcessBean bean = getCreateCandidacyProcessBean();
	request.setAttribute("createCandidacyBean", bean);

	final ChoosePersonBean choosePersonBean = getCreateCandidacyProcessBean().getChoosePersonBean();
	if (!choosePersonBean.hasPerson()) {
	    if (choosePersonBean.isFirstTimeSearch()) {
		final Collection<Person> persons = Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber());
		choosePersonBean.setFirstTimeSearch(false);
		if (showSimilarPersons(choosePersonBean, persons)) {
		    RenderUtils.invalidateViewState();
		    return mapping.findForward("searchPerson");
		}
	    }
	    bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
		    choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));

	    return mapping.findForward("createCandidacy");

	} else {
	    bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
	    return mapping.findForward("createCandidacy");
	}

    }

    protected boolean showSimilarPersons(final ChoosePersonBean choosePersonBean, final Collection<Person> persons) {
	if (!persons.isEmpty()) {
	    return true;
	}
	return !Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(),
		Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty();
    }

    public ActionForward createCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());

	return mapping.findForward("createCandidacy");
    }

    public ActionForward createCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	CreateNewProcess.run(PhdIndividualProgramProcess.class, getCreateCandidacyProcessBean());

	return mapping.findForward("manageProcesses");

    }

    private PhdProgramCandidacyProcessBean getCreateCandidacyProcessBean() {
	return (PhdProgramCandidacyProcessBean) getRenderedObject("createCandidacyBean");
    }

    // End of Create Candidacy Steps

    public ActionForward cancelCreateCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("manageProcesses");
    }

    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("candidacyDocuments", getProcess(request).getDocuments());
	request.setAttribute("documentsToUpload", Arrays.asList(new PhdCandidacyDocumentUploadBean(),
		new PhdCandidacyDocumentUploadBean(), new PhdCandidacyDocumentUploadBean(), new PhdCandidacyDocumentUploadBean(),
		new PhdCandidacyDocumentUploadBean()));

	return mapping.findForward("manageCandidacyDocuments");
    }

    public ActionForward uploadDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("documentsToUpload", getDocumentsToUpload());

	return mapping.findForward("manageCandidacyDocuments");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return executeActivity(PhdProgramCandidacyProcess.UploadDocuments.class, getDocumentsToUpload(), request, mapping,
		"manageCandidacyDocuments", "manageCandidacyDocuments", "message.documents.uploaded.with.success");

    }

    private List<PhdCandidacyDocumentUploadBean> getDocumentsToUpload() {
	return (List<PhdCandidacyDocumentUploadBean>) getObjectFromViewState("documentsToUpload");
    }

}
