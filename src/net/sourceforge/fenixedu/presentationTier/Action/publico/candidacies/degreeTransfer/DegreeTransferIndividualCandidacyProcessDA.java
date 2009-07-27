package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.degreeTransfer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.IndividualCandidacyProcessPublicDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;

//@Mapping(path = "/candidacies/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "publico", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "candidacy-process-intro", path = "candidacy.process.intro"),
	@Forward(name = "show-candidacy-creation-page", path = "degree.transfer.candidacy.creation.page"),
	@Forward(name = "begin-candidacy-process-intro", path = "degree.transfer.candidacy.process.intro"),
	@Forward(name = "show-pre-creation-candidacy-form", path = "show.pre.creation.candidacy.form"),
	@Forward(name = "show-email-message-sent", path = "show.email.message.sent"),
	@Forward(name = "inform-submited-candidacy", path = "inform.submited.candidacy"),
	@Forward(name = "open-candidacy-processes-not-found", path = "candidacy.process.closed"),
	@Forward(name = "show-candadidacy-authentication-page", path = "show.candadidacy.authentication.page"),
	@Forward(name = "show-candidacy-details", path = "degree.transfer.show.candidacy.details"),
	@Forward(name = "edit-candidacy", path = "degree.transfer.edit.candidacy"),
	@Forward(name = "edit-candidacy-documents", path = "degree.transfer.edit.candidacy.documents") })
public class DegreeTransferIndividualCandidacyProcessDA extends IndividualCandidacyProcessPublicDA {

    private static final LocalDateTime start = new LocalDateTime(2010, 1, 1, 0, 0);
    private static final LocalDateTime end = new LocalDateTime(20010, 1, 1, 0, 0);

    @Override
    protected String getCandidacyNameKey() {
	return "label.candidacyName.degreeTransfer";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DegreeTransferIndividualCandidacyProcess individualCandidacyProcess = (DegreeTransferIndividualCandidacyProcess) request
		.getAttribute("individualCandidacyProcess");
	DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean(
		individualCandidacyProcess);
	bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));
	bean.setCandidacyInformationBean(new CandidacyInformationBean(individualCandidacyProcess.getCandidacy()));

	request.setAttribute("individualCandidacyProcessBean", bean);
	return mapping.findForward("show-candidacy-details");
    }

    public ActionForward prepareCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String hash = request.getParameter("hash");
	DegreeOfficePublicCandidacyHashCode candidacyHashCode = (DegreeOfficePublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(hash);

	if (candidacyHashCode == null) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	if (candidacyHashCode.getIndividualCandidacyProcess() != null) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	}

	CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();
	if (candidacyProcess == null)
	    return mapping.findForward("open-candidacy-processes-not-found");

	DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean();
	bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean());
	bean.setPersonBean(new PersonBean());
	bean.setCandidacyProcess(candidacyProcess);
	bean.setCandidacyInformationBean(new CandidacyInformationBean());
	bean.setPublicCandidacyHashCode(candidacyHashCode);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

	/* FIXME ANil : Fill for fun */
	bean.getPersonBean().setName("Anil MAmede Ali Kassamali Degree Transfer");
	bean.getPersonBean().setGender(Gender.MALE);
	bean.getPersonBean().setDateOfBirth(new YearMonthDay(1980, 02, 15));
	bean.getPersonBean().setDocumentIdNumber("123456789");
	bean.getPersonBean().setIdDocumentType(IDDocumentType.IDENTITY_CARD);
	bean.getPersonBean().setDocumentIdEmissionDate(new YearMonthDay(2008, 02, 15));
	bean.getPersonBean().setDocumentIdExpirationDate(new YearMonthDay(2013, 02, 15));
	bean.getPersonBean().setDocumentIdEmissionLocation("Lisboa");
	bean.getPersonBean().setFiscalCode("987654321");
	bean.getPersonBean().setAddress("Rua Vasco Da Gama");
	bean.getPersonBean().setArea("Odivelas");
	bean.getPersonBean().setAreaCode("2675-460");
	bean.getPersonBean().setAreaOfAreaCode("Odivelas");
	bean.getPersonBean().setPhone("939584538");
	bean.getPersonBean().setEmail(candidacyHashCode.getEmail());

	return mapping.findForward("show-candidacy-creation-page");
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    if (!validateCaptcha(mapping, request)) {
		return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
	    }

	    DegreeTransferIndividualCandidacyProcessBean bean = (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	    bean.setInternalPersonCandidacy(Boolean.FALSE);
	    bean.getCandidacyInformationBean().setInstitutionName(bean.getPrecedentDegreeInformation().getInstitutionName());
	    bean.getCandidacyInformationBean().setDegreeDesignation(bean.getPrecedentDegreeInformation().getDegreeDesignation());

	    DegreeTransferIndividualCandidacyProcess process = (DegreeTransferIndividualCandidacyProcess) createNewProcess(bean);

	    request.setAttribute("process", process);
	    request.setAttribute("mappingPath", mapping.getPath());

	    return mapping.findForward("inform-submited-candidacy");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    e.printStackTrace();
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("fill-candidacy-information");
	}
    }

    public ActionForward prepareEditCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy");
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	DegreeTransferIndividualCandidacyProcessBean bean = (DegreeTransferIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    if (!validateCaptcha(mapping, request)) {
		return executeEditCandidacyPersonalInformationInvalid(mapping, form, request, response);
	    }

	    bean.getCandidacyInformationBean().setInstitutionName(bean.getPrecedentDegreeInformation().getInstitutionName());
	    bean.getCandidacyInformationBean().setDegreeDesignation(bean.getPrecedentDegreeInformation().getDegreeDesignation());
	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyPersonalInformation",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return viewCandidacy(mapping, form, request, response);
    }

    @Override
    protected Class<? extends CandidacyProcess> getParentProcessType() {
	return DegreeTransferCandidacyProcess.class;
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub

    }

    @Override
    protected Class<? extends IndividualCandidacyProcess> getProcessType() {
	return DegreeTransferIndividualCandidacyProcess.class;
    }

    @Override
    protected LocalDateTime getApplicationDateEnd() {
	return end;
    }

    @Override
    protected LocalDateTime getApplicationDateStart() {
	return start;
    }

    @Override
    protected String getRootPortalCandidacyAccess() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected String getRootPortalCandidacySubmission() {
	// TODO Auto-generated method stub
	return null;
    }
}
