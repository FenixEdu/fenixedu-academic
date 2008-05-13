package net.sourceforge.fenixedu.presentationTier.Action.candidacy.over23;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

@Mapping(path = "/caseHandlingOver23IndividualCandidacyProcess", module = "academicAdminOffice", formBean = "candidacyForm")
@Forwards( { @Forward(name = "intro", path = "/candidacy/over23/intro.jsp"),
	@Forward(name = "list-processes", path = "/academicAdminOffice/caseHandling/listProcesses.jsp"),
	@Forward(name = "list-allowed-activities", path = "/academicAdminOffice/caseHandling/listActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/over23/createCandidacyStepOne.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/over23/createCandidacyStepTwo.jsp"),
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/over23/editCandidacyPersonalInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/over23/editCandidacyInformation.jsp"),
	@Forward(name = "introduce-candidacy-result", path = "/candidacy/over23/introduceCandidacyResult.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp")

})
public class Over23IndividualCandidacyProcessDA extends CaseHandlingDispatchAction {

    @Override
    protected Class getProcessType() {
	return Over23IndividualCandidacyProcess.class;
    }

    public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("intro");
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Over23IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean();
	final Over23CandidacyProcess candidacyProcess = getCandidacyProcess();
	if (candidacyProcess == null) {
	    addActionMessage(request, "error.Over32IndividualCandidacy.invalid.candidacyProcess");
	    return listProcesses(mapping, form, request, response);
	} else {
	    bean.setCandidacyProcess(candidacyProcess);
	    bean.setChoosePersonBean(new ChoosePersonBean());
	    request.setAttribute("over23IndividualCandidacyProcessBean", bean);
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    private Over23CandidacyProcess getCandidacyProcess() {
	final Over23CandidacyPeriod candidacyPeriod = ExecutionYear.readCurrentExecutionYear().getOver23CandidacyPeriod();
	return (candidacyPeriod == null) ? null : candidacyPeriod.getOver23CandidacyProcess();
    }

    public ActionForward prepareCreateNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward fillPersonalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Over23IndividualCandidacyProcessBean bean = getCandidacyBean();
	final ChoosePersonBean choosePersonBean = bean.getChoosePersonBean();
	request.setAttribute("over23IndividualCandidacyProcessBean", bean);

	if (!choosePersonBean.hasPerson()) {
	    if (choosePersonBean.isFirstTimeSearch()) {
		final Collection<Person> persons = Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber());
		filterPersonsWithStudent(persons);
		choosePersonBean.setFirstTimeSearch(false);
		if (showSimilarPersons(choosePersonBean, persons)) {
		    RenderUtils.invalidateViewState();
		    return mapping.findForward("prepare-create-new-process");
		}
	    }
	    bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
		    choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));
	    return mapping.findForward("prepare-create-new-process");

	} else {
	    bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    private void filterPersonsWithStudent(final Collection<Person> persons) {
	final Iterator<Person> personsIter = persons.iterator();
	while (personsIter.hasNext()) {
	    if (personsIter.next().hasStudent()) {
		personsIter.remove();
	    }
	}
    }

    public ActionForward fillCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final Over23IndividualCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("over23IndividualCandidacyProcessBean", bean);
	return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward fillCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("fill-candidacy-information");
    }

    private boolean showSimilarPersons(final ChoosePersonBean choosePersonBean, final Collection<Person> persons) {
	if (!persons.isEmpty()) {
	    return true;
	}

	final Collection<Person> personsByDateOfBirth = Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(), Person
		.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName()));
	filterPersonsWithStudent(personsByDateOfBirth);
	return !personsByDateOfBirth.isEmpty();
    }

    public ActionForward addDegreeToCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return addDegreeToCandidacy(mapping, actionForm, request, response, "fill-candidacy-information");
    }

    private ActionForward addDegreeToCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, String forward) {

	final Over23IndividualCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("over23IndividualCandidacyProcessBean", bean);
	if (bean.hasDegreeToAdd() && !bean.containsDegree(bean.getDegreeToAdd())) {
	    bean.addDegree(bean.getDegreeToAdd());
	    bean.removeDegreeToAdd();
	    RenderUtils.invalidateViewState();
	}
	return mapping.findForward(forward);
    }

    public ActionForward removeDegreeFromCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return removeDegreeFromCandidacy(mapping, actionForm, request, response, "fill-candidacy-information");
    }

    private ActionForward removeDegreeFromCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, String forward) {

	final Over23IndividualCandidacyProcessBean bean = getCandidacyBean();
	request.setAttribute("over23IndividualCandidacyProcessBean", bean);

	final String[] degreeIDs = ((DynaActionForm) actionForm).getStrings("degreesToDelete");
	for (final Degree degree : getDegrees(degreeIDs)) {
	    if (bean.containsDegree(degree)) {
		bean.removeDegree(degree);
	    }
	}

	return mapping.findForward(forward);
    }

    private List<Degree> getDegrees(final String[] degreeIDs) {
	final List<Degree> result = new ArrayList<Degree>();
	for (final String degreeId : degreeIDs) {
	    final Degree degree = rootDomainObject.readDegreeByOID(Integer.valueOf(degreeId));
	    if (degree != null) {
		result.add(degree);
	    }
	}
	return result;
    }

    public ActionForward backToStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final Over23IndividualCandidacyProcessBean bean = getCandidacyBean();
	bean.removeDegreeToAdd();
	bean.removeSelectedDegrees();
	request.setAttribute("over23IndividualCandidacyProcessBean", bean);
	return mapping.findForward("prepare-create-new-process");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    request.setAttribute("process", executeService("CreateNewProcess", getProcessType().getName(), getCandidacyBean()));
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("over23IndividualCandidacyProcessBean", getCandidacyBean());
	    return mapping.findForward("fill-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, form, request, response);
    }

    @Override
    protected Over23IndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (Over23IndividualCandidacyProcess) super.getProcess(request);
    }

    private Over23IndividualCandidacyProcessBean getCandidacyBean() {
	return (Over23IndividualCandidacyProcessBean) getRenderedObject("over23IndividualCandidacyProcessBean");
    }

    public ActionForward prepareExecuteCandidacyPayment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("prepare-candidacy-payment");
    }

    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Over23IndividualCandidacyProcessBean bean = new Over23IndividualCandidacyProcessBean();
	bean.setPersonBean(new PersonBean(getProcess(request).getCandidacyPerson()));
	request.setAttribute("over23IndividualCandidacyProcessBean", bean);
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditCandidacyPersonalInformation", getCandidacyBean());
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("over23IndividualCandidacyProcessBean", getCandidacyBean());
	    return mapping.findForward("edit-candidacy-personal-information");
	}
	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyProcessBean",
		new Over23IndividualCandidacyProcessBean(getProcess(request)));
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyProcessBean", getCandidacyBean());
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward addDegreeToCandidacyWhenEditing(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return addDegreeToCandidacy(mapping, actionForm, request, response, "edit-candidacy-information");
    }

    public ActionForward removeDegreeFromCandidacyWhenEditing(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return removeDegreeFromCandidacy(mapping, actionForm, request, response, "edit-candidacy-information");
    }

    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditCandidacyInformation", getCandidacyBean());
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("over23IndividualCandidacyProcessBean", getCandidacyBean());
	    return mapping.findForward("edit-candidacy-information");
	}
	return listProcessAllowedActivities(mapping, form, request, response);
    }

    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyResultBean", new Over23IndividualCandidacyResultBean(getProcess(request)));
	return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyResultBean", getCandidacyResultBean());
	return mapping.findForward("introduce-candidacy-result");
    }

    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "IntroduceCandidacyResult", getCandidacyResultBean());
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return mapping.findForward("introduce-candidacy-result");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private Over23IndividualCandidacyResultBean getCandidacyResultBean() {
	return (Over23IndividualCandidacyResultBean) getRenderedObject("over23IndividualCandidacyResultBean");
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
	try {
	    executeActivity(getProcess(request), "CreateRegistration");
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }
}
