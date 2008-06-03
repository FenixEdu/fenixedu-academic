package net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

@Mapping(path = "/caseHandlingSecondCycleIndividualCandidacyProcess", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/secondCycle/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/secondCycle/fillPersonalInformation.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/secondCycle/fillCandidacyInformation.jsp"),
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/secondCycle/editCandidacyPersonalInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/secondCycle/editCandidacyInformation.jsp"),
	@Forward(name = "introduce-candidacy-result", path = "/candidacy/secondCycle/introduceCandidacyResult.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
	@Forward(name = "create-registration", path="/candidacy/createRegistration.jsp")

})
public class SecondCycleIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return SecondCycleIndividualCandidacyProcess.class;
    }

    @Override
    protected SecondCycleCandidacyProcess getParentProcess(HttpServletRequest request) {
	return (SecondCycleCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected SecondCycleIndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (SecondCycleIndividualCandidacyProcess) super.getProcess(request);
    }

    private SecondCycleIndividualCandidacyProcessBean getCandidacyBean() {
	return (SecondCycleIndividualCandidacyProcessBean) getRenderedObject("secondCycleIndividualCandidacyProcessBean");
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final SecondCycleCandidacyProcess candidacyProcess = getParentProcess(request);
	if (candidacyProcess == null) {
	    addActionMessage(request, "error.SecondCycleCandidacyPeriod.invalid.candidacyProcess");
	    return listProcesses(mapping, form, request, response);
	} else {
	    final SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
	    bean.setCandidacyProcess(candidacyProcess);
	    bean.setChoosePersonBean(new ChoosePersonBean());
	    request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    public ActionForward prepareCreateNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward searchPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final SecondCycleIndividualCandidacyProcessBean bean = getCandidacyBean();
	final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);

	if (!choosePersonBean.hasPerson()) {
	    if (choosePersonBean.isFirstTimeSearch()) {
		final Collection<Person> persons = Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber());
		choosePersonBean.setFirstTimeSearch(false);
		if (showSimilarPersons(choosePersonBean, persons)) {
		    RenderUtils.invalidateViewState();
		    return mapping.findForward("prepare-create-new-process");
		}
	    }
	    bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
		    choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));
	    return mapping.findForward("fill-personal-information");

	} else {
	    bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
	    return mapping.findForward("fill-personal-information");
	}
    }

    public ActionForward searchAgainPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final SecondCycleIndividualCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);
	bean.getChoosePersonBean().setFirstTimeSearch(true);
	RenderUtils.invalidateViewState();
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward selectPersonForCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final SecondCycleIndividualCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);

	if (!bean.hasChoosenPerson()) {
	    addActionMessage(request, "error.candidacy.must.select.any.person");
	    return mapping.findForward("prepare-create-new-process");
	}

	bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
	bean.removeChoosePersonBean();
	return mapping.findForward("fill-personal-information");

    }

    public ActionForward fillPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final SecondCycleIndividualCandidacyProcessBean bean = getCandidacyBean();
	final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);
	bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
		choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));
	bean.removeChoosePersonBean();
	return mapping.findForward("fill-personal-information");
    }

    public ActionForward fillPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("fill-personal-information");
    }

    public ActionForward fillCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward fillCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("fill-candidacy-information");
    }

    private boolean showSimilarPersons(final ChoosePersonBean choosePersonBean, final Collection<Person> persons) {
	if (!persons.isEmpty()) {
	    return true;
	}
	return !Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(),
		Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty();
    }

    public ActionForward fillDegreeCandidacyInformationPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	RenderUtils.invalidateViewState();
	return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward fillCandidacyInformationPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final SecondCycleIndividualCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);
	RenderUtils.invalidateViewState();

	if (bean.hasPrecedentDegreeType()) {
	    if (bean.isExternalPrecedentDegreeType()) {
		bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean());
	    } else {
		final StudentCurricularPlan studentCurricularPlan = bean.getPrecedentStudentCurricularPlan();
		if (studentCurricularPlan != null) {
		    createCandidacyPrecedentDegreeInformation(bean, studentCurricularPlan);
		}
	    }
	}

	return mapping.findForward("fill-candidacy-information");
    }

    private void createCandidacyPrecedentDegreeInformation(final SecondCycleIndividualCandidacyProcessBean bean,
	    final StudentCurricularPlan studentCurricularPlan) {
	final CandidacyPrecedentDegreeInformationBean info = new CandidacyPrecedentDegreeInformationBean();
	info.setDegreeDesignation(studentCurricularPlan.getName());
	info.setConclusionDate(new LocalDate(studentCurricularPlan.isBolonhaDegree() ? studentCurricularPlan
		.getConclusionDate(CycleType.FIRST_CYCLE) : studentCurricularPlan.getRegistration().getConclusionDate()));
	info.setConclusionGrade(studentCurricularPlan.isBolonhaDegree() ? studentCurricularPlan
		.getFinalAverage(CycleType.FIRST_CYCLE) : studentCurricularPlan.getRegistration().getFinalAverage());
	info.setInstitutionUnitName(rootDomainObject.getInstitutionUnit().getUnitName());
	bean.setPrecedentDegreeInformation(info);
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    request.setAttribute("process", executeService("CreateNewProcess", getProcessType().getName(), getCandidacyBean()));
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	    return mapping.findForward("fill-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteCandidacyPayment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("prepare-candidacy-payment");
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
	bean.setPersonBean(new PersonBean(getProcess(request).getCandidacyPerson()));
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "EditCandidacyPersonalInformation", getCandidacyBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	    return mapping.findForward("edit-candidacy-personal-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean(getProcess(request));
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", bean);
	final PersonBean personBean = new PersonBean();
	personBean.setPerson(getProcess(request).getCandidacyPerson());
	bean.setPersonBean(personBean);
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "EditCandidacyInformation", getCandidacyBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("secondCycleIndividualCandidacyProcessBean", getCandidacyBean());
	    return mapping.findForward("edit-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyResultBean", new SecondCycleIndividualCandidacyResultBean(
		getProcess(request)));
	return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyResultBean", getCandidacyResultBean());
	return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "IntroduceCandidacyResult", getCandidacyResultBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("secondCycleIndividualCandidacyResultBean", getCandidacyResultBean());
	    return mapping.findForward("introduce-candidacy-result");
	}

	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private SecondCycleIndividualCandidacyResultBean getCandidacyResultBean() {
	return (SecondCycleIndividualCandidacyResultBean) getRenderedObject("secondCycleIndividualCandidacyResultBean");
    }

    public ActionForward prepareExecuteCancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("cancel-candidacy");
    }

    public ActionForward executeCancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "CancelCandidacy", null);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return mapping.findForward("cancel-candidacy");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final SecondCycleIndividualCandidacyProcess process = getProcess(request);
	request.setAttribute("degree", process.getCandidacySelectedDegree());
	return mapping.findForward("create-registration");
    }

    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "CreateRegistration");
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return mapping.findForward("create-registration");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

}
