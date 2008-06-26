package net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonIndividualProcess", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
	
	@Forward(name = "fill-candidacy-information", path = "/candidacy/graduatedPerson/fillCandidacyInformation.jsp"),
	
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editCandidacyPersonalInformation.jsp"),
	
	@Forward(name = "edit-candidacy-information", path = "/candidacy/secondCycle/editCandidacyInformation.jsp"),
	
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp")

})
public class DegreeCandidacyForGraduatedPersonIndividualProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getProcessType() {
	return DegreeCandidacyForGraduatedPersonIndividualProcess.class;
    }

    @Override
    protected Class getParentProcessType() {
	return DegreeCandidacyForGraduatedPersonProcess.class;
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonProcess getParentProcess(HttpServletRequest request) {
	return (DegreeCandidacyForGraduatedPersonProcess) super.getParentProcess(request);
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonIndividualProcessBean getIndividualCandidacyProcessBean() {
	return (DegreeCandidacyForGraduatedPersonIndividualProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = new DegreeCandidacyForGraduatedPersonIndividualProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));
	bean.setChoosePersonBean(new ChoosePersonBean());
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }
    
    public ActionForward fillCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("fill-candidacy-information");
    }

    public ActionForward fillCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("fill-candidacy-information");
    }

    /*
    public ActionForward fillCandidacyInformationPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final SecondCycleIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	RenderUtils.invalidateViewState();

	if (bean.hasPrecedentDegreeType()) {
	    if (bean.isExternalPrecedentDegreeType()) {
		bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean());
	    } else if (bean.hasPrecedentStudentCurricularPlan()) {
		createCandidacyPrecedentDegreeInformation(bean, bean.getPrecedentStudentCurricularPlan());
	    } else {
		final List<StudentCurricularPlan> scps = bean.getPrecedentStudentCurricularPlans();
		if (scps.size() == 1) {
		    createCandidacyPrecedentDegreeInformation(bean, scps.get(0));
		    bean.setPrecedentStudentCurricularPlan(scps.get(0));
		}
	    }
	}

	return mapping.findForward("fill-candidacy-information");
    }

    private void createCandidacyPrecedentDegreeInformation(final SecondCycleIndividualCandidacyProcessBean bean,
	    final StudentCurricularPlan studentCurricularPlan) {
	if (!studentCurricularPlan.isBolonhaDegree()) {
	    bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean(studentCurricularPlan));
	} else {
	    bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean(studentCurricularPlan,
		    CycleType.FIRST_CYCLE));
	}
    }

    public ActionForward fillCandidacyInformationStudentCurricularPlanPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final SecondCycleIndividualCandidacyProcessBean bean = getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

	if (bean.hasPrecedentStudentCurricularPlan()) {
	    createCandidacyPrecedentDegreeInformation(bean, bean.getPrecedentStudentCurricularPlan());
	}

	RenderUtils.invalidateViewState();
	return mapping.findForward("fill-candidacy-information");
    }
    */
    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final DegreeCandidacyForGraduatedPersonIndividualProcessBean bean = new DegreeCandidacyForGraduatedPersonIndividualProcessBean();
	bean.setPersonBean(new PersonBean(getProcess(request).getCandidacyPerson()));
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-personal-information");
    }

    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "EditCandidacyPersonalInformation", getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-personal-information");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }
    
}
