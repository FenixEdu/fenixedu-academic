package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.CompetenceCourseInformationRequestBean;
import net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.CompetenceCourseLoadBean;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateCompetenceCourseInformationChangeRequest extends FenixService {

    @Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
    @Service
    public static void run(CompetenceCourseInformationRequestBean bean, CompetenceCourseLoadBean loadBean, Person requestor) {

	CompetenceCourse course = bean.getCompetenceCourse();
	ExecutionSemester period = bean.getExecutionPeriod();
	CompetenceCourseInformationChangeRequest request = course.getCompetenceCourseInformationChangeRequests(period);
	if (request != null) {
	    request.delete();
	}

	new CompetenceCourseInformationChangeRequest(bean.getName(), bean.getNameEn(), bean.getJustification(), bean.getRegime(),
		bean.getObjectives(), bean.getObjectivesEn(), bean.getProgram(), bean.getProgramEn(), bean.getEvaluationMethod(),
		bean.getEvaluationMethodEn(), bean.getCompetenceCourse(), bean.getCompetenceCourseLevel(), bean
			.getExecutionPeriod(), requestor, loadBean.getTheoreticalHours(), loadBean.getProblemsHours(), loadBean
			.getLaboratorialHours(), loadBean.getSeminaryHours(), loadBean.getFieldWorkHours(), loadBean
			.getTrainingPeriodHours(), loadBean.getTutorialOrientationHours(), loadBean.getAutonomousWorkHours(),
		loadBean.getEctsCredits(), loadBean.getSecondTheoreticalHours(), loadBean.getSecondProblemsHours(), loadBean
			.getSecondLaboratorialHours(), loadBean.getSecondSeminaryHours(), loadBean.getSecondFieldWorkHours(),
		loadBean.getSecondTrainingPeriodHours(), loadBean.getSecondTutorialOrientationHours(), loadBean
			.getSecondAutonomousWorkHours(), loadBean.getSecondEctsCredits(), bean.getReferences());

    }
}