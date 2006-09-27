/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class StudentOperationsDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateStudent(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("executionDegreeBean", new ExecutionDegreeBean());
	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseDegreePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState()
		.getMetaObject().getObject();
	executionDegreeBean.setDegreeCurricularPlan(null);
	executionDegreeBean.setExecutionDegree(null);
	RenderUtils.invalidateViewState();
	request.setAttribute("executionDegreeBean", executionDegreeBean);

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseDegreeCurricularPlanPostBack(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState()
		.getMetaObject().getObject();

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

    public ActionForward chooseIngressionPostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState(
		"executionDegree").getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils
		.getViewState("chooseIngression").getMetaObject().getObject();

	RenderUtils.invalidateViewState();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);

	if (!ingressionInformationBean.getIngression().hasEntryPhase()) {
	    request.setAttribute("choosePersonBean", new ChoosePersonBean());
	}

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseEntryPhasePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState(
		"executionDegree").getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils
		.getViewState("chooseIngression").getMetaObject().getObject();

	RenderUtils.invalidateViewState();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);
	request.setAttribute("choosePersonBean", new ChoosePersonBean());

	return mapping.findForward("chooseNewStudentExecutionDegreeAndIdentification");
    }

    public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("executionDegreeBean", RenderUtils.getViewState().getMetaObject()
		.getObject());

	return mapping.getInputForward();
    }

    public ActionForward choosePerson(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState(
		"executionDegree").getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils
		.getViewState("chooseIngression").getMetaObject().getObject();
	PrecedentDegreeInformationBean precedentDegreeInformationBean = RenderUtils
		.getViewState("precedentDegreeInformation") == null ? new PrecedentDegreeInformationBean()
		: (PrecedentDegreeInformationBean) RenderUtils
			.getViewState("precedentDegreeInformation").getMetaObject().getObject();

	PersonBean personBean = null;
	String identificationNumber = null;
	IDDocumentType documentType = null;

	if (RenderUtils.getViewState("choosePerson") != null) { // 1st time
	    ChoosePersonBean choosePersonBean = (ChoosePersonBean) RenderUtils.getViewState(
		    "choosePerson").getMetaObject().getObject();
	    personBean = new PersonBean(choosePersonBean.getIdentificationNumber(), choosePersonBean
		    .getDocumentType());
	} else { // Postback
	    personBean = (PersonBean) RenderUtils.getViewState("person").getMetaObject().getObject();
	}

	Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationNumber,
		documentType);

	if (person == null) {

	    request.setAttribute("executionDegreeBean", executionDegreeBean);
	    request.setAttribute("ingressionInformationBean", ingressionInformationBean);
	    request.setAttribute("precedentDegreeInformationBean", precedentDegreeInformationBean);
	    request.setAttribute("personBean", personBean);
	    return mapping.findForward("fillNewPersonData");
	} else {

	}

	return null;
    }

    public ActionForward prepareShowCreateStudentConfirmation(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState(
		"executionDegree").getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils
		.getViewState("chooseIngression").getMetaObject().getObject();
	PersonBean personBean = (PersonBean) RenderUtils.getViewState("person").getMetaObject()
		.getObject();
	PrecedentDegreeInformationBean precedentDegreeInformationBean = (PrecedentDegreeInformationBean) RenderUtils
		.getViewState("precedentDegreeInformation").getMetaObject().getObject();

	request.setAttribute("executionDegreeBean", executionDegreeBean);
	request.setAttribute("ingressionInformationBean", ingressionInformationBean);
	request.setAttribute("personBean", personBean);
	request.setAttribute("precedentDegreeInformationBean", precedentDegreeInformationBean);

	return mapping.findForward("showCreateStudentConfirmation");
    }

    public ActionForward createStudent(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) RenderUtils.getViewState(
		"executionDegree").getMetaObject().getObject();
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) RenderUtils
		.getViewState("chooseIngression").getMetaObject().getObject();
	PersonBean personBean = (PersonBean) RenderUtils.getViewState("person").getMetaObject()
		.getObject();
	PrecedentDegreeInformationBean precedentDegreeInformationBean = (PrecedentDegreeInformationBean) RenderUtils
		.getViewState("precedentDegreeInformation").getMetaObject().getObject();

	Object[] args = { personBean, executionDegreeBean, precedentDegreeInformationBean,
		ingressionInformationBean };
	Registration registration = (Registration) ServiceUtils.executeService(getUserView(request),
		"CreateStudent", args);

	request.setAttribute("registration", registration);

	return mapping.findForward("createStudentSuccess");
    }

    public ActionForward printRegistrationDeclarationTemplate(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

	Integer registrationID = Integer.valueOf(request.getParameter("registrationID"));
	request.setAttribute("registration", rootDomainObject.readRegistrationByOID(registrationID));

	return mapping.findForward("printRegistrationDeclarationTemplate");
    }

}
