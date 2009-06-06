package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/candidacies/phdProgramCandidacyProcess", module = "publico")
@Forwards( {

@Forward(name = "createCandidacyIdentity", path = "phdProgram.createCandidacyIdentity")

})
public class PublicPhdProgramCandidacyProcessDA extends PhdProcessDA {

    public ActionForward prepareCreateCandidacyIdentity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", new PhdProgramCandidacyProcessBean());
	return mapping.findForward("createCandidacyIdentity");
    }
    
    public ActionForward prepareCreateCandidacyIdentityInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("candidacyBean", getRenderedObject());
	return mapping.findForward("createCandidacyIdentity");
    }

    // @Override
    // public ActionForward execute(ActionMapping mapping, ActionForm
    // actionForm, HttpServletRequest request,
    // HttpServletResponse response) throws Exception {
    //
    // final Process process = getProcess(request);
    // if (process != null) {
    // request.setAttribute("processId", process.getExternalId());
    // request.setAttribute("process", process);
    // }
    //
    // request.setAttribute("process", getProcess(request));
    //
    // return super.execute(mapping, actionForm, request, response);
    // }
    //
    // @Override
    // protected PhdProgramCandidacyProcess getProcess(HttpServletRequest
    // request) {
    // return (PhdProgramCandidacyProcess) super.getProcess(request);
    // }
    //
    // // Create Candidacy Steps
    //
    // public ActionForward prepareSearchPerson(ActionMapping mapping,
    // ActionForm form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // request.setAttribute("createCandidacyBean", new
    // PhdProgramCandidacyProcessBean());
    // request.setAttribute("persons", Collections.emptyList());
    //
    // return mapping.findForward("searchPerson");
    // }
    //
    // public ActionForward searchPerson(ActionMapping mapping, ActionForm form,
    // HttpServletRequest request,
    // HttpServletResponse response) throws FenixFilterException,
    // FenixServiceException {
    //
    // final PhdProgramCandidacyProcessBean bean =
    // getCreateCandidacyProcessBean();
    // request.setAttribute("createCandidacyBean", bean);
    //
    // final ChoosePersonBean choosePersonBean =
    // getCreateCandidacyProcessBean().getChoosePersonBean();
    // if (!choosePersonBean.hasPerson()) {
    // if (choosePersonBean.isFirstTimeSearch()) {
    // final Collection<Person> persons =
    // Person.findPersonByDocumentID(choosePersonBean
    // .getIdentificationNumber());
    // choosePersonBean.setFirstTimeSearch(false);
    // if (showSimilarPersons(choosePersonBean, persons)) {
    // RenderUtils.invalidateViewState();
    // return mapping.findForward("searchPerson");
    // }
    // }
    // bean.setPersonBean(new PersonBean(choosePersonBean.getName(),
    // choosePersonBean.getIdentificationNumber(),
    // choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));
    //
    // return mapping.findForward("createCandidacy");
    //
    // } else {
    // bean.setPersonBean(new
    // PersonBean(bean.getChoosePersonBean().getPerson()));
    // setIsEmployeeAttributeAndMessage(request,
    // bean.getChoosePersonBean().getPerson());
    // return mapping.findForward("createCandidacy");
    // }
    //
    // }
    //
    // protected boolean showSimilarPersons(final ChoosePersonBean
    // choosePersonBean, final Collection<Person> persons) {
    // if (!persons.isEmpty()) {
    // return true;
    // }
    // return !Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(),
    //Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName
    // ())).isEmpty();
    // }
    //
    // public ActionForward createCandidacyInvalid(ActionMapping mapping,
    // ActionForm form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // request.setAttribute("createCandidacyBean",
    // getCreateCandidacyProcessBean());
    // setIsEmployeeAttributeAndMessage(request,
    // getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());
    //
    // return mapping.findForward("createCandidacy");
    // }
    //
    // public ActionForward createCandidacy(ActionMapping mapping, ActionForm
    // form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // try {
    //
    // if (!validateAreaCodeAndAreaOfAreaCode(request,
    // getCreateCandidacyProcessBean().getChoosePersonBean().getPerson(),
    // getCreateCandidacyProcessBean().getPersonBean().getCountryOfResidence(),
    // getCreateCandidacyProcessBean()
    // .getPersonBean().getAreaCode(),
    // getCreateCandidacyProcessBean().getPersonBean().getAreaOfAreaCode())) {
    //
    // setIsEmployeeAttributeAndMessage(request,
    // getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());
    // request.setAttribute("createCandidacyBean",
    // getCreateCandidacyProcessBean());
    // return mapping.findForward("createCandidacy");
    //
    // }
    //
    // CreateNewProcess.run(PhdIndividualProgramProcess.class,
    // getCreateCandidacyProcessBean());
    //
    // } catch (DomainException e) {
    // addErrorMessage(request, e.getKey(), e.getArgs());
    // setIsEmployeeAttributeAndMessage(request,
    // getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());
    // getCreateCandidacyProcessBean().getPersonBean().setPerson(null);
    // request.setAttribute("createCandidacyBean",
    // getCreateCandidacyProcessBean());
    // return mapping.findForward("createCandidacy");
    // }
    //
    // return mapping.findForward("manageProcesses");
    //
    // }
    //
    // private PhdProgramCandidacyProcessBean getCreateCandidacyProcessBean() {
    // return (PhdProgramCandidacyProcessBean)
    // getRenderedObject("createCandidacyBean");
    // }
    //
    // // End of Create Candidacy Steps
    //
    // public ActionForward cancelCreateCandidacy(ActionMapping mapping,
    // ActionForm form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // return mapping.findForward("manageProcesses");
    // }
    //
    // public ActionForward manageCandidacyDocuments(ActionMapping mapping,
    // ActionForm form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // prepareDocumentsToUpload(request);
    //
    // return mapping.findForward("manageCandidacyDocuments");
    // }
    //
    // private void prepareDocumentsToUpload(HttpServletRequest request) {
    // request.setAttribute("documentsToUpload", Arrays.asList(new
    // PhdCandidacyDocumentUploadBean(),
    // new PhdCandidacyDocumentUploadBean(), new
    // PhdCandidacyDocumentUploadBean(), new PhdCandidacyDocumentUploadBean(),
    // new PhdCandidacyDocumentUploadBean()));
    // }
    //
    // public ActionForward uploadDocumentsInvalid(ActionMapping mapping,
    // ActionForm form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // request.setAttribute("documentsToUpload", getDocumentsToUpload());
    //
    // return mapping.findForward("manageCandidacyDocuments");
    // }
    //
    // public ActionForward uploadDocuments(ActionMapping mapping, ActionForm
    // form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // if (!hasAnyDocumentToUpload()) {
    // request.setAttribute("documentsToUpload", getDocumentsToUpload());
    //
    // addErrorMessage(request, "message.no.documents.to.upload");
    //
    // return mapping.findForward("manageCandidacyDocuments");
    // }
    //
    // final ActionForward result =
    // executeActivity(PhdProgramCandidacyProcess.UploadDocuments.class,
    // getDocumentsToUpload(),
    // request, mapping, "manageCandidacyDocuments", "manageCandidacyDocuments",
    // "message.documents.uploaded.with.success");
    //
    // RenderUtils.invalidateViewState("documentsToUpload");
    //
    // prepareDocumentsToUpload(request);
    //
    // return result;
    //
    // }
    //
    // private boolean hasAnyDocumentToUpload() {
    // for (final PhdCandidacyDocumentUploadBean each : getDocumentsToUpload())
    // {
    // if (each.hasAnyInformation()) {
    // return true;
    // }
    // }
    //
    // return false;
    //
    // }
    //
    // private List<PhdCandidacyDocumentUploadBean> getDocumentsToUpload() {
    // return (List<PhdCandidacyDocumentUploadBean>)
    // getObjectFromViewState("documentsToUpload");
    // }
    //
    // public ActionForward deleteDocument(ActionMapping mapping, ActionForm
    // form, HttpServletRequest request,
    // HttpServletResponse response) {
    //
    // prepareDocumentsToUpload(request);
    //
    // return executeActivity(PhdProgramCandidacyProcess.DeleteDocument.class,
    // getDocument(request), request, mapping,
    // "manageCandidacyDocuments", "manageCandidacyDocuments",
    // "message.document.deleted.successfuly");
    // }
    //
    // private PhdProgramCandidacyProcessDocument getDocument(HttpServletRequest
    // request) {
    // return getDomainObject(request, "documentId");

    // }
}
