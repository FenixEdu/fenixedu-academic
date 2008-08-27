package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.competenceCourses;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public class ChangeCompetenceCourseInformationChangeRequestStatus extends Service {

    public void run(CompetenceCourseInformationChangeRequest changeRequest, Person analisedBy, Boolean status)
	    throws FenixServiceException {
	if (changeRequest.getApproved() != null) {
	    throw new FenixServiceException("error.request.already.processed");
	}

	changeRequest.setApproved(status);
	changeRequest.setAnalizedBy(analisedBy);
	if (status == true) {

	    CompetenceCourse course = changeRequest.getCompetenceCourse();

	    ExecutionSemester executionSemester = changeRequest.getExecutionPeriod();
	    CompetenceCourseInformation information = null;
	    if (course.isCompetenceCourseInformationDefinedAtExecutionPeriod(executionSemester)) {
		information = course.findCompetenceCourseInformationForExecutionPeriod(executionSemester);
		information.edit(changeRequest.getName(), changeRequest.getNameEn(), information.getBasic(), changeRequest
			.getCompetenceCourseLevel());
		information.setRegime(changeRequest.getRegime());
		information.edit(changeRequest.getObjectives(), changeRequest.getProgram(), changeRequest.getEvaluationMethod(),
			changeRequest.getObjectivesEn(), changeRequest.getProgramEn(), changeRequest.getEvaluationMethodEn());

		information.setBibliographicReferences(changeRequest.getBibliographicReferences());

		for (; !information.getCompetenceCourseLoads().isEmpty(); information.getCompetenceCourseLoads().get(0).delete())
		    ;
		createLoads(changeRequest, information);

	    } else {
		information = new CompetenceCourseInformation(changeRequest.getName(), changeRequest.getNameEn(), course
			.isBasic(), changeRequest.getRegime(), changeRequest.getCompetenceCourseLevel(), changeRequest
			.getExecutionPeriod());
		information.edit(changeRequest.getObjectives(), changeRequest.getProgram(), changeRequest.getEvaluationMethod(),
			changeRequest.getObjectivesEn(), changeRequest.getProgramEn(), changeRequest.getEvaluationMethodEn());
		information.setAcronym(course.getAcronym());
		information.setBibliographicReferences(changeRequest.getBibliographicReferences());
		course.addCompetenceCourseInformations(information);

		createLoads(changeRequest, information);
	    }
	}
    }

    private void createLoads(CompetenceCourseInformationChangeRequest changeRequest, CompetenceCourseInformation information) {
	CompetenceCourseLoad courseLoad = new CompetenceCourseLoad(changeRequest.getTheoreticalHours(), changeRequest
		.getProblemsHours(), changeRequest.getLaboratorialHours(), changeRequest.getSeminaryHours(), changeRequest
		.getFieldWorkHours(), changeRequest.getTrainingPeriodHours(), changeRequest.getTutorialOrientationHours(),
		changeRequest.getAutonomousWorkHours(), changeRequest.getEctsCredits(), Integer.valueOf(1), (changeRequest
			.getRegime() == RegimeType.SEMESTRIAL) ? AcademicPeriod.SEMESTER : AcademicPeriod.YEAR);

	information.addCompetenceCourseLoads(courseLoad);

	if (changeRequest.getRegime() == RegimeType.ANUAL) {
	    CompetenceCourseLoad secondCourseLoad = new CompetenceCourseLoad(changeRequest.getSecondTheoreticalHours(),
		    changeRequest.getSecondProblemsHours(), changeRequest.getSecondLaboratorialHours(), changeRequest
			    .getSecondSeminaryHours(), changeRequest.getSecondFieldWorkHours(), changeRequest
			    .getSecondTrainingPeriodHours(), changeRequest.getSecondTutorialOrientationHours(), changeRequest
			    .getSecondAutonomousWorkHours(), changeRequest.getSecondEctsCredits(), Integer.valueOf(2),
		    AcademicPeriod.YEAR);

	    information.addCompetenceCourseLoads(secondCourseLoad);
	}
    }
}
