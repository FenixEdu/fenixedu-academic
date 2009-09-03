package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeChange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "scientificCouncil", formBeanClass = FenixActionForm.class)
@Forwards( {
	// @Forward(name = "intro", path =
	// "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "intro", path = "/caseHandlingDegreeChangeCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/scientificCouncil/candidacy/degreeChange/listIndividualCandidacyActivities.jsp") })
public class DegreeChangeIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return DegreeChangeCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return DegreeChangeIndividualCandidacyProcess.class;
    }

    @Override
    protected DegreeChangeCandidacyProcess getParentProcess(HttpServletRequest request) {
	return (DegreeChangeCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected DegreeChangeIndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (DegreeChangeIndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected DegreeChangeIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
	return (DegreeChangeIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final DegreeChangeIndividualCandidacyProcessBean bean = new DegreeChangeIndividualCandidacyProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));
	bean.setChoosePersonBean(new ChoosePersonBean());

	/*
	 * 18/07/2009 - A informacao para o RAIDs nao e introduzidas mas temos
	 * de criar este bean
	 */
	bean.setCandidacyInformationBean(new CandidacyInformationBean());

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    protected void createCandidacyPrecedentDegreeInformation(IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
	    StudentCurricularPlan studentCurricularPlan) {

	final CandidacyPrecedentDegreeInformationBean info = new CandidacyPrecedentDegreeInformationBean();

	info.setDegreeDesignation(studentCurricularPlan.getName());
	info.setInstitutionUnitName(rootDomainObject.getInstitutionUnit().getUnitName());
	info.initCurricularCoursesInformation(studentCurricularPlan);

	bean.setPrecedentDegreeInformation(info);
    }

    private DegreeChangeIndividualCandidacyResultBean getCandidacyResultBean() {
	return (DegreeChangeIndividualCandidacyResultBean) getRenderedObject("individualCandidacyResultBean");
    }

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
	    IndividualCandidacyProcess process) {
	throw new RuntimeException("This method shouldnt be called");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return super.execute(mapping, actionForm, request, response);
    }

}
