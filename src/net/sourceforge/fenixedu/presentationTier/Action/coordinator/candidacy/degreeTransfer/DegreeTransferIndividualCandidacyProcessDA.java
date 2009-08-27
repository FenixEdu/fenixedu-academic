package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.degreeTransfer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "coordinator", formBeanClass = FenixActionForm.class)
@Forwards( {
	// @Forward(name = "intro", path =
	// "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "intro", path = "/caseHandlingDegreeTransferCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/coordinator/candidacy/degreeTransfer/listIndividualCandidacyActivities.jsp") })
public class DegreeTransferIndividualCandidacyProcessDA extends IndividualCandidacyProcessDA {

    @Override
    protected Class getParentProcessType() {
	return DegreeTransferCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return DegreeTransferIndividualCandidacyProcess.class;
    }

    @Override
    protected DegreeTransferCandidacyProcess getParentProcess(HttpServletRequest request) {
	return (DegreeTransferCandidacyProcess) super.getParentProcess(request);
    }

    @Override
    protected DegreeTransferIndividualCandidacyProcess getProcess(HttpServletRequest request) {
	return (DegreeTransferIndividualCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected DegreeTransferIndividualCandidacyProcessBean getIndividualCandidacyProcessBean() {
	return (DegreeTransferIndividualCandidacyProcessBean) super.getIndividualCandidacyProcessBean();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final DegreeTransferIndividualCandidacyProcessBean bean = new DegreeTransferIndividualCandidacyProcessBean();
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

    private DegreeTransferIndividualCandidacyResultBean getCandidacyResultBean() {
	return (DegreeTransferIndividualCandidacyResultBean) getRenderedObject("individualCandidacyResultBean");
    }

    @Override
    protected void prepareInformationForBindPersonToCandidacyOperation(HttpServletRequest request,
	    IndividualCandidacyProcess process) {
	throw new RuntimeException("This shouldnt be called");

    }

}
