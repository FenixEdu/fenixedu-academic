/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiryDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherInquiryDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesCourse extends InquiriesCourse_Base {

    public InquiriesCourse() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
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
     * Preconditions: - executionCourse != null - executionDegreeCourse != null -
     * executionDegreeStudent != null - executionPeriod != null Postcondition: -
     * A new inquiriesCourse is created, referencing correctly the other
     * domainObjects and its basic properties are initialized Invariants: - None
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
    }

    public void createInquiriesTeacher(Teacher teacher, ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {
	new InquiriesTeacher(this, teacher, shiftType, infoInquiriesTeacher);
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
	inquiriesCourse.setHighWorkLoadReasonOtherReasons(answersMap.get("highWorkLoadReasonOtherReasons").getValue());
	inquiriesCourse.setPreviousKnowledgeEnoughToCUAttendance(answersMap.get("previousKnowledgeEnoughToCUAttendance")
		.getValueAsInteger());
	inquiriesCourse.setActivityParticipation(answersMap.get("activityParticipation").getValueAsInteger());
	inquiriesCourse.setKnowledgeAndComprehensionOfCU(answersMap.get("knowledgeAndComprehensionOfCU").getValueAsInteger());
	inquiriesCourse.setKnowledgeApplicationOfCU(answersMap.get("knowledgeApplicationOfCU").getValueAsInteger());
	inquiriesCourse.setCriticSenseAndReflexiveSpirit(answersMap.get("criticSenseAndReflexiveSpirit").getValueAsInteger());
	inquiriesCourse.setCooperationAndComunicationCapacity(answersMap.get("cooperationAndComunicationCapacity")
		.getValueAsInteger());
	inquiriesCourse.setPredictedProgramTeached(answersMap.get("predictedProgramTeached").getValueAsInteger());
	inquiriesCourse.setWellStructuredOfCU(answersMap.get("wellStructuredOfCU").getValueAsInteger());
	inquiriesCourse.setGoodGuidanceMaterial(answersMap.get("goodGuidanceMaterial").getValueAsInteger());
	inquiriesCourse.setRecomendendBibliographyImportance(answersMap.get("recomendendBibliographyImportance")
		.getValueAsInteger());
	inquiriesCourse.setFairEvaluationMethods(answersMap.get("fairEvaluationMethods").getValueAsInteger());
	inquiriesCourse.setGlobalClassificationOfCU(answersMap.get("globalClassificationOfCU").getValueAsInteger());
    }

}
