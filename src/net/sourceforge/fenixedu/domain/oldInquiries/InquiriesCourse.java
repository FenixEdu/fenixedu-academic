/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.StudentInquiryDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeacherInquiryDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistryState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryNotAnsweredJustification;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.DateTime;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesCourse extends InquiriesCourse_Base {

	public InquiriesCourse() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setResponseDateTime(new DateTime());
	}

	/**
	 * 
	 * @param executionCourse
	 * @param executionDegreeCourse
	 * @param executionDegreeStudent
	 * @param executionSemester
	 * @param schoolClass
	 * @param infoInquiriesCourse
	 * 
	 *            Preconditions: - executionCourse != null -
	 *            executionDegreeCourse != null - executionDegreeStudent != null
	 *            - executionPeriod != null Postcondition: - A new
	 *            inquiriesCourse is created, referencing correctly the other
	 *            domainObjects and its basic properties are initialized
	 *            Invariants: - None
	 */
	public InquiriesCourse(ExecutionCourse executionCourse, ExecutionDegree executionDegreeCourse,
			ExecutionDegree executionDegreeStudent, ExecutionSemester executionSemester, SchoolClass schoolClass,
			InfoInquiriesCourse infoInquiriesCourse, Character entryGradeClassification,
			Character approvationRatioClassification, Character arithmeticMeanClassification) {
		this();
		if ((executionCourse == null) || (executionDegreeCourse == null) || (executionDegreeStudent == null)
				|| (executionSemester == null)) {
			throw new DomainException(
					"Neither the executionCourse, executionDegreeCourse, executionDegreeStudent nor executionPeriod should not be null!");
		}
		this.setExecutionCourse(executionCourse);
		this.setExecutionDegreeCourse(executionDegreeCourse);
		this.setExecutionDegreeStudent(executionDegreeStudent);
		this.setExecutionPeriod(executionSemester);
		this.setStudentSchoolClass(schoolClass);
		this.setEntryGradeClassification(entryGradeClassification);
		this.setApprovationRatioClassification(approvationRatioClassification);
		this.setArithmeticMeanClassification(arithmeticMeanClassification);

		this.setBasicProperties(infoInquiriesCourse);

	}

	public InquiriesCourse(InquiriesRegistry inquiriesRegistry) {
		this();
		setExecutionPeriod(inquiriesRegistry.getExecutionPeriod());
		setExecutionCourse(inquiriesRegistry.getExecutionCourse());
		setExecutionDegreeStudent(inquiriesRegistry.getExecutionDegree());

		setWeeklyHoursSpentPercentage(inquiriesRegistry.getWeeklyHoursSpentPercentage());
		setStudyDaysSpentInExamsSeason(inquiriesRegistry.getStudyDaysSpentInExamsSeason());

		setExecutionDegreeCourse(ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(inquiriesRegistry
				.getCurricularCourse().getDegreeCurricularPlan(), inquiriesRegistry.getExecutionPeriod().getExecutionYear()));
	}

	public void createInquiriesTeacher(Professorship professorship, ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {
		new InquiriesTeacher(this, professorship, shiftType, infoInquiriesTeacher);
	}

	public void createInquiriesTeacher(NonAffiliatedTeacher nonAffiliatedTeacher, ShiftType shiftType,
			InfoInquiriesTeacher infoInquiriesTeacher) {
		new InquiriesTeacher(this, nonAffiliatedTeacher, shiftType, infoInquiriesTeacher);
	}

	public void createInquiriesRoom(AllocatableSpace room, InfoInquiriesRoom infoInquiriesRoom) {
		new InquiriesRoom(this, room, infoInquiriesRoom);
	}

	private void setBasicProperties(InfoInquiriesCourse infoInquiriesCourse) {
		this.setStudentCurricularYear(infoInquiriesCourse.getStudentCurricularYear());
		this.setStudentFirstEnrollment(infoInquiriesCourse.getStudentFirstEnrollment());
		this.setOnlineInfo(infoInquiriesCourse.getOnlineInfo());
		this.setClassCoordination(infoInquiriesCourse.getClassCoordination());
		this.setStudyElementsContribution(infoInquiriesCourse.getStudyElementsContribution());
		this.setPreviousKnowledgeArticulation(infoInquiriesCourse.getPreviousKnowledgeArticulation());
		this.setContributionForGraduation(infoInquiriesCourse.getContributionForGraduation());
		this.setEvaluationMethodAdequation(infoInquiriesCourse.getEvaluationMethodAdequation());
		this.setWeeklySpentHours(infoInquiriesCourse.getWeeklySpentHours());
		this.setGlobalAppreciation(infoInquiriesCourse.getGlobalAppreciation());
	}

	public static InquiriesCourse makeNew(StudentInquiryDTO inquiryDTO) {
		final InquiriesRegistry inquiriesRegistry = inquiryDTO.getInquiriesRegistry();

		InquiriesCourse inquiriesCourse = new InquiriesCourse(inquiriesRegistry);
		inquiriesCourse.setAnswerDuration(inquiryDTO.getAnswerDuration());
		//TODO remove this classes at the end of the new QUC model implementation
		final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod = null; //inquiriesRegistry.getStudent().getStudent().getInquiriesStudentExecutionPeriod(inquiriesRegistry.getExecutionPeriod());

		inquiriesCourse.setWeeklyHoursSpentInClassesSeason(inquiriesStudentExecutionPeriod.getWeeklyHoursSpentInClassesSeason());

		setAnswers(inquiryDTO, inquiriesCourse);

		inquiriesRegistry.setState(InquiriesRegistryState.ANSWERED);

		Map<TeacherDTO, Collection<? extends TeacherInquiryDTO>> inquiriesByTeacher = inquiryDTO.getTeachersInquiries();
		for (Collection<? extends TeacherInquiryDTO> teachersInquiries : inquiriesByTeacher.values()) {
			for (TeacherInquiryDTO teacherInquiryDTO : teachersInquiries) {
				if (teacherInquiryDTO.isFilled()) {
					inquiriesCourse.addAssociatedInquiriesTeachers(InquiriesTeacher.makeNew(teacherInquiryDTO));
				}
			}
		}

		return inquiriesCourse;
	}

	public static InquiriesCourse makeNewNotAnswered(final InquiriesRegistry inquiriesRegistry,
			final InquiryNotAnsweredJustification justification, final String otherJustification) {
		final InquiriesCourse inquiriesCourse = new InquiriesCourse(inquiriesRegistry);
		//TODO remove this classes at the end of the new QUC model implementation
		final InquiriesStudentExecutionPeriod inquiriesStudentExecutionPeriod = null; //inquiriesRegistry.getStudent().getStudent().getInquiriesStudentExecutionPeriod(inquiriesRegistry.getExecutionPeriod());
		inquiriesCourse.setWeeklyHoursSpentInClassesSeason(inquiriesStudentExecutionPeriod.getWeeklyHoursSpentInClassesSeason());
		inquiriesRegistry.setState(InquiriesRegistryState.NOT_ANSWERED);

		inquiriesCourse.setNotAnsweredJustification(justification);
		inquiriesCourse.setNotAnsweredOtherJustification(otherJustification);

		return inquiriesCourse;
	}

	private static void setAnswers(StudentInquiryDTO inquiryDTO, InquiriesCourse inquiriesCourse) {
		Map<String, InquiriesQuestion> answersMap = inquiryDTO.buildAnswersMap(false);

		inquiriesCourse.setClassificationInThisCU(answersMap.get("classificationInThisCU").getValue());
		inquiriesCourse.setHighWorkLoadReasonComplexProjects(answersMap.get("highWorkLoadReasonComplexProjects")
				.getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonExtenseProjects(answersMap.get("highWorkLoadReasonExtenseProjects")
				.getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonManyProjects(answersMap.get("highWorkLoadReasonManyProjects").getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonLackOfPreviousPreparation(answersMap.get(
				"highWorkLoadReasonLackOfPreviousPreparation").getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonCurricularProgramExtension(answersMap.get(
				"highWorkLoadReasonCurricularProgramExtension").getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonLackOfAttendanceOfLessons(answersMap.get(
				"highWorkLoadReasonLackOfAttendanceOfLessons").getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonCuOrganizationProblems(answersMap.get("highWorkLoadReasonCuOrganizationProblems")
				.getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonPersonalOrTeamProblems(answersMap.get("highWorkLoadReasonPersonalOrTeamProblems")
				.getValueAsBoolean());
		inquiriesCourse.setHighWorkLoadReasonOtherReasons(answersMap.get("highWorkLoadReasonOtherReasons").getValue());
		inquiriesCourse.setPreviousKnowledgeEnoughToCUAttendance(answersMap.get("previousKnowledgeEnoughToCUAttendance")
				.getValueAsInteger());
		inquiriesCourse.setActivityParticipation(answersMap.get("activityParticipation").getValueAsInteger());
		inquiriesCourse.setKnowledgeAndComprehensionOfCU(answersMap.get("knowledgeAndComprehensionOfCU").getValueAsInteger());
		inquiriesCourse.setKnowledgeApplicationOfCU(answersMap.get("knowledgeApplicationOfCU").getValueAsInteger());
		inquiriesCourse.setCriticSenseAndReflexiveSpirit(answersMap.get("criticSenseAndReflexiveSpirit").getValueAsInteger());
		inquiriesCourse.setCooperationAndComunicationCapacity(answersMap.get("cooperationAndComunicationCapacity")
				.getValueAsInteger());
		inquiriesCourse.setAutonomousLearningCapacity(answersMap.get("autonomousLearningCapacity").getValueAsInteger());
		inquiriesCourse.setSocialAndProfessionalContextAnalysis(answersMap.get("socialAndProfessionalContextAnalysis")
				.getValueAsInteger());
		inquiriesCourse.setStudyMethodAttendToClasses(answersMap.get("studyMethodAttendToClasses").getValueAsBoolean());
		inquiriesCourse.setStudyMethodSuggestedBibliography(answersMap.get("studyMethodSuggestedBibliography")
				.getValueAsBoolean());
		inquiriesCourse.setStudyMethodTeacherDocuments(answersMap.get("studyMethodTeacherDocuments").getValueAsBoolean());
		inquiriesCourse.setStudyMethodStudentDocuments(answersMap.get("studyMethodStudentDocuments").getValueAsBoolean());
		inquiriesCourse.setStudyMethodOther(answersMap.get("studyMethodOther").getValue());
		inquiriesCourse.setPredictedProgramTeached(answersMap.get("predictedProgramTeached").getValueAsInteger());
		inquiriesCourse.setWellStructuredOfCU(answersMap.get("wellStructuredOfCU").getValueAsInteger());
		inquiriesCourse.setGoodGuidanceMaterial(answersMap.get("goodGuidanceMaterial").getValueAsInteger());
		inquiriesCourse.setRecomendendBibliographyImportance(answersMap.get("recomendendBibliographyImportance")
				.getValueAsInteger());
		inquiriesCourse.setFairEvaluationMethods(answersMap.get("fairEvaluationMethods").getValueAsInteger());
		inquiriesCourse.setGlobalClassificationOfCU(answersMap.get("globalClassificationOfCU").getValueAsInteger());
	}

	@Deprecated
	public java.util.Date getResponse() {
		org.joda.time.DateTime dt = getResponseDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setResponse(java.util.Date date) {
		if (date == null) {
			setResponseDateTime(null);
		} else {
			setResponseDateTime(new org.joda.time.DateTime(date.getTime()));
		}
	}

}
