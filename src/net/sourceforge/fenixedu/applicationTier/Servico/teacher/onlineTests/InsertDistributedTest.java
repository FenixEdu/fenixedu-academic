package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import jvstm.TransactionalCommand;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.stm.Transaction;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.commons.beanutils.BeanComparator;

import com.sun.faces.el.impl.parser.ParseException;

public class InsertDistributedTest extends Service {

    public void run(Integer executionCourseId, Integer testId, String testInformation, String evaluationTitle,
	    Calendar beginDate, Calendar beginHour, Calendar endDate, Calendar endHour, TestType testType,
	    CorrectionAvailability correctionAvaiability, Boolean imsFeedback, List<InfoStudent> infoStudentList,
	    String contextPath) throws FenixServiceException, ExcepcaoPersistencia {
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	if (executionCourse == null)
	    throw new InvalidArgumentsServiceException();

	Test test = rootDomainObject.readTestByOID(testId);
	if (test == null)
	    throw new InvalidArgumentsServiceException();

	try {
	    final DistributedTestCreator distributedTestCreator = new DistributedTestCreator(executionCourse, test,
		    testInformation, evaluationTitle, beginDate, beginHour, endDate, endHour, testType, correctionAvaiability,
		    imsFeedback);
	    distributedTestCreator.start();
	    distributedTestCreator.join();

	    final String replacedContextPath = contextPath.replace('\\', '/');

	    final Integer distributedTestId = distributedTestCreator.distributedTestId;
	    if (distributedTestId == null) {
		throw new Error("Creator thread was unable to create a distributed test!");
	    }
	    Distributor.runThread(infoStudentList, distributedTestId, test.getIdInternal(), replacedContextPath);

	} catch (InterruptedException e) {
	    throw new Error(e);
	}

	return;
    }

    public static class DistributedTestCreator extends Thread implements TransactionalCommand {

	private final Integer executionCourseId;

	private final Integer testId;

	private final String testInformation;

	private final String evaluationTitle;

	private final Calendar beginDate;

	private final Calendar beginHour;

	private final Calendar endDate;

	private final Calendar endHour;

	private final TestType testType;

	private final CorrectionAvailability correctionAvaiability;

	private final Boolean imsFeedback;

	private Integer tempDistributedTestId = null;

	public Integer distributedTestId = null;

	public DistributedTestCreator(final ExecutionCourse executionCourse, final Test test, final String testInformation,
		final String evaluationTitle, final Calendar beginDate, final Calendar beginHour, final Calendar endDate,
		final Calendar endHour, final TestType testType, final CorrectionAvailability correctionAvaiability,
		final Boolean imsFeedback) {
	    this.executionCourseId = executionCourse.getIdInternal();
	    this.testId = test.getIdInternal();
	    this.testInformation = testInformation;
	    this.evaluationTitle = evaluationTitle;
	    this.beginDate = beginDate;
	    this.beginHour = beginHour;
	    this.endDate = endDate;
	    this.endHour = endHour;
	    this.testType = testType;
	    this.correctionAvaiability = correctionAvaiability;
	    this.imsFeedback = imsFeedback;
	}

	public void run() {
	    Transaction.withTransaction(this);
	    distributedTestId = tempDistributedTestId;
	}

	public void doIt() {
	    final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	    final Test test = rootDomainObject.readTestByOID(testId);

	    DistributedTest distributedTest = new DistributedTest();
	    distributedTest.setTitle(test.getTitle());
	    distributedTest.setTestInformation(testInformation);
	    distributedTest.setEvaluationTitle(evaluationTitle);
	    distributedTest.setBeginDate(beginDate);
	    distributedTest.setBeginHour(beginHour);
	    distributedTest.setEndDate(endDate);
	    distributedTest.setEndHour(endHour);
	    distributedTest.setTestType(testType);
	    distributedTest.setCorrectionAvailability(correctionAvaiability);
	    distributedTest.setImsFeedback(imsFeedback);
	    distributedTest.setNumberOfQuestions(test.getTestQuestionsCount());

	    TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class, executionCourseId);

	    if (testScope == null) {
		testScope = new TestScope(executionCourse);
	    }
	    distributedTest.setTestScope(testScope);

	    // Create Evaluation - OnlineTest and Marks
	    if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) {
		OnlineTest onlineTest = new OnlineTest();
		onlineTest.addAssociatedExecutionCourses(executionCourse);
		onlineTest.setDistributedTest(distributedTest);
	    }

	    tempDistributedTestId = distributedTest.getIdInternal();
	}

	protected static void runThread(final ExecutionCourse executionCourse, final Test test, final String testInformation,
		final String evaluationTitle, final Calendar beginDate, final Calendar beginHour, final Calendar endDate,
		final Calendar endHour, final TestType testType, final CorrectionAvailability correctionAvaiability,
		final Boolean imsFeedback) {
	    final DistributedTestCreator distributedTestCreator = new DistributedTestCreator(executionCourse, test,
		    testInformation, evaluationTitle, beginDate, beginHour, endDate, endHour, testType, correctionAvaiability,
		    imsFeedback);
	    distributedTestCreator.start();
	}
    }

    private static class QuestionPair {
	private final Integer testQuestionId;

	private final Integer questionId;

	public QuestionPair(final TestQuestion testQuestion, final Question question) {
	    testQuestionId = testQuestion.getIdInternal();
	    questionId = question.getIdInternal();
	}

	public TestQuestion getTestQuestion() {
	    return rootDomainObject.readTestQuestionByOID(testQuestionId);
	}

	public Question getQuestion() {
	    return rootDomainObject.readQuestionByOID(questionId);
	}
    }

    private static class Distribution {

	private Map<InfoStudent, Collection<QuestionPair>> questionMap = new HashMap<InfoStudent, Collection<QuestionPair>>();

	public Distribution(final List<TestQuestion> testQuestionList, final List<InfoStudent> infoStudentList) {
	    final int numberOfStudents = infoStudentList.size();

	    final Random r = new Random();

	    for (final TestQuestion testQuestion : testQuestionList) {
		final List<Question> questions = getQuestions(testQuestion, numberOfStudents);
		if (questions.size() >= numberOfStudents) {
		    for (final InfoStudent infoStudent : infoStudentList) {
			Collection<QuestionPair> questionsForStudent = questionMap.get(infoStudent);
			if (questionsForStudent == null) {
			    questionsForStudent = new ArrayList<QuestionPair>();
			    questionMap.put(infoStudent, questionsForStudent);
			}

			int questionIndex = r.nextInt(questions.size());
			final Question question = questions.get(questionIndex);
			questionsForStudent.add(new QuestionPair(testQuestion, question));
			questions.remove(questionIndex);
		    }
		}
	    }
	}

	private List<Question> getQuestions(final TestQuestion testQuestion, final int numberOfStudents) {
	    final List<Question> questions = new ArrayList<Question>();
	    final Metadata metadata = testQuestion.getQuestion().getMetadata();
	    while (metadata.getQuestionsCount() > 0 && questions.size() < numberOfStudents) {
		for (final Question question : metadata.getQuestionsSet()) {
		    if (question.getVisibility()) {
			questions.add(question);
		    }
		}
	    }
	    return questions;
	}
    }

    public static class Distributor extends Thread implements TransactionalCommand {

	private final List<InfoStudent> infoStudentList;

	private final Integer distributedTestId;

	private final Integer testId;

	private final String replacedContextPath;

	public Distributor(final List<InfoStudent> infoStudentList, final Integer distributedTestId, final Integer testId,
		final String replacedContextPath) {
	    this.infoStudentList = infoStudentList;
	    this.distributedTestId = distributedTestId;
	    this.testId = testId;
	    this.replacedContextPath = replacedContextPath;
	}

	public void run() {
	    Transaction.withTransaction(this);
	}

	public void doIt() {
	    Test test = rootDomainObject.readTestByOID(testId);

	    List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
	    Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));

	    final Distribution distribution = new Distribution(testQuestionList, infoStudentList);
	    for (final Entry<InfoStudent, Collection<QuestionPair>> entry : distribution.questionMap.entrySet()) {
		final InfoStudent infoStudent = entry.getKey();
		final Collection<QuestionPair> questions = entry.getValue();
		try {
		    DistributeForStudentThread.runThread(distributedTestId, replacedContextPath, infoStudent, questions);
		} catch (InterruptedException e) {
		}
	    }
	}

	protected static void runThread(final List<InfoStudent> infoStudentList, final Integer distributedTestId,
		final Integer testId, final String replacedContextPath) throws InterruptedException {
	    final Distributor distributor = new Distributor(infoStudentList, distributedTestId, testId, replacedContextPath);
	    distributor.start();
	    distributor.join();
	}
    }

    private static void addAllQuestions(final List<Integer> questionList, final List<Question> visibleQuestions) {
	for (final Question question : visibleQuestions) {
	    questionList.add(question.getIdInternal());
	}
    }

    public static class DistributeForStudentThread extends Thread implements TransactionalCommand {

	private final Integer distributedTestId;

	private final String replacedContextPath;

	private final InfoStudent infoStudent;

	private final Collection<QuestionPair> questionList;

	public DistributeForStudentThread(final Integer distributedTestId, final String replacedContextPath,
		final InfoStudent infoStudent, final Collection<QuestionPair> questionList) {
	    this.distributedTestId = distributedTestId;
	    this.replacedContextPath = replacedContextPath;
	    this.infoStudent = infoStudent;
	    this.questionList = questionList;
	}

	public void run() {
	    Transaction.withTransaction(this);
	}

	public void doIt() {
	    final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
	    final Registration registration = rootDomainObject.readRegistrationByOID(infoStudent.getIdInternal());

	    for (final QuestionPair questionPair : questionList) {
		final TestQuestion testQuestion = questionPair.getTestQuestion();

		final StudentTestQuestion studentTestQuestion = new StudentTestQuestion();
		studentTestQuestion.setStudent(registration);
		studentTestQuestion.setDistributedTest(distributedTest);
		studentTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
		studentTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
		studentTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
		studentTestQuestion.setTestQuestionMark(Double.valueOf(0));
		studentTestQuestion.setResponse(null);

		Question question = null;
		try {
		    question = getStudentQuestion(questionPair.getQuestion(), replacedContextPath);
		} catch (ParseException e) {
		    throw new Error(e);
		} catch (ParseQuestionException e) {
		    throw new Error(e);
		}
		if (question == null) {
		    throw new Error();
		}
		if (question.getSubQuestions().size() >= 1 && question.getSubQuestions().get(0).getItemId() != null) {
		    studentTestQuestion.setItemId(question.getSubQuestions().get(0).getItemId());
		}
		studentTestQuestion.setQuestion(question);
		questionList.remove(question);
	    }
	}

	private Question getStudentQuestion(final Question question, String path) throws ParseException, ParseQuestionException {
	    return question.getSubQuestions() == null || question.getSubQuestions().size() == 0 ? new ParseSubQuestion()
		    .parseSubQuestion(question, path) : question;
	}

	protected static void runThread(final Integer distributedTestId, final String replacedContextPath,
		final InfoStudent infoStudent, final Collection<QuestionPair> questionList) throws InterruptedException {
	    final DistributeForStudentThread distributeForStudentThread = new DistributeForStudentThread(distributedTestId,
		    replacedContextPath, infoStudent, questionList);
	    distributeForStudentThread.start();
	    //TODO
	    distributeForStudentThread.join();
	}
    }

}