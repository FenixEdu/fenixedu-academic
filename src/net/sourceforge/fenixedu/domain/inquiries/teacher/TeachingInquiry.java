package net.sourceforge.fenixedu.domain.inquiries.teacher;

import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class TeachingInquiry extends TeachingInquiry_Base {

    public TeachingInquiry() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setResponseDateTime(new DateTime());
    }

    private static void setAnswers(final TeachingInquiryDTO inquiryDTO, final TeachingInquiry teachingInquiry) {
	Map<String, InquiriesQuestion> answersMap = inquiryDTO.buildAnswersMap(false);

	teachingInquiry.setLoadAndClassTypeContributionToFullfilmentOfCUProgram(answersMap.get(
		"loadAndClassTypeContributionToFullfilmentOfCUProgram").getValueAsInteger());
	teachingInquiry.setTeacherNumberSuitableForCUOperation(answersMap.get("teacherNumberSuitableForCUOperation")
		.getValueAsInteger());
	teachingInquiry.setEstablishedScheduleSuitable(answersMap.get("establishedScheduleSuitable").getValueAsInteger());
	teachingInquiry
		.setEstablishedScheduleNotSuitableReason(answersMap.get("establishedScheduleNotSuitableReason").getValue());
	teachingInquiry.setAvailableInfrastructureSuitable(answersMap.get("availableInfrastructureSuitable").getValueAsInteger());
	teachingInquiry.setAvailableInfrastructureSuitableReason(answersMap.get("availableInfrastructureSuitableReason")
		.getValue());

	teachingInquiry.setStudentsReadyForFollowingCU(answersMap.get("studentsReadyForFollowingCU").getValueAsInteger());
	teachingInquiry.setEvaluationMethodSuitableForCUTeachingTypeAndObjective(answersMap.get(
		"evaluationMethodSuitableForCUTeachingTypeAndObjective").getValueAsInteger());
	teachingInquiry.setDisturbingEventsInClasses(answersMap.get("disturbingEventsInClasses").getValueAsInteger());
	teachingInquiry.setDisturbingEventsInClassesReasonInfrastructure(answersMap.get(
		"disturbingEventsInClassesReasonInfrastructure").getValueAsBoolean());
	teachingInquiry.setDisturbingEventsInClassesReasonLowAssiduity(answersMap.get(
		"disturbingEventsInClassesReasonLowAssiduity").getValueAsBoolean());
	teachingInquiry.setDisturbingEventsInClassesReasonIndiscipline(answersMap.get(
		"disturbingEventsInClassesReasonIndiscipline").getValueAsBoolean());
	teachingInquiry.setDisturbingEventsInClassesReasonIntermediateEvaluations(answersMap.get(
		"disturbingEventsInClassesReasonIntermediateEvaluations").getValueAsBoolean());
	teachingInquiry
		.setDisturbingEventsInClassesDescription(answersMap.get("disturbingEventsInClassesDescription").getValue());

	teachingInquiry.setSemesterStartAverageStudentNumberInTheorical(answersMap.get(
		"semesterStartAverageStudentNumberInTheorical").getValue());
	teachingInquiry.setSemesterMiddleAverageStudentNumberInTheorical(answersMap.get(
		"semesterMiddleAverageStudentNumberInTheorical").getValue());
	teachingInquiry.setSemesterEndAverageStudentNumberInTheorical(answersMap
		.get("semesterEndAverageStudentNumberInTheorical").getValue());
	teachingInquiry.setSemesterStartActiveAndInteressedStudentsRatioInTheorical(answersMap.get(
		"semesterStartActiveAndInteressedStudentsRatioInTheorical").getValue());
	teachingInquiry.setSemesterMiddleActiveAndInteressedStudentsRatioInTheorica(answersMap.get(
		"semesterMiddleActiveAndInteressedStudentsRatioInTheorica").getValue());
	teachingInquiry.setSemesterEndActiveAndInteressedStudentsRatioInTheorical(answersMap.get(
		"semesterEndActiveAndInteressedStudentsRatioInTheorical").getValue());

	teachingInquiry.setSemesterStartAverageStudentNumberInProblems(answersMap.get(
		"semesterStartAverageStudentNumberInProblems").getValue());
	teachingInquiry.setSemesterMiddleAverageStudentNumberInProblems(answersMap.get(
		"semesterMiddleAverageStudentNumberInProblems").getValue());
	teachingInquiry.setSemesterEndAverageStudentNumberInProblems(answersMap.get("semesterEndAverageStudentNumberInProblems")
		.getValue());
	teachingInquiry.setSemesterStartActiveAndInteressedStudentsRatioInProblems(answersMap.get(
		"semesterStartActiveAndInteressedStudentsRatioInProblems").getValue());
	teachingInquiry.setSemesterMiddleActiveAndInteressedStudentsRatioInProblems(answersMap.get(
		"semesterMiddleActiveAndInteressedStudentsRatioInProblems").getValue());
	teachingInquiry.setSemesterEndActiveAndInteressedStudentsRatioInProblems(answersMap.get(
		"semesterEndActiveAndInteressedStudentsRatioInProblems").getValue());

	teachingInquiry.setSemesterStartAverageStudentNumberInLabs(answersMap.get("semesterStartAverageStudentNumberInLabs")
		.getValue());
	teachingInquiry.setSemesterMiddleAverageStudentNumberInLabs(answersMap.get("semesterMiddleAverageStudentNumberInLabs")
		.getValue());
	teachingInquiry.setSemesterEndAverageStudentNumberInLabs(answersMap.get("semesterEndAverageStudentNumberInLabs")
		.getValue());
	teachingInquiry.setSemesterStartActiveAndInteressedStudentsRatioInLabs(answersMap.get(
		"semesterStartActiveAndInteressedStudentsRatioInLabs").getValue());
	teachingInquiry.setSemesterMiddleActiveAndInteressedStudentsRatioInLabs(answersMap.get(
		"semesterMiddleActiveAndInteressedStudentsRatioInLabs").getValue());
	teachingInquiry.setSemesterEndActiveAndInteressedStudentsRatioInLabs(answersMap.get(
		"semesterEndActiveAndInteressedStudentsRatioInLabs").getValue());

	teachingInquiry.setSemesterStartAverageStudentNumberInSeminary(answersMap.get(
		"semesterStartAverageStudentNumberInSeminary").getValue());
	teachingInquiry.setSemesterMiddleAverageStudentNumberInSeminary(answersMap.get(
		"semesterMiddleAverageStudentNumberInSeminary").getValue());
	teachingInquiry.setSemesterEndAverageStudentNumberInSeminary(answersMap.get("semesterEndAverageStudentNumberInSeminary")
		.getValue());
	teachingInquiry.setSemesterStartActiveAndInteressedStudentsRatioInSeminary(answersMap.get(
		"semesterStartActiveAndInteressedStudentsRatioInSeminary").getValue());
	teachingInquiry.setSemesterMiddleActiveAndInteressedStudentsRatioInSeminary(answersMap.get(
		"semesterMiddleActiveAndInteressedStudentsRatioInSeminary").getValue());
	teachingInquiry.setSemesterEndActiveAndInteressedStudentsRatioInSeminary(answersMap.get(
		"semesterEndActiveAndInteressedStudentsRatioInSeminary").getValue());

	teachingInquiry.setSemesterStartAverageStudentNumberInProject(answersMap
		.get("semesterStartAverageStudentNumberInProject").getValue());
	teachingInquiry.setSemesterMiddleAverageStudentNumberInProject(answersMap.get(
		"semesterMiddleAverageStudentNumberInProject").getValue());
	teachingInquiry.setSemesterEndAverageStudentNumberInProject(answersMap.get("semesterEndAverageStudentNumberInProject")
		.getValue());
	teachingInquiry.setSemesterStartActiveAndInteressedStudentsRatioInProject(answersMap.get(
		"semesterStartActiveAndInteressedStudentsRatioInProject").getValue());
	teachingInquiry.setSemesterMiddleActiveAndInteressedStudentsRatioInProject(answersMap.get(
		"semesterMiddleActiveAndInteressedStudentsRatioInProject").getValue());
	teachingInquiry.setSemesterEndActiveAndInteressedStudentsRatioInProject(answersMap.get(
		"semesterEndActiveAndInteressedStudentsRatioInProject").getValue());

	teachingInquiry.setReportDisclosureToAcademicComunity(answersMap.get("reportDisclosureToAcademicComunity")
		.getValueAsBoolean());
	teachingInquiry.setResultsDisclosureToAcademicComunity(answersMap.get("resultsDisclosureToAcademicComunity")
		.getValueAsBoolean());

	teachingInquiry.setClarificationOfDoubtsOutsideClasses(answersMap.get("clarificationOfDoubtsOutsideClasses")
		.getValueAsInteger());
	teachingInquiry.setAutonomousWork(answersMap.get("autonomousWork").getValueAsInteger());
	teachingInquiry.setStudentsPerformance(answersMap.get("studentsPerformance").getValueAsInteger());
	teachingInquiry.setClassesAndOtherActivitiesFrequency(answersMap.get("classesAndOtherActivitiesFrequency")
		.getValueAsInteger());
	teachingInquiry.setClassesAndOtherActivitiesPonctuality(answersMap.get("classesAndOtherActivitiesPonctuality")
		.getValueAsInteger());
	teachingInquiry.setGlobalQualityOfTeachingInCU(answersMap.get("globalQualityOfTeachingInCU").getValueAsInteger());
	teachingInquiry.setPedagogicalActivitiesDeveloped(answersMap.get("pedagogicalActivitiesDeveloped").getValueAsInteger());
	teachingInquiry.setRelativePedagogicalInitiatives(answersMap.get("relativePedagogicalInitiatives").getValue());
	teachingInquiry.setGeneralCommentToCUOperation(answersMap.get("generalCommentToCUOperation").getValue());

	teachingInquiry.setNumberOfExams(answersMap.get("numberOfExams").getValueAsInteger());
	teachingInquiry.setNumberOfTests(answersMap.get("numberOfTests").getValueAsInteger());
	teachingInquiry.setNumberOfQuizzesAndMiniTests(answersMap.get("numberOfQuizzesAndMiniTests").getValueAsInteger());
	teachingInquiry.setNumberOfElectronicQuizzes(answersMap.get("numberOfElectronicQuizzes").getValueAsInteger());
	teachingInquiry.setNumberOfStudyVisitsOrOtherActivitiesReports(answersMap.get(
		"numberOfStudyVisitsOrOtherActivitiesReports").getValueAsInteger());
	teachingInquiry.setNumberOfWorksOrProjects(answersMap.get("numberOfWorksOrProjects").getValueAsInteger());
	teachingInquiry.setTeachingLanguage(answersMap.get("teachingLanguage").getValue());
	teachingInquiry.setOtherTeachingLanguage(answersMap.get("otherTeachingLanguage").getValue());
	teachingInquiry.setWorkLoadClassification(answersMap.get("workLoadClassification").getValueAsInteger());
	teachingInquiry.setWorkLoadClassificationReasons(answersMap.get("workLoadClassificationReasons").getValue());
	teachingInquiry.setPositionOfCUInStudentCurricularPlan(answersMap.get("positionOfCUInStudentCurricularPlan")
		.getValueAsInteger());
	teachingInquiry.setComprehensionAndKnowledgeOfCU(answersMap.get("comprehensionAndKnowledgeOfCU").getValueAsInteger());
	teachingInquiry.setComprehensionApplicationOfCU(answersMap.get("comprehensionApplicationOfCU").getValueAsInteger());
	teachingInquiry.setCriticalSenseAndReflexiveSpirit(answersMap.get("criticalSenseAndReflexiveSpirit").getValueAsInteger());
	teachingInquiry.setCooperationAndCommunicationCapacity(answersMap.get("cooperationAndCommunicationCapacity")
		.getValueAsInteger());
	teachingInquiry.setIncreaseAutonoumousLearningCapacity(answersMap.get("increaseAutonoumousLearningCapacity")
		.getValueAsInteger());
	teachingInquiry.setSocialAndProfessionalContextAnalysis(answersMap.get("socialAndProfessionalContextAnalysis")
		.getValueAsInteger());

	teachingInquiry.setGlobalClassificationOfThisCU(answersMap.get("globalClassificationOfThisCU").getValueAsInteger());
	teachingInquiry.setWeakPointsOfCUTeachingProcess(answersMap.get("weakPointsOfCUTeachingProcess").getValue());
	teachingInquiry.setStrongPointsOfCUTeachingProcess(answersMap.get("strongPointsOfCUTeachingProcess").getValue());
	teachingInquiry.setFinalCommentsAndImproovements(answersMap.get("finalCommentsAndImproovements").getValue());
	teachingInquiry.setNegativeResultsResolutionAndImproovementPlanOfAction(answersMap.get(
		"negativeResultsResolutionAndImproovementPlanOfAction").getValue());

	teachingInquiry.setResponsibleTeacherReportDisclosureToAcademicComunity(answersMap.get(
		"responsibleTeacherReportDisclosureToAcademicComunity").getValueAsBoolean());
    }

    @Service
    static public TeachingInquiry makeNew(final TeachingInquiryDTO inquiryDTO) {

	TeachingInquiry teachingInquiry = new TeachingInquiry();
	teachingInquiry.setProfessorship(inquiryDTO.getProfessorship());
	teachingInquiry.setAnswerDuration(inquiryDTO.getAnswerDuration());

	setAnswers(inquiryDTO, teachingInquiry);

	return teachingInquiry;
    }

    public void delete() {
	removeProfessorship();
	removeRootDomainObject();
	super.deleteDomainObject();

    }
}
