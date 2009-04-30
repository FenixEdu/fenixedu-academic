package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.degreeCandidacyForGraduatedPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.IndividualCandidacyProcessPublicDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/candidacies/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess", module = "publico", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "show-candidacy-creation-page", path = "degree.candidacy.for.graduated.person.candidacy.creation.page"),
	@Forward(name = "inform-submited-candidacy", path = "inform.submited.candidacy"),
	@Forward(name = "open-candidacy-processes-not-found", path = "candidacy.process.closed"), 
	@Forward(name = "candidacy-process-intro", path="candidacy.process.intro"),
	@Forward(name = "show-candadidacy-authentication-page", path="show.candadidacy.authentication.page"),
	@Forward(name = "show-candidacy-details", path="degree.candidacy.for.graduated.person.show.candidacy.details"),
	@Forward(name = "edit-candidacy", path="degree.candidacy.for.graduated.person.edit.candidacy"),
	@Forward(name = "begin-candidacy-process-intro", path="degree.candidacy.for.graduated.person.candidacy.process.intro"),
	@Forward(name = "edit-candidacy-documents", path="degree.candidacy.for.graduated.person.edit.candidacy.documents")})
public class DegreeCandidacyForGraduatedPersonIndividualProcessDA extends IndividualCandidacyProcessPublicDA {

    private static final LocalDateTime start = new LocalDateTime(2010, 1, 1, 0, 0);
    private static final LocalDateTime end = new LocalDateTime(20010, 1, 1, 0, 0);

    @Override
    protected String getCandidacyNameKey() {
	return "label.candidacyName.degreeCandidacyForGraduatedPerson";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DegreeCandidacyForGraduatedPersonIndividualProcess individualCandidacyProcess = (DegreeCandidacyForGraduatedPersonIndividualProcess) request.getAttribute("individualCandidacyProcess");
	DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = new DegreeCandidacyForGraduatedPersonIndividualProcessBean(individualCandidacyProcess);
	bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));
	bean.setCandidacyInformationBean(new CandidacyInformationBean(individualCandidacyProcess.getCandidacy()));
	
	request.setAttribute("individualCandidacyProcessBean", bean);
	return mapping.findForward("show-candidacy-details");	
    }

    @Override
    protected Class<? extends CandidacyProcess> getParentProcessType() {
	return DegreeCandidacyForGraduatedPersonProcess.class;
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub

    }

    @Override
    protected Class<? extends IndividualCandidacyProcess> getProcessType() {
	return DegreeCandidacyForGraduatedPersonIndividualProcess.class;
    }
    
    
    public ActionForward prepareCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	String hash = request.getParameter("hash");
	PublicCandidacyHashCode candidacyHashCode = PublicCandidacyHashCode.getPublicCandidacyCodeByHash(hash);
	
	if(candidacyHashCode == null) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}
	
	if(candidacyHashCode.getIndividualCandidacyProcess() != null) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	}

	CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();
	if (candidacyProcess == null)
	    return mapping.findForward("open-candidacy-processes-not-found");

	DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = new DegreeCandidacyForGraduatedPersonIndividualProcessBean();
	bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean());
	bean.setPersonBean(new PersonBean());
	bean.setCandidacyProcess(candidacyProcess);
	bean.setCandidacyInformationBean(new CandidacyInformationBean());
	bean.setPublicCandidacyHashCode(candidacyHashCode);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	
	/* FIXME ANil : Fill for fun */
	bean.getPersonBean().setName("Anil MAmede Ali Kassamali Degree Candidacy For Graduated Person");
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
	bean.getPersonBean().setEmail("amak@mega.ist.utl.pt");
	
	return mapping.findForward("show-candidacy-creation-page");
    }


    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    if (!validateCaptcha(mapping, request)) {
		return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
	    }

	    DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = (DegreeCandidacyForGraduatedPersonIndividualProcessBean) 
	    	getIndividualCandidacyProcessBean();
	    bean.setInternalPersonCandidacy(Boolean.FALSE);
	    bean.getCandidacyInformationBean().setInstitutionName(bean.getPrecedentDegreeInformation().getInstitutionName());
	    bean.getCandidacyInformationBean().setDegreeDesignation(bean.getPrecedentDegreeInformation().getDegreeDesignation());

	    DegreeCandidacyForGraduatedPersonIndividualProcess process = (DegreeCandidacyForGraduatedPersonIndividualProcess) createNewProcess(bean);
	    
	    request.setAttribute("process", process);
	    request.setAttribute("mappingPath", mapping.getPath());

	    return mapping.findForward("inform-submited-candidacy");
	} catch(DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    e.printStackTrace();
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("fill-candidacy-information");
	}
    }
    
    public ActionForward prepareEditCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy");
    }
    
    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = (DegreeCandidacyForGraduatedPersonIndividualProcessBean)
		getIndividualCandidacyProcessBean();
	try {
	    if (!validateCaptcha(mapping, request)) {
		return executeEditCandidacyPersonalInformationInvalid(mapping, form, request, response);
	    }

	    bean.getCandidacyInformationBean().setInstitutionName(bean.getPrecedentDegreeInformation().getInstitutionName());
	    bean.getCandidacyInformationBean().setDegreeDesignation(bean.getPrecedentDegreeInformation().getDegreeDesignation());
	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyPersonalInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy");
	}
	
	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return viewCandidacy(mapping, form, request, response);
    }

    @Override
    protected LocalDateTime getApplicationDateEnd() {
	return end;
    }

    @Override
    protected LocalDateTime getApplicationDateStart() {
	return start;
    }
    
    
}
