/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentInquiryDTO implements Serializable {

    private DomainReference<InquiriesRegistry> inquiriesRegistry;

    Map<TeacherDTO, Collection<? extends TeacherInquiryDTO>> teachersInquiries = new TreeMap<TeacherDTO, Collection<? extends TeacherInquiryDTO>>(
	    new BeanComparator("name"));

    private InquiriesBlock firstPageFirstBlock;

    private InquiriesBlock firstPageSecondBlock;

    private InquiriesBlock firstPageThirdBlock;

    private InquiriesBlock firstPageFourthBlock;

    private InquiriesBlock firstPageFifthBlock;

    private InquiriesBlock secondPageFirstBlock;

    private InquiriesBlock secondPageSecondBlock;

    private InquiriesBlock secondPageThirdBlock;

    private StudentInquiryDTO(InquiriesRegistry inquiriesRegistry) {

	this.inquiriesRegistry = new DomainReference<InquiriesRegistry>(inquiriesRegistry);

	buildQuestionBlocks();

	final ExecutionCourse executionCourse = inquiriesRegistry.getExecutionCourse();
	final Set<ShiftType> shiftTypes = executionCourse.getShiftTypes();

	fillTeachersInquiriesWithAffiliatedTeachers(executionCourse, shiftTypes);
	fillTeachersInquiriesWithNonAffiliatedTeachers(executionCourse, shiftTypes);

    }

    private void fillTeachersInquiriesWithNonAffiliatedTeachers(final ExecutionCourse executionCourse,
	    final Set<ShiftType> shiftTypes) {
	for (final NonAffiliatedTeacher nonAffiliatedTeacher : executionCourse.getNonAffiliatedTeachers()) {
	    final NonAffiliatedTeacherDTO nonAffiliatedTeacherDTO = new NonAffiliatedTeacherDTO(nonAffiliatedTeacher);
	    Collection<TeacherInquiryDTO> nonAffiliatedTeachers = new ArrayList<TeacherInquiryDTO>();
	    for (final ShiftType shiftType : shiftTypes) {
		nonAffiliatedTeachers.add(new TeacherInquiryDTO(nonAffiliatedTeacherDTO, executionCourse, shiftType));
	    }
	    teachersInquiries.put(nonAffiliatedTeacherDTO, nonAffiliatedTeachers);
	}
    }

    private void fillTeachersInquiriesWithAffiliatedTeachers(final ExecutionCourse executionCourse,
	    final Set<ShiftType> shiftTypes) {
	Map<Teacher, Map<ShiftType, TeacherInquiryDTO>> teachersShifts = new HashMap<Teacher, Map<ShiftType, TeacherInquiryDTO>>();
	for (final Professorship professorship : executionCourse.getProfessorships()) {

	    final Teacher teacher = professorship.getTeacher();
	    if (!teachersShifts.containsKey(teacher)) {
		teachersShifts.put(teacher, new HashMap<ShiftType, TeacherInquiryDTO>());
	    }

	    final Map<ShiftType, TeacherInquiryDTO> teacherShift = teachersShifts.get(teacher);
	    final TeacherDTO teacherDTO = new AffiliatedTeacherDTO(teacher);

	    for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServices()) {
		for (ShiftType shiftType : degreeTeachingService.getShift().getTypes()) {
		    if (!teacherShift.containsKey(shiftType)) {
			teacherShift.put(shiftType, new TeacherInquiryDTO(teacherDTO, executionCourse, shiftType));
		    }
		}
	    }

	    if (teacherShift.isEmpty()) {
		for (final ShiftType shiftType : shiftTypes) {
		    teacherShift.put(shiftType, new TeacherInquiryDTO(teacherDTO, executionCourse, shiftType));
		}
	    }

	}

	for (Entry<Teacher, Map<ShiftType, TeacherInquiryDTO>> entry : teachersShifts.entrySet()) {
	    teachersInquiries.put(new AffiliatedTeacherDTO(entry.getKey()), new ArrayList<TeacherInquiryDTO>(entry.getValue()
		    .values()));
	}
    }

    public static StudentInquiryDTO makeNew(InquiriesRegistry inquiriesRegistry) {
	return new StudentInquiryDTO(inquiriesRegistry);
    }

    public InquiriesBlock getFirstBlock() {
	return firstPageFirstBlock;
    }

    public Map<TeacherDTO, Collection<? extends TeacherInquiryDTO>> getTeachersInquiries() {
	return teachersInquiries;
    }

    public InquiriesBlock getFirstPageFirstBlock() {
	return firstPageFirstBlock;
    }

    public InquiriesBlock getFirstPageSecondBlock() {
	return firstPageSecondBlock;
    }

    public InquiriesBlock getFirstPageThirdBlock() {
	return firstPageThirdBlock;
    }

    public InquiriesBlock getFirstPageFourthBlock() {
	return firstPageFourthBlock;
    }

    public InquiriesBlock getFirstPageFifthBlock() {
	return firstPageFifthBlock;
    }

    public InquiriesBlock getSecondPageFirstBlock() {
	return secondPageFirstBlock;
    }

    public InquiriesBlock getSecondPageSecondBlock() {
	return secondPageSecondBlock;
    }

    public InquiriesBlock getSecondPageThirdBlock() {
	return secondPageThirdBlock;
    }

    public InquiriesRegistry getInquiriesRegistry() {
	return inquiriesRegistry.getObject();
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
	final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();

	retrieveAnswersFromBlock(answers, firstPageFirstBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageThirdBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageFourthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageFifthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFirstBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageSecondBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageThirdBlock, fullLabels);

	return answers;
    }

    static void retrieveAnswersFromBlock(final Map<String, InquiriesQuestion> answers, InquiriesBlock inquiriesBlock,
	    boolean fullLabels) {
	for (final InquiriesQuestion inquiriesQuestion : inquiriesBlock.getQuestions()) {
	    if (fullLabels) {
		answers.put(inquiriesQuestion.getLabel(), inquiriesQuestion);
	    } else {
		final String label = inquiriesQuestion.getLabel();
		answers.put(label.substring(label.lastIndexOf('.') + 1), inquiriesQuestion);
	    }
	}
    }

    private void buildQuestionBlocks() {
	this.firstPageFirstBlock = new InquiriesBlock(StringUtils.EMPTY, false,
		"header.studentInquiries.firstPageFirstBlock.nonEvaluated",
		"header.studentInquiries.firstPageFirstBlock.flunked", "header.studentInquiries.firstPageFirstBlock.10_12",
		"header.studentInquiries.firstPageFirstBlock.13_14", "header.studentInquiries.firstPageFirstBlock.15_16",
		"header.studentInquiries.firstPageFirstBlock.17_18", "header.studentInquiries.firstPageFirstBlock.19_20");
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFirstBlock.classificationInThisCU", false, "nonEvaluated", "flunked", "10_12",
		"13_14", "15_16", "17_18", "19_20"));

	this.firstPageSecondBlock = new InquiriesBlock("title.studentInquiries.firstPageSecondBlock", false);
	this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonComplexProjects"));
	this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonExtenseProjects"));
	this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonManyProjects"));
	this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonLackOfPreviousPreparation"));
	this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonCurricularProgramExtension"));
	this.firstPageSecondBlock.addQuestion(new CheckBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonLackOfAttendanceOfLessons"));
	this.firstPageSecondBlock.addQuestion(new TextBoxQuestion(
		"label.studentInquiries.firstPageSecondBlock.highWorkLoadReasonOtherReasons", true));

	this.firstPageThirdBlock = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.studentInquiries.firstPageThirdBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.disagree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.agree", StringUtils.EMPTY,
		"header.studentInquiries.firstPageThirdBlock.totallyAgree");
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageThirdBlock.previousKnowledgeEnoughToCUAttendance", 1, 9, true));

	this.firstPageFourthBlock = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.studentInquiries.firstPageFourthBlock.passive",
		"header.studentInquiries.firstPageFourthBlock.activeWhenRequired",
		"header.studentInquiries.firstPageFourthBlock.activeByOwnWill");
	this.firstPageFourthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFourthBlock.activityParticipation", 1, 3, true));

	this.firstPageFifthBlock = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.studentInquiries.firstPageFifthBlock.unknown",
		"header.studentInquiries.firstPageFifthBlock.didNotContribute",
		"header.studentInquiries.firstPageFifthBlock.didContribute",
		"header.studentInquiries.firstPageFifthBlock.contributedAlot");
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.knowledgeAndComprehensionOfCU", 0, 3, true));
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.knowledgeApplicationOfCU", 0, 3, true));
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.criticSenseAndReflexiveSpirit", 0, 3, true));
	this.firstPageFifthBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.firstPageFifthBlock.cooperationAndComunicationCapacity", 0, 3, true));

	this.secondPageFirstBlock = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.studentInquiries.secondPageFirstBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.disagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.agree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.totallyAgree");
	this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageFirstBlock.predictedProgramTeached", 1, 9, true));
	this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageFirstBlock.wellStructuredOfCU", 1, 9, true)
		.setToolTip("tooltip.studentInquiries.secondPageFirstBlock.wellStructuredOfCU"));
	this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageFirstBlock.goodGuidanceMaterial", 1, 9, true));
	this.secondPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageFirstBlock.recomendendBibliographyImportance", 1, 9, true)
		.setToolTip("tooltip.studentInquiries.secondPageFirstBlock.recomendendBibliographyImportance"));

	this.secondPageSecondBlock = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.studentInquiries.secondPageFirstBlock.totallyDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.disagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.neitherAgreeOrDisagree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.agree", StringUtils.EMPTY,
		"header.studentInquiries.secondPageFirstBlock.totallyAgree");
	this.secondPageSecondBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageSecondBlock.fairEvaluationMethods", 1, 9, true));

	this.secondPageThirdBlock = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.studentInquiries.secondPageThirdBlock.veryBad", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.bad", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.neitherGoodOrBad", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.good", StringUtils.EMPTY,
		"header.studentInquiries.secondPageThirdBlock.veryGood");
	this.secondPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.studentInquiries.secondPageThirdBlock.globalClassificationOfCU", 1, 9, true));
    }

}
