package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.AffiliatedTeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.NonAffiliatedTeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeacherDTO;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.EntryGradesInterval;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistryState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class StudentInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DateTime startedWhen;
    private Set<InquiryBlockDTO> curricularCourseBlocks;
    private StudentInquiryRegistry inquiryRegistry;
    Map<TeacherDTO, List<? extends StudentTeacherInquiryBean>> teachersInquiries = new TreeMap<TeacherDTO, List<? extends StudentTeacherInquiryBean>>(
	    new BeanComparator("name"));

    public StudentInquiryBean(StudentTeacherInquiryTemplate studentTeacherInquiryTemplate, StudentInquiryRegistry inquiryRegistry) {
	setInquiryRegistry(inquiryRegistry);
	setStartedWhen(new DateTime());
	final ExecutionCourse executionCourse = getInquiryRegistry().getExecutionCourse();
	final Set<ShiftType> shiftTypes = executionCourse.getShiftTypes();

	fillTeachersInquiriesWithAffiliatedTeachers(executionCourse, shiftTypes, studentTeacherInquiryTemplate);
	fillTeachersInquiriesWithNonAffiliatedTeachers(executionCourse, shiftTypes, studentTeacherInquiryTemplate);
    }

    private void fillTeachersInquiriesWithNonAffiliatedTeachers(final ExecutionCourse executionCourse,
	    final Set<ShiftType> shiftTypes, StudentInquiryTemplate studentTeacherInquiryTemplate) {
	for (final NonAffiliatedTeacher nonAffiliatedTeacher : executionCourse.getNonAffiliatedTeachers()) {
	    final NonAffiliatedTeacherDTO nonAffiliatedTeacherDTO = new NonAffiliatedTeacherDTO(nonAffiliatedTeacher);
	    List<StudentTeacherInquiryBean> nonAffiliatedTeachers = new ArrayList<StudentTeacherInquiryBean>();
	    for (final ShiftType shiftType : shiftTypes) {
		nonAffiliatedTeachers.add(new StudentTeacherInquiryBean(nonAffiliatedTeacherDTO, executionCourse, shiftType,
			studentTeacherInquiryTemplate));
	    }
	    Collections.sort(nonAffiliatedTeachers, new BeanComparator("shiftType"));
	    getTeachersInquiries().put(nonAffiliatedTeacherDTO, nonAffiliatedTeachers);
	}
    }

    private void fillTeachersInquiriesWithAffiliatedTeachers(final ExecutionCourse executionCourse,
	    final Set<ShiftType> shiftTypes, StudentInquiryTemplate studentTeacherInquiryTemplate) {
	Map<Person, Map<ShiftType, StudentTeacherInquiryBean>> teachersShifts = new HashMap<Person, Map<ShiftType, StudentTeacherInquiryBean>>();
	for (final Professorship professorship : executionCourse.getProfessorships()) {

	    final Person person = professorship.getPerson();
	    if (!teachersShifts.containsKey(person)) {
		teachersShifts.put(person, new HashMap<ShiftType, StudentTeacherInquiryBean>());
	    }

	    final Map<ShiftType, StudentTeacherInquiryBean> teacherShift = teachersShifts.get(person);
	    final TeacherDTO teacherDTO = new AffiliatedTeacherDTO(person);

	    Teacher teacher = person.getTeacher();
	    boolean mandatoryTeachingService = false;
	    if (teacher != null) {
		Category category = teacher.getCategoryForCreditsByPeriod(executionCourse.getExecutionPeriod());
		if (category != null && category.isTeacherCareerCategory()) {
		    mandatoryTeachingService = true;
		}
	    }

	    for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServices()) {
		if (degreeTeachingService.getPercentage() >= 20) {
		    for (ShiftType shiftType : degreeTeachingService.getShift().getTypes()) {
			if (!teacherShift.containsKey(shiftType)) {
			    teacherShift.put(shiftType, new StudentTeacherInquiryBean(teacherDTO, executionCourse, shiftType,
				    studentTeacherInquiryTemplate));
			}
		    }
		}
	    }
	    if (teacherShift.isEmpty() && !mandatoryTeachingService) {
		System.out.println("NAO TEM SERVIÇO DOCENTE");
		for (final ShiftType shiftType : shiftTypes) {
		    teacherShift.put(shiftType, new StudentTeacherInquiryBean(teacherDTO, executionCourse, shiftType,
			    studentTeacherInquiryTemplate));
		}
	    } else {
		System.out.println("PROF CARREIRA SEM SERVIÇO DOCENTE ACIMA DOS 20%");
	    }
	}

	for (Entry<Person, Map<ShiftType, StudentTeacherInquiryBean>> entry : teachersShifts.entrySet()) {
	    ArrayList<StudentTeacherInquiryBean> studentTeachers = new ArrayList<StudentTeacherInquiryBean>(entry.getValue()
		    .values());
	    Collections.sort(studentTeachers, new BeanComparator("shiftType"));
	    getTeachersInquiries().put(new AffiliatedTeacherDTO(entry.getKey()), studentTeachers);
	}
    }

    public String validateCurricularInquiry() {
	String validationResult = null;
	for (InquiryBlockDTO inquiryBlockDTO : getCurricularCourseBlocks()) {
	    validationResult = inquiryBlockDTO.validate();
	    if (!Boolean.valueOf(validationResult)) {
		return validationResult;
	    }
	}
	return Boolean.toString(true);
    }

    public Set getTeachers() {
	return getTeachersInquiries().entrySet();
    }

    public Set<InquiryBlockDTO> getCurricularCourseBlocks() {
	return curricularCourseBlocks;
    }

    public void setCurricularCourseBlocks(Set<InquiryBlockDTO> curricularCourseBlocks) {
	this.curricularCourseBlocks = curricularCourseBlocks;
    }

    public StudentInquiryRegistry getInquiryRegistry() {
	return inquiryRegistry;
    }

    public void setInquiryRegistry(StudentInquiryRegistry inquiryRegistry) {
	this.inquiryRegistry = inquiryRegistry;
    }

    public Map<TeacherDTO, List<? extends StudentTeacherInquiryBean>> getTeachersInquiries() {
	return teachersInquiries;
    }

    public void setTeachersInquiries(Map<TeacherDTO, List<? extends StudentTeacherInquiryBean>> teachersInquiries) {
	this.teachersInquiries = teachersInquiries;
    }

    public DateTime getStartedWhen() {
	return startedWhen;
    }

    public void setStartedWhen(DateTime startedWhen) {
	this.startedWhen = startedWhen;
    }

    @Service
    public void setAnsweredInquiry() {
	InquiryCourseAnswer inquiryCourseAnswer = new InquiryCourseAnswer();
	DateTime endTime = new DateTime();
	inquiryCourseAnswer.setAnswerDuration(endTime.minus(getStartedWhen().getMillis()).getMillis());
	inquiryCourseAnswer.setAttendenceClassesPercentage(getInquiryRegistry().getAttendenceClassesPercentage());
	inquiryCourseAnswer.setCommittedFraud(Boolean.FALSE);//TODO ver como ir buscar isto
	inquiryCourseAnswer.setEntryGrade(EntryGradesInterval.getInterval(getInquiryRegistry().getStudent().getEntryGrade()));
	inquiryCourseAnswer.setExecutionCourse(getInquiryRegistry().getExecutionCourse());
	inquiryCourseAnswer.setExecutionDegreeStudent(getInquiryRegistry().getStudent().getLastStudentCurricularPlan()
		.getDegreeCurricularPlan().getMostRecentExecutionDegree());
	ExecutionDegree executionDegreeCourse = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(getInquiryRegistry()
		.getCurricularCourse().getDegreeCurricularPlan(), getInquiryRegistry().getExecutionPeriod().getExecutionYear());
	inquiryCourseAnswer.setExecutionDegreeCourse(executionDegreeCourse);
	inquiryCourseAnswer.setExecutionPeriod(getInquiryRegistry().getExecutionPeriod());

	inquiryCourseAnswer.setGrade(getGrade(getInquiryRegistry()));
	inquiryCourseAnswer.setNumberOfEnrolments(getNumberOfEnrolments());
	inquiryCourseAnswer.setResponseDateTime(endTime);
	inquiryCourseAnswer.setStudentType(getInquiryRegistry().getStudent().getRegistrationAgreement());
	inquiryCourseAnswer.setStudyDaysSpentInExamsSeason(getInquiryRegistry().getStudyDaysSpentInExamsSeason());
	//	final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod = getInquiryRegistry().getStudent().getStudent()
	//		.getStudentInquiryExecutionPeriod(getInquiryRegistry().getExecutionPeriod());
	//	inquiryCourseAnswer
	//		.setWeeklyHoursSpentInClassesSeason(studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason());
	inquiryCourseAnswer.setWeeklyHoursSpentPercentage(getInquiryRegistry().getWeeklyHoursSpentPercentage());

	for (InquiryBlockDTO inquiryBlockDTO : getCurricularCourseBlocks()) {
	    for (InquiryGroupQuestionBean groupQuestionBean : inquiryBlockDTO.getInquiryGroups()) {
		for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
		    QuestionAnswer questionAnswer = new QuestionAnswer();
		    questionAnswer.setInquiryAnswer(inquiryCourseAnswer);
		    questionAnswer.setInquiryQuestion(questionDTO.getInquiryQuestion());
		    questionAnswer.setAnswer(questionDTO.getFinalValue());
		}
	    }
	}

	for (TeacherDTO teacherDTO : getTeachersInquiries().keySet()) {
	    for (StudentTeacherInquiryBean teacherInquiryBean : getTeachersInquiries().get(teacherDTO)) {
		if (teacherInquiryBean.isInquiryFilledIn()) {
		    InquiryTeacherAnswer inquiryTeacherAnswer = new InquiryTeacherAnswer();
		    if (teacherDTO.getTeacher() instanceof Person) {
			inquiryTeacherAnswer.setProfessorship(teacherInquiryBean.getExecutionCourse().getProfessorship(
				(Person) teacherDTO.getTeacher()));
		    } else {
			inquiryTeacherAnswer.setNonAffiliatedTeacher((NonAffiliatedTeacher) teacherDTO.getTeacher());
		    }
		    inquiryTeacherAnswer.setShiftType(teacherInquiryBean.getShiftType());
		    inquiryTeacherAnswer.setInquiryCourseAnswer(inquiryCourseAnswer);
		    for (InquiryBlockDTO inquiryBlockDTO : teacherInquiryBean.getTeacherInquiryBlocks()) {
			for (InquiryGroupQuestionBean groupQuestionBean : inquiryBlockDTO.getInquiryGroups()) {
			    for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
				QuestionAnswer questionAnswer = new QuestionAnswer();
				questionAnswer.setInquiryAnswer(inquiryTeacherAnswer);
				questionAnswer.setInquiryQuestion(questionDTO.getInquiryQuestion());
				questionAnswer.setAnswer(questionDTO.getFinalValue());
			    }
			}
		    }
		}
	    }
	}
	getInquiryRegistry().setState(InquiriesRegistryState.ANSWERED);
    }

    private int getNumberOfEnrolments() {
	final StudentCurricularPlan studentCurricularPlan = getInquiryRegistry().getStudent().getStudentCurricularPlan(
		getInquiryRegistry().getExecutionPeriod().getExecutionYear());
	return studentCurricularPlan.getEnrolments(getInquiryRegistry().getCurricularCourse()).size();
    }

    private Grade getGrade(StudentInquiryRegistry inquiryRegistry) {
	Collection<Enrolment> enrolments = inquiryRegistry.getStudent().getEnrolments(getInquiryRegistry().getExecutionPeriod());
	Grade grade = null;
	for (Enrolment enrolment : enrolments) {
	    if (getInquiryRegistry().getExecutionCourse() == enrolment.getExecutionCourseFor(getInquiryRegistry()
		    .getExecutionPeriod())) {
		final EnrolmentEvaluation enrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
		if (enrolmentEvaluation != null && (enrolmentEvaluation.isTemporary() || enrolmentEvaluation.isFinal())) {
		    grade = enrolmentEvaluation.getGrade();
		    break;
		}
	    }
	}
	return grade;
    }
}
