package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CompetenceCourseInformationChangeRequest extends CompetenceCourseInformationChangeRequest_Base {

    public CompetenceCourseInformationChangeRequest() {
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CompetenceCourseInformationChangeRequest(String name, String nameEn, String justification, RegimeType regime,
	    String objectives, String objectivesEn, String program, String programEn, String evaluationMethod,
	    String evaluationMethodEn, CompetenceCourse course, CompetenceCourseLevel level, ExecutionSemester period,
	    Person requester, Double theoreticalHours, Double problemsHours, Double laboratorialHours, Double seminaryHours,
	    Double fieldWorkHours, Double trainingPeriodHours, Double tutorialOrientationHours, Double autonomousWorkHours,
	    Double ectsCredits, Double secondTheoreticalHours, Double secondProblemsHours, Double secondLaboratorialHours,
	    Double secondSeminaryHours, Double secondFieldWorkHours, Double secondTrainingPeriodHours,
	    Double secondTutorialOrientationHours, Double secondAutonomousWorkHours, Double secondEctsCredits,
	    BibliographicReferences references) {
	this();

	if (name == null || nameEn == null || justification == null || regime == null || objectives == null
		|| objectivesEn == null || program == null || programEn == null || evaluationMethod == null
		|| evaluationMethodEn == null || course == null || period == null || requester == null || level == null
		|| theoreticalHours == null || problemsHours == null || laboratorialHours == null || seminaryHours == null
		|| fieldWorkHours == null || trainingPeriodHours == null || tutorialOrientationHours == null
		|| autonomousWorkHours == null || ectsCredits == null) {

	    throw new DomainException("error.fields.are.required");
	}

	setName(name);
	setNameEn(nameEn);
	setJustification(justification);
	setRegime(regime);
	setObjectives(objectives);
	setObjectivesEn(objectivesEn);
	setProgram(program);
	setProgramEn(programEn);
	setEvaluationMethod(evaluationMethod);
	setEvaluationMethodEn(evaluationMethodEn);
	setCompetenceCourse(course);
	setExecutionPeriod(period);
	setRequester(requester);
	setApproved(null);
	setCompetenceCourseLevel(level);

	setTheoreticalHours(theoreticalHours);
	setProblemsHours(problemsHours);
	setLaboratorialHours(laboratorialHours);
	setSeminaryHours(seminaryHours);
	setFieldWorkHours(fieldWorkHours);
	setTrainingPeriodHours(trainingPeriodHours);
	setTutorialOrientationHours(tutorialOrientationHours);
	setAutonomousWorkHours(autonomousWorkHours);
	setEctsCredits(ectsCredits);

	setSecondTheoreticalHours(secondTheoreticalHours);
	setSecondProblemsHours(secondProblemsHours);
	setSecondLaboratorialHours(secondLaboratorialHours);
	setSecondSeminaryHours(secondSeminaryHours);
	setSecondFieldWorkHours(secondFieldWorkHours);
	setSecondTrainingPeriodHours(secondTrainingPeriodHours);
	setSecondTutorialOrientationHours(secondTutorialOrientationHours);
	setSecondAutonomousWorkHours(secondAutonomousWorkHours);
	setSecondEctsCredits(secondEctsCredits);

	setBibliographicReferences(references);
    }

    public RequestStatus getStatus() {
	if (getApproved() == null) {
	    return RequestStatus.ON_HOLD;
	} else {
	    return (getApproved()) ? RequestStatus.APPROVED : RequestStatus.REJECTED;
	}
    }

    public void delete() {
	removeCompetenceCourse();
	removeAnalizedBy();
	removeRequester();
	removeExecutionPeriod();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public enum RequestStatus {
	APPROVED, REJECTED, ON_HOLD;
    }
}
