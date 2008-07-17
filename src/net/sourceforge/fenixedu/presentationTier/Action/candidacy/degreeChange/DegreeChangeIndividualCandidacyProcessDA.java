package net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.student.curriculum.AverageType;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.IndividualCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/listIndividualCandidacyActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/selectPersonForCandidacy.jsp"),
	@Forward(name = "fill-personal-information", path = "/candidacy/fillPersonalInformation.jsp"),
	@Forward(name = "fill-candidacy-information", path = "/candidacy/degreeChange/fillCandidacyInformation.jsp"),
	@Forward(name = "prepare-candidacy-payment", path = "/candidacy/candidacyPayment.jsp"),
	@Forward(name = "edit-candidacy-personal-information", path = "/candidacy/editPersonalInformation.jsp"),
	@Forward(name = "edit-candidacy-information", path = "/candidacy/degreeChange/editCandidacyInformation.jsp"),
	@Forward(name = "introduce-candidacy-result", path = "/candidacy/degreeChange/introduceCandidacyResult.jsp"),
	@Forward(name = "cancel-candidacy", path = "/candidacy/cancelCandidacy.jsp"),
	@Forward(name = "create-registration", path = "/candidacy/createRegistration.jsp")

})
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
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

    @Override
    protected void createCandidacyPrecedentDegreeInformation(IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean,
	    StudentCurricularPlan studentCurricularPlan) {

	super.createCandidacyPrecedentDegreeInformation(bean, studentCurricularPlan);

	final CandidacyPrecedentDegreeInformationBean degreeInformation = bean.getPrecedentDegreeInformation();
	degreeInformation.setNumberOfApprovedCurricularCourses(studentCurricularPlan.getRoot()
		.getNumberOfAllApprovedCurriculumLines());
	degreeInformation.setApprovedEcts(BigDecimal.valueOf(studentCurricularPlan.getRoot().getAprovedEctsCredits()));
	degreeInformation.setEnroledEcts(BigDecimal.valueOf(studentCurricularPlan.getRoot().getEctsCredits()));

	final Curriculum curriculum = studentCurricularPlan.getRoot().getCurriculum();
	curriculum.setAverageType(AverageType.SIMPLE);
	degreeInformation.setGradeSum(curriculum.getSumPiCi());
    }

}
