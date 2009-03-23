/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Professorship;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TeachingInquiryDTO implements Serializable {

    private DomainReference<Professorship> professorship;

    private InquiriesBlock firstPageFirstBlock;

    private InquiriesBlock firstPageSecondBlockFirstPart;

    private InquiriesBlock firstPageSecondBlockSecondPart;

    private InquiriesBlock firstPageSecondBlockThirdPart;

    private InquiriesBlock firstPageSecondBlockFourthPart;

    private InquiriesBlock firstPageThirdBlock;

    private InquiriesBlock firstPageFourthBlock;

    private InquiriesBlock secondPageFourthBlock;

    private InquiriesBlock secondPageFifthBlockFirstPart;

    private InquiriesBlock secondPageFifthBlockSecondPart;

    private InquiriesBlock secondPageSixthBlock;

    private InquiriesBlock secondPageSeventhBlock;

    private InquiriesBlock secondPageEighthBlock;

    private InquiriesBlock thirdPageNinthBlock;

    private InquiriesBlock thirdPageReportDisclosureBlock;

    private InquiriesBlock secondPageFourthBlockThirdPart;

    private DateTime startDateTime;

    public TeachingInquiryDTO(Professorship professorship) {
	setProfessorship(professorship);
	this.startDateTime = new DateTime();
	buildQuestionBlocks();
    }

    public long getAnswerDuration() {
	return this.startDateTime == null ? 0 : new DateTime().getMillis() - this.startDateTime.getMillis();
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
	final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();
	retrieveAnswersFromBlock(answers, firstPageFirstBlock, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlockFirstPart, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlockSecondPart, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageSecondBlockFourthPart, fullLabels);
	retrieveAnswersFromBlock(answers, firstPageThirdBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFourthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFifthBlockFirstPart, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFifthBlockSecondPart, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageSixthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageSeventhBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageEighthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, thirdPageNinthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondPageFourthBlockThirdPart, fullLabels);
	retrieveAnswersFromBlock(answers, thirdPageReportDisclosureBlock, fullLabels);
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

	this.firstPageFirstBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.totallyDisagree",
		"header.teachingInquiries.two", "header.teachingInquiries.disagree", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherAgreeOrDisagree", "header.teachingInquiries.six",
		"header.teachingInquiries.agree", "header.teachingInquiries.eight", "header.teachingInquiries.totallyAgree");
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.loadAndClassTypeContributionToFullfilmentOfCUProgram", 1, 9, false)
		.setShowRequiredMark(false));
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.teacherNumberSuitableForCUOperation", 1, 9, false).setShowRequiredMark(false));
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.establishedScheduleSuitable", 1, 9,
		false).setShowRequiredMark(false));
	this.firstPageFirstBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.establishedScheduleNotSuitableReason",
		false).setRequired(false));
	this.firstPageFirstBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.availableInfrastructureSuitable", 1,
		9, false).setShowRequiredMark(false));
	this.firstPageFirstBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.availableInfrastructureSuitableReason",
		false).setRequired(false));

	this.firstPageSecondBlockFirstPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.totallyDisagree", "header.teachingInquiries.two", "header.teachingInquiries.disagree",
		"header.teachingInquiries.four", "header.teachingInquiries.neitherAgreeOrDisagree",
		"header.teachingInquiries.six", "header.teachingInquiries.agree", "header.teachingInquiries.eight",
		"header.teachingInquiries.totallyAgree");
	this.firstPageSecondBlockFirstPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.studentsReadyForFollowingCU", 1, 9, false).setShowRequiredMark(false));
	this.firstPageSecondBlockFirstPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.evaluationMethodSuitableForCUTeachingTypeAndObjective", 1, 9, false)
		.setShowRequiredMark(false));
	this.firstPageSecondBlockFirstPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.disturbingEventsInClasses", 1, 9, false).setShowRequiredMark(false));
	QuestionHeader disturbingEventsInClassesHeader = new QuestionHeader(
		"label.teachingInquiries.disturbingEventsInClassesReasons");
	this.firstPageSecondBlockFirstPart.addQuestion(new CheckBoxQuestion(
		"label.teachingInquiries.disturbingEventsInClassesReasonInfrastructure", disturbingEventsInClassesHeader)
		.setRequired(false).setToolTip("label.teachingInquiries.disturbingEventsInClassesReasonInfrastructure.tooltip"));
	this.firstPageSecondBlockFirstPart.addQuestion(new CheckBoxQuestion(
		"label.teachingInquiries.disturbingEventsInClassesReasonLowAssiduity").setRequired(false).setToolTip(
		"label.teachingInquiries.disturbingEventsInClassesReasonLowAssiduity.tooltip"));
	this.firstPageSecondBlockFirstPart.addQuestion(new CheckBoxQuestion(
		"label.teachingInquiries.disturbingEventsInClassesReasonIndiscipline").setRequired(false).setToolTip(
		"label.teachingInquiries.disturbingEventsInClassesReasonIndiscipline.tooltip"));
	this.firstPageSecondBlockFirstPart.addQuestion(new CheckBoxQuestion(
		"label.teachingInquiries.disturbingEventsInClassesReasonIntermediateEvaluations").setRequired(false)
		.setToolTip("label.teachingInquiries.disturbingEventsInClassesReasonIntermediateEvaluations.tooltip"));

	this.firstPageSecondBlockFirstPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.disturbingEventsInClassesDescription", false).setRequired(false));

	// this.firstPageSecondBlockSecondPart = new InquiriesBlock(
	// "title.teachingInquiries.semesterAverageStudentNumberInTheorical",
	// true, StringUtils.EMPTY);
	this.firstPageSecondBlockSecondPart = new InquiriesBlock("title.teachingInquiries.semesterAverageStudentNumber", true,
		"label.teachingInquiries.semesterStartAverageStudentNumber", StringUtils.EMPTY,
		"label.teachingInquiries.semesterMiddleAverageStudentNumber", StringUtils.EMPTY,
		"label.teachingInquiries.semesterEndAverageStudentNumber", StringUtils.EMPTY);

	QuestionHeader firstPageSecondBlockSecondPartSubHeader = new QuestionHeader(StringUtils.EMPTY,
		"header.teachingInquiries.semesterAverageStudentNumber",
		"header.teachingInquiries.activeAndInteressedStudentsRatio",
		"header.teachingInquiries.semesterAverageStudentNumber",
		"header.teachingInquiries.activeAndInteressedStudentsRatio",
		"header.teachingInquiries.semesterAverageStudentNumber",
		"header.teachingInquiries.activeAndInteressedStudentsRatio");

	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartAverageStudentNumberInTheorical", firstPageSecondBlockSecondPartSubHeader)
		.setShowRequiredMark(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartActiveAndInteressedStudentsRatioInTheorical", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleAverageStudentNumberInTheorical", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleActiveAndInteressedStudentsRatioInTheorica", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndAverageStudentNumberInTheorical", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndActiveAndInteressedStudentsRatioInTheorical", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));

	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartAverageStudentNumberInProblems", false).setShowRequiredMark(false)
		.setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartActiveAndInteressedStudentsRatioInProblems", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleAverageStudentNumberInProblems", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleActiveAndInteressedStudentsRatioInProblems", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndAverageStudentNumberInProblems", false).setShowRequiredMark(false).setNewRow(
		false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndActiveAndInteressedStudentsRatioInProblems", false)
		.setShowRequiredMark(false).setNewRow(false).setAutofit(false));

	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartAverageStudentNumberInLabs", false).setShowRequiredMark(false).setAutofit(
		false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartActiveAndInteressedStudentsRatioInLabs", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleAverageStudentNumberInLabs", false).setShowRequiredMark(false).setNewRow(
		false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleActiveAndInteressedStudentsRatioInLabs", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndAverageStudentNumberInLabs", false).setShowRequiredMark(false).setNewRow(
		false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndActiveAndInteressedStudentsRatioInLabs", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));

	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartAverageStudentNumberInSeminary", false).setShowRequiredMark(false)
		.setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartActiveAndInteressedStudentsRatioInSeminary", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleAverageStudentNumberInSeminary", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleActiveAndInteressedStudentsRatioInSeminary", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndAverageStudentNumberInSeminary", false).setShowRequiredMark(false).setNewRow(
		false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndActiveAndInteressedStudentsRatioInSeminary", false)
		.setShowRequiredMark(false).setNewRow(false).setAutofit(false));

	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartAverageStudentNumberInProject", false).setShowRequiredMark(false)
		.setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterStartActiveAndInteressedStudentsRatioInProject", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleAverageStudentNumberInProject", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterMiddleActiveAndInteressedStudentsRatioInProject", false).setShowRequiredMark(
		false).setNewRow(false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndAverageStudentNumberInProject", false).setShowRequiredMark(false).setNewRow(
		false).setAutofit(false));
	this.firstPageSecondBlockSecondPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.semesterEndActiveAndInteressedStudentsRatioInProject", false).setShowRequiredMark(false)
		.setNewRow(false).setAutofit(false));

	this.firstPageSecondBlockFourthPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.totallyDisagree", "header.teachingInquiries.two", "header.teachingInquiries.disagree",
		"header.teachingInquiries.four", "header.teachingInquiries.neitherAgreeOrDisagree",
		"header.teachingInquiries.six", "header.teachingInquiries.agree", "header.teachingInquiries.eight",
		"header.teachingInquiries.totallyAgree");
	this.firstPageSecondBlockFourthPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.clarificationOfDoubtsOutsideClasses", 1, 9, false).setShowRequiredMark(false));
	this.firstPageSecondBlockFourthPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.autonomousWork", 1, 9,
		false).setShowRequiredMark(false));
	this.firstPageSecondBlockFourthPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.studentsPerformance", 1,
		9, false, new QuestionHeader("title.teachingInquiries.studentsPerformance", "header.teachingInquiries.veryBad",
			"header.teachingInquiries.two", "header.teachingInquiries.bad", "header.teachingInquiries.four",
			"header.teachingInquiries.neitherGoodOrBad", "header.teachingInquiries.six",
			"header.teachingInquiries.good", "header.teachingInquiries.eight", "header.teachingInquiries.veryGood"))
		.setShowRequiredMark(false));

	this.firstPageThirdBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.veryBad",
		"header.teachingInquiries.two", "header.teachingInquiries.bad", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherGoodOrBad", "header.teachingInquiries.six", "header.teachingInquiries.good",
		"header.teachingInquiries.eight", "header.teachingInquiries.veryGood");
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.classesAndOtherActivitiesFrequency",
		1, 9, false).setShowRequiredMark(false));
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.classesAndOtherActivitiesPonctuality", 1, 9, false).setShowRequiredMark(false));
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.globalQualityOfTeachingInCU", 1, 9,
		false).setShowRequiredMark(false));
	this.firstPageThirdBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.pedagogicalActivitiesDeveloped", 1,
		9, false).setShowRequiredMark(false));
	this.firstPageThirdBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.relativePedagogicalInitiatives", true)
		.setRequired(false).setShowRequiredMark(false));
	this.firstPageThirdBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.generalCommentToCUOperation", true)
		.setShowRequiredMark(false));

	this.firstPageFourthBlock = new InquiriesBlock("title.teachingInquiries.reportDisclosureToAcademicComunity", true,
		"label.true", "label.false");
	final RadioGroupQuestion reportDisclosureToAcademicComunityQuestion = new RadioGroupQuestion(
		"label.teachingInquiries.reportDisclosureToAcademicComunity", false).addChoice(Boolean.toString(true),
		StringUtils.EMPTY).addChoice(Boolean.toString(false), StringUtils.EMPTY);
	reportDisclosureToAcademicComunityQuestion.setValue(Boolean.toString(true));
	this.firstPageFourthBlock.addQuestion(reportDisclosureToAcademicComunityQuestion.setShowRequiredMark(false));
	final RadioGroupQuestion resultsDisclosureToAcademicComunityQuestion = new RadioGroupQuestion(
		"label.teachingInquiries.resultsDisclosureToAcademicComunity", false).addChoice(Boolean.toString(true),
		StringUtils.EMPTY).addChoice(Boolean.toString(false), StringUtils.EMPTY);
	resultsDisclosureToAcademicComunityQuestion.setValue(Boolean.toString(true));
	this.firstPageFourthBlock.addQuestion(resultsDisclosureToAcademicComunityQuestion.setHeader(
		new QuestionHeader("title.teachingInquiries.resultsDisclosureToAcademicComunity", "label.true", "label.false"))
		.setShowRequiredMark(false).setToolTip("label.teachingInquiries.resultsDisclosureToAcademicComunity.tooltip"));

	this.secondPageFourthBlock = new InquiriesBlock("title.teachingInquiries.cuEvaluationMethod.writtenProofs", true,
		StringUtils.EMPTY);
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfExams", false).setInteger(
		true).setToolTip("tooltip.teachingInquiries.numberOfExams").setShowRequiredMark(false));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfTests", false).setInteger(
		true).setToolTip("tooltip.teachingInquiries.numberOfTests").setShowRequiredMark(false));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfQuizzesAndMiniTests", false)
		.setInteger(true).setToolTip("tooltip.teachingInquiries.numberOfQuizzesAndMiniTests").setShowRequiredMark(false));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfElectronicQuizzes", false)
		.setInteger(true).setToolTip("tooltip.teachingInquiries.numberOfElectronicQuizzes").setShowRequiredMark(false));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.numberOfStudyVisitsOrOtherActivitiesReports", false).setInteger(true).setHeader(
		new QuestionHeader("title.teachingInquiries.cuEvaluationMethod.worksOrProjects", StringUtils.EMPTY)));
	this.secondPageFourthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.numberOfWorksOrProjects", false)
		.setInteger(true).setToolTip("tooltip.teachingInquiries.numberOfWorksOrProjects").setShowRequiredMark(false));

	this.secondPageFourthBlockThirdPart = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.language.pt",
		"header.teachingInquiries.language.en", "header.teachingInquiries.language.both",
		"header.teachingInquiries.language.others");
	this.secondPageFourthBlockThirdPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.teachingLanguage", false)
		.addChoice("PT", StringUtils.EMPTY).addChoice("EN", StringUtils.EMPTY).addChoice("BOTH", StringUtils.EMPTY)
		.addChoice("OTHER", StringUtils.EMPTY).setShowRequiredMark(false));
	this.secondPageFourthBlockThirdPart.addQuestion(new TextBoxQuestion("label.teachingInquiries.otherTeachingLanguage",
		false).setRequired(false).setShowRequiredMark(false));

	this.secondPageFifthBlockFirstPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.belowExpected", "header.teachingInquiries.expected",
		"header.teachingInquiries.aboveExpected");
	this.secondPageFifthBlockFirstPart.addQuestion(new RadioGroupQuestion("label.teachingInquiries.workLoadClassification",
		1, 3, false).setShowRequiredMark(false));
	this.secondPageFifthBlockFirstPart.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.workLoadClassificationReasons", false).setRequired(false).setShowRequiredMark(false));

	this.secondPageFifthBlockSecondPart = new InquiriesBlock(StringUtils.EMPTY, true,
		"header.teachingInquiries.totallyDisagree", "header.teachingInquiries.two", "header.teachingInquiries.disagree",
		"header.teachingInquiries.four", "header.teachingInquiries.neitherAgreeOrDisagree",
		"header.teachingInquiries.six", "header.teachingInquiries.agree", "header.teachingInquiries.eight",
		"header.teachingInquiries.totallyAgree");
	this.secondPageFifthBlockSecondPart.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.positionOfCUInStudentCurricularPlan", 1, 9, false).setShowRequiredMark(false));

	this.secondPageSixthBlock = new InquiriesBlock(
		"label.teachingInquiries.cuStudentsCompetenceAcquisitionAndDevelopmentLevel", true,
		"header.teachingInquiries.notAppliableWithoutValue", "header.teachingInquiries.totallyDisagree",
		"header.teachingInquiries.two", "header.teachingInquiries.disagree", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherAgreeOrDisagree", "header.teachingInquiries.six",
		"header.teachingInquiries.agree", "header.teachingInquiries.eight", "header.teachingInquiries.totallyAgree");
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.comprehensionAndKnowledgeOfCU", 0,
		9, false).setShowRequiredMark(false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.comprehensionApplicationOfCU", 0,
		9, false).setShowRequiredMark(false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.criticalSenseAndReflexiveSpirit",
		0, 9, false).setShowRequiredMark(false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.cooperationAndCommunicationCapacity", 0, 9, false).setShowRequiredMark(false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.increaseAutonoumousLearningCapacity", 0, 9, false).setShowRequiredMark(false));
	this.secondPageSixthBlock.addQuestion(new RadioGroupQuestion(
		"label.teachingInquiries.socialAndProfessionalContextAnalysis", 0, 9, false).setShowRequiredMark(false));

	this.secondPageSeventhBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.teachingInquiries.veryBad",
		"header.teachingInquiries.two", "header.teachingInquiries.bad", "header.teachingInquiries.four",
		"header.teachingInquiries.neitherGoodOrBad", "header.teachingInquiries.six", "header.teachingInquiries.good",
		"header.teachingInquiries.eight", "header.teachingInquiries.veryGood");
	this.secondPageSeventhBlock.addQuestion(new RadioGroupQuestion("label.teachingInquiries.globalClassificationOfThisCU", 1,
		9, false).setShowRequiredMark(false));

	this.secondPageEighthBlock = new InquiriesBlock(true);
	this.secondPageEighthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.strongPointsOfCUTeachingProcess",
		true).setShowRequiredMark(false));
	this.secondPageEighthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.weakPointsOfCUTeachingProcess", true)
		.setShowRequiredMark(false));
	this.secondPageEighthBlock.addQuestion(new TextBoxQuestion("label.teachingInquiries.finalCommentsAndImproovements", true)
		.setRequired(false).setShowRequiredMark(false));

	this.thirdPageNinthBlock = new InquiriesBlock(true);
	this.thirdPageNinthBlock.addQuestion(new TextBoxQuestion(
		"label.teachingInquiries.negativeResultsResolutionAndImproovementPlanOfAction", true).setShowRequiredMark(false));

	this.thirdPageReportDisclosureBlock = new InquiriesBlock(
		"title.teachingInquiries.responsibleTeacherReportDisclosureToAcademicComunity", true, "label.true", "label.false");
	final RadioGroupQuestion responsibleTeacherReportDisclosureToAcademicComunityQuestion = new RadioGroupQuestion(
		"label.teachingInquiries.responsibleTeacherReportDisclosureToAcademicComunity", false).addChoice(
		Boolean.toString(true), StringUtils.EMPTY).addChoice(Boolean.toString(false), StringUtils.EMPTY);
	responsibleTeacherReportDisclosureToAcademicComunityQuestion.setValue(Boolean.toString(true));
	this.thirdPageReportDisclosureBlock.addQuestion(responsibleTeacherReportDisclosureToAcademicComunityQuestion
		.setShowRequiredMark(false));

    }

    public Professorship getProfessorship() {
	return professorship == null ? null : professorship.getObject();
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = new DomainReference<Professorship>(professorship);
    }

    public InquiriesBlock getFirstPageFirstBlock() {
	return firstPageFirstBlock;
    }

    public void setFirstPageFirstBlock(InquiriesBlock firstPageFirstBlock) {
	this.firstPageFirstBlock = firstPageFirstBlock;
    }

    public InquiriesBlock getFirstPageSecondBlockFirstPart() {
	return firstPageSecondBlockFirstPart;
    }

    public void setFirstPageSecondBlockFirstPart(InquiriesBlock firstPageSecondBlockFirstPart) {
	this.firstPageSecondBlockFirstPart = firstPageSecondBlockFirstPart;
    }

    public InquiriesBlock getFirstPageSecondBlockSecondPart() {
	return firstPageSecondBlockSecondPart;
    }

    public void setFirstPageSecondBlockSecondPart(InquiriesBlock firstPageSecondBlockSecondPart) {
	this.firstPageSecondBlockSecondPart = firstPageSecondBlockSecondPart;
    }

    public InquiriesBlock getFirstPageSecondBlockThirdPart() {
	return firstPageSecondBlockThirdPart;
    }

    public void setFirstPageSecondBlockThirdPart(InquiriesBlock firstPageSecondBlockThirdPart) {
	this.firstPageSecondBlockThirdPart = firstPageSecondBlockThirdPart;
    }

    public InquiriesBlock getFirstPageSecondBlockFourthPart() {
	return firstPageSecondBlockFourthPart;
    }

    public void setFirstPageSecondBlockFourthPart(InquiriesBlock firstPageSecondBlockFourthPart) {
	this.firstPageSecondBlockFourthPart = firstPageSecondBlockFourthPart;
    }

    public InquiriesBlock getFirstPageThirdBlock() {
	return firstPageThirdBlock;
    }

    public void setFirstPageThirdBlock(InquiriesBlock firstPageThirdBlock) {
	this.firstPageThirdBlock = firstPageThirdBlock;
    }

    public InquiriesBlock getSecondPageFourthBlock() {
	return secondPageFourthBlock;
    }

    public void setSecondPageFourthBlock(InquiriesBlock secondPageFifthBlock) {
	this.secondPageFourthBlock = secondPageFifthBlock;
    }

    public InquiriesBlock getSecondPageFifthBlockFirstPart() {
	return secondPageFifthBlockFirstPart;
    }

    public void setSecondPageFifthBlockFirstPart(InquiriesBlock secondPageSixthBlockFirstPart) {
	this.secondPageFifthBlockFirstPart = secondPageSixthBlockFirstPart;
    }

    public InquiriesBlock getSecondPageFifthBlockSecondPart() {
	return secondPageFifthBlockSecondPart;
    }

    public void setSecondPageFifthBlockSecondPart(InquiriesBlock secondPageSixthBlockSecondPart) {
	this.secondPageFifthBlockSecondPart = secondPageSixthBlockSecondPart;
    }

    public InquiriesBlock getSecondPageSeventhBlock() {
	return secondPageSeventhBlock;
    }

    public void setSecondPageSeventhBlock(InquiriesBlock secondPageSeventhBlock) {
	this.secondPageSeventhBlock = secondPageSeventhBlock;
    }

    public InquiriesBlock getSecondPageEighthBlock() {
	return secondPageEighthBlock;
    }

    public void setSecondPageEighthBlock(InquiriesBlock secondPageEighthBlock) {
	this.secondPageEighthBlock = secondPageEighthBlock;
    }

    public InquiriesBlock getThirdPageNinthBlock() {
	return thirdPageNinthBlock;
    }

    public void setThirdPageNinthBlock(InquiriesBlock secondPageNinthBlock) {
	this.thirdPageNinthBlock = secondPageNinthBlock;
    }

    public InquiriesBlock getSecondPageSixthBlock() {
	return secondPageSixthBlock;
    }

    public void setSecondPageSixthBlock(InquiriesBlock secondPageSixthBlock) {
	this.secondPageSixthBlock = secondPageSixthBlock;
    }

    public InquiriesBlock getSecondPageFourthBlockThirdPart() {
	return secondPageFourthBlockThirdPart;
    }

    public void setSecondPageFourthBlockThirdPart(InquiriesBlock secondPageFourthBlockThirdPart) {
	this.secondPageFourthBlockThirdPart = secondPageFourthBlockThirdPart;
    }

    public InquiriesBlock getFirstPageFourthBlock() {
	return firstPageFourthBlock;
    }

    public void setFirstPageFourthBlock(InquiriesBlock firstPageFourthBlock) {
	this.firstPageFourthBlock = firstPageFourthBlock;
    }

    public InquiriesBlock getThirdPageReportDisclosureBlock() {
	return thirdPageReportDisclosureBlock;
    }

    public void setThirdPageReportDisclosureBlock(InquiriesBlock thirdPageReportDisclosureBlock) {
	this.thirdPageReportDisclosureBlock = thirdPageReportDisclosureBlock;
    }

}
