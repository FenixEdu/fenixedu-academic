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

    @Service
    static public TeachingInquiry makeNew(final TeachingInquiryDTO inquiryDTO) {

	TeachingInquiry teachingInquiry = new TeachingInquiry();
	teachingInquiry.setProfessorship(inquiryDTO.getProfessorship());
	teachingInquiry.setAnswerDuration(inquiryDTO.getAnswerDuration());

	setAnswers(inquiryDTO, teachingInquiry);

	return teachingInquiry;
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
	teachingInquiry.setStudentsReadyForFollowingCU(answersMap.get("studentsReadyForFollowingCU").getValueAsInteger());
	teachingInquiry.setEvaluationMethodSuitableForCUTeachingTypeAndObjective(answersMap.get(
		"evaluationMethodSuitableForCUTeachingTypeAndObjective").getValueAsInteger());
	teachingInquiry.setDisturbingEventsInClasses(answersMap.get("disturbingEventsInClasses").getValueAsInteger());
	teachingInquiry
		.setDisturbingEventsInClassesDescription(answersMap.get("disturbingEventsInClassesDescription").getValue());
	teachingInquiry.setSemesterStartAverageStudentNumber(answersMap.get("semesterStartAverageStudentNumber")
		.getValueAsInteger());
	teachingInquiry.setSemesterMiddleAverageStudentNumber(answersMap.get("semesterMiddleAverageStudentNumber")
		.getValueAsInteger());
	teachingInquiry.setSemesterEndAverageStudentNumber(answersMap.get("semesterEndAverageStudentNumber").getValueAsInteger());
	teachingInquiry.setActiveAndInteressedStudentsRatio(answersMap.get("activeAndInteressedStudentsRatio").getValue());
	teachingInquiry.setStudentsPerformance(answersMap.get("studentsPerformance").getValueAsInteger());
	teachingInquiry.setClassesAndOtherActivitiesFrequency(answersMap.get("classesAndOtherActivitiesFrequency")
		.getValueAsInteger());
	teachingInquiry.setClassesAndOtherActivitiesPonctuality(answersMap.get("classesAndOtherActivitiesPonctuality")
		.getValueAsInteger());
	teachingInquiry.setGlobalQualityOfTeachingInCU(answersMap.get("globalQualityOfTeachingInCU").getValueAsInteger());
	teachingInquiry.setPedagogicalActivitiesDeveloped(answersMap.get("pedagogicalActivitiesDeveloped").getValueAsInteger());
	teachingInquiry.setRelativePedagogicalInitiatives(answersMap.get("relativePedagogicalInitiatives").getValue());

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
	teachingInquiry.setGlobalClassificationOfThisCU(answersMap.get("globalClassificationOfThisCU").getValueAsInteger());
	teachingInquiry.setWeakPointsOfCUTeachingProcess(answersMap.get("weakPointsOfCUTeachingProcess").getValue());
	teachingInquiry.setStrongPointsOfCUTeachingProcess(answersMap.get("strongPointsOfCUTeachingProcess").getValue());
	teachingInquiry.setFinalCommentsAndImproovements(answersMap.get("finalCommentsAndImproovements").getValue());
	teachingInquiry.setNegativeResultsResolutionAndImproovementPlanOfAction(answersMap.get(
		"negativeResultsResolutionAndImproovementPlanOfAction").getValue());
    }

}
