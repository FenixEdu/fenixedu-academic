/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class StudentOperationsDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("executionDegreeBean", new ExecutionDegreeBean());
	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseDegreePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState().getMetaObject().getObject();
	executionDegreeBean.setDegreeCurricularPlan(null);
	executionDegreeBean.setExecutionDegree(null);
	RenderUtils.invalidateViewState();
	request.setAttribute("executionDegreeBean", executionDegreeBean);

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseDegreeCurricularPlanPostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState().getMetaObject().getObject();

	ExecutionDegree executionDegree = null;
	if (executionDegreeBean.getDegreeCurricularPlan() != null) {
	    executionDegree = executionDegreeBean.getDegreeCurricularPlan().getExecutionDegreeByYear(
		    ExecutionYear.readCurrentExecutionYear());
	}

	executionDegreeBean.setExecutionDegree(executionDegree);
	RenderUtils.invalidateViewState();
	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", new IngressionInformationBean());

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseAgreementPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree")
		.getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils.getViewState(
		"chooseIngression").getMetaObject().getObject();
	ingressionInformationBean.clearIngressionAndEntryPhase();

	RenderUtils.invalidateViewState();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);

	if (ingressionInformationBean.getRegistrationAgreement() != null
		&& !ingressionInformationBean.getRegistrationAgreement().isNormal()) {
	    request.setAttribute("choosePersonBean", new ChoosePersonBean());
	}

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseIngressionPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree")
		.getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils.getViewState(
		"chooseIngression").getMetaObject().getObject();
	ingressionInformationBean.clearAgreement();

	RenderUtils.invalidateViewState();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);

	if (ingressionInformationBean.getIngression() != null && !ingressionInformationBean.getIngression().hasEntryPhase()) {
	    request.setAttribute("choosePersonBean", new ChoosePersonBean());
	}

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseEntryPhasePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree")
		.getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils.getViewState(
		"chooseIngression").getMetaObject().getObject();
	ingressionInformationBean.clearAgreement();

	RenderUtils.invalidateViewState();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);
	request.setAttribute("choosePersonBean", new ChoosePersonBean());

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("executionDegreeBean", RenderUtils.getViewState().getMetaObject().getObject());

	return mapping.getInputForward();
    }

    public ActionForward choosePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree")
		.getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils.getViewState(
		"chooseIngression").getMetaObject().getObject();
	PrecedentDegreeInformationBean precedentDegreeInformationBean = RenderUtils.getViewState("precedentDegreeInformation") == null ? new PrecedentDegreeInformationBean()
		: (PrecedentDegreeInformationBean) RenderUtils.getViewState("precedentDegreeInformation").getMetaObject()
			.getObject();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);
	request.setAttribute("precedentDegreeInformationBean", precedentDegreeInformationBean);

	PersonBean personBean = null;
	Person person = null;

	if (RenderUtils.getViewState("person") != null) { // Postback
	    request.setAttribute("personBean", RenderUtils.getViewState("person").getMetaObject().getObject());
	    return mapping.findForward("fillNewPersonData");
	}

	ChoosePersonBean choosePersonBean = (ChoosePersonBean) RenderUtils.getViewState("choosePerson").getMetaObject()
		.getObject();

	final String identificationNumber = choosePersonBean.getIdentificationNumber();
	final YearMonthDay dateOfBirth = choosePersonBean.getDateOfBirth();

	if (choosePersonBean.getPerson() == null) {

	    Collection<Person> persons = Person.findPersonByDocumentID(identificationNumber);

	    if (choosePersonBean.isFirstTimeSearch()) {
		choosePersonBean.setFirstTimeSearch(false);
		if (!persons.isEmpty()
			|| !Person.findByDateOfBirth(dateOfBirth,
				Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty()) {
		    // show similar persons
		    RenderUtils.invalidateViewState();
		    request.setAttribute("choosePersonBean", choosePersonBean);
		    return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
		}
	    }

	} else {
	    person = choosePersonBean.getPerson();
	}

	if (!checkIngression(mapping, request, executionDegreeBean, ingressionInformationBean, person, choosePersonBean)) {
	    return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
	}

	if (person != null) {
	    personBean = new PersonBean(person);

	    if (person.getEmployee() != null && person.getEmployee().getCurrentWorkingContract() != null) {
		request.setAttribute("personBean", personBean);
		return mapping.findForward("fillNewPersonDataForEmployee");
	    }

	} else {
	    personBean = new PersonBean(choosePersonBean.getName(), identificationNumber, choosePersonBean.getDocumentType(),
		    dateOfBirth);
	}

	request.setAttribute("personBean", personBean);
	return mapping.findForward("fillNewPersonData");
    }

    private boolean checkIngression(ActionMapping mapping, HttpServletRequest request, ExecutionDegreeBean executionDegreeBean,
	    IngressionInformationBean ingressionInformationBean, Person person, ChoosePersonBean choosePersonBean) {
	if (ingressionInformationBean.getIngression() == Ingression.RI) {
	    Degree sourceDegree = executionDegreeBean.getDegreeCurricularPlan().getEquivalencePlan()
		    .getSourceDegreeCurricularPlan().getDegree();

	    if (person == null || !person.hasStudent()) {
		RenderUtils.invalidateViewState();
		request.setAttribute("choosePersonBean", choosePersonBean);
		addActionMessage(request, "error.registration.preBolonhaSourceDegreeNotFound");
		return false;

	    } else {
		final Registration sourceRegistration = person.getStudent().readRegistrationByDegree(sourceDegree);
		if (sourceRegistration == null) {
		    RenderUtils.invalidateViewState();
		    request.setAttribute("choosePersonBean", choosePersonBean);
		    addActionMessage(request, "error.registration.preBolonhaSourceDegreeNotFound");
		    return false;
		}
		if (!sourceRegistration.getActiveStateType().canReingress()) {
		    RenderUtils.invalidateViewState();
		    request.setAttribute("choosePersonBean", choosePersonBean);
		    addActionMessage(request, "error.registration.preBolonhaSourceRegistrationCannotReingress");
		    return false;
		}
	    }

	}
	return true;
    }

    public ActionForward prepareShowCreateStudentConfirmation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree")
		.getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils.getViewState(
		"chooseIngression").getMetaObject().getObject();
	PersonBean personBean = (PersonBean) RenderUtils.getViewState("person").getMetaObject().getObject();
	PrecedentDegreeInformationBean precedentDegreeInformationBean = (PrecedentDegreeInformationBean) RenderUtils
		.getViewState("precedentDegreeInformation").getMetaObject().getObject();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);
	request.setAttribute("personBean", personBean);
	request.setAttribute("precedentDegreeInformationBean", precedentDegreeInformationBean);

	return mapping.findForward("showCreateStudentConfirmation");
    }

    public ActionForward createStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState("executionDegree")
		.getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils.getViewState(
		"chooseIngression").getMetaObject().getObject();
	PersonBean personBean = (PersonBean) RenderUtils.getViewState("person").getMetaObject().getObject();
	PrecedentDegreeInformationBean precedentDegreeInformationBean = (PrecedentDegreeInformationBean) RenderUtils
		.getViewState("precedentDegreeInformation").getMetaObject().getObject();

	Object[] args = { personBean, executionDegreeBean, precedentDegreeInformationBean, ingressionInformationBean };

	try {
	    Registration registration = (Registration) ServiceUtils.executeService(getUserView(request), "CreateStudent", args);
	    request.setAttribute("registration", registration);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return prepareShowCreateStudentConfirmation(mapping, actionForm, request, response);
	}

	return mapping.findForward("createStudentSuccess");
    }

    public ActionForward printRegistrationDeclarationTemplate(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	Integer registrationID = Integer.valueOf(request.getParameter("registrationID"));
	request.setAttribute("registration", rootDomainObject.readRegistrationByOID(registrationID));

	return mapping.findForward("printRegistrationDeclarationTemplate");
    }

}
