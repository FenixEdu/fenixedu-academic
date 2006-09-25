package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.IQuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.QuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.joda.time.DateTime;

public class ChangeStudentTestQuestionValue extends Service {
    public List<InfoSiteDistributedTestAdvisory> run(Integer executionCourseId, Integer distributedTestId, Double newValue, Integer questionId,
            Integer studentId, TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();

        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        Question question = distributedTest.findQuestionByOID(questionId);

        List<StudentTestQuestion> studentsTestQuestionList = new ArrayList<StudentTestQuestion>();
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
        	final Registration registration = rootDomainObject.readRegistrationByOID(studentId);
        	studentsTestQuestionList.add(StudentTestQuestion.findStudentTestQuestion(question, registration, distributedTest));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST_VARIATION) {
            studentsTestQuestionList.addAll(StudentTestQuestion.findStudentTestQuestions(question, distributedTest));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
        	final Registration registration = rootDomainObject.readRegistrationByOID(studentId);
        	final StudentTestQuestion studentTestQuestion = StudentTestQuestion.findStudentTestQuestion(question, registration, distributedTest);
            studentsTestQuestionList.addAll(distributedTest.findStudentTestQuestionsByTestQuestionOrder(studentTestQuestion.getTestQuestionOrder()));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS) {
            studentsTestQuestionList.addAll(question.getStudentTestsQuestionsSet());
        }
        for (StudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
            InfoSiteDistributedTestAdvisory infoSiteDistributedTestAdvisory = new InfoSiteDistributedTestAdvisory();
            infoSiteDistributedTestAdvisory.setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(studentTestQuestion.getDistributedTest()));
            infoSiteDistributedTestAdvisory.setInfoAdvisory(null);
            List<InfoStudent> group = new ArrayList<InfoStudent>();

            if (!group.contains(studentTestQuestion.getStudent().getPerson())) {
                group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));
            }

            if (studentTestQuestion.getResponse() != null
                    && studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                studentTestQuestion.setTestQuestionMark(getNewQuestionMark(studentTestQuestion, newValue, path.replace('\\', '/')));

                OnlineTest onlineTest = studentTestQuestion.getDistributedTest().getOnlineTest();
                ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
                Attends attend = studentTestQuestion.getStudent().readAttendByExecutionCourse(executionCourse);
                Mark mark = onlineTest.getMarkByAttend(attend);
                if (mark != null) {
                    mark.setMark(getNewStudentMark(studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent()));
                }
            }
            studentTestQuestion.setTestQuestionValue(newValue);

            infoSiteDistributedTestAdvisory.setInfoStudentList(group);
            infoSiteDistributedTestAdvisoryList.add(infoSiteDistributedTestAdvisory);
            StudentTestLog studentTestLog = new StudentTestLog();
            studentTestLog.setDistributedTest(studentTestQuestion.getDistributedTest());
            studentTestLog.setStudent(studentTestQuestion.getStudent());
            studentTestLog.setDateDateTime(new DateTime());
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
            studentTestLog.setEvent(MessageFormat.format(bundle
                    .getString("message.changeStudentValueLogMessage"), new Object[] { newValue }));
        }
        return infoSiteDistributedTestAdvisoryList;
    }

    private String getNewStudentMark(DistributedTest dt, Registration s) throws ExcepcaoPersistencia {
        double totalMark = 0;
        Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(s, dt);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        DecimalFormat df = new DecimalFormat("#0.##");
        DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        return (df.format(Math.max(0, totalMark)));
    }

    private Double getNewQuestionMark(StudentTestQuestion studentTestQuestion, Double newValue, String path) throws FenixServiceException {
        Double newMark = new Double(0);
        if (studentTestQuestion.getResponse() != null && !newValue.equals(Double.parseDouble("0"))) {
            if (studentTestQuestion.getTestQuestionValue().equals(Double.parseDouble("0"))) {
                ParseSubQuestion parse = new ParseSubQuestion();
                try {
                    studentTestQuestion = parse.parseStudentTestQuestion(studentTestQuestion, path);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory.getInstance();
                IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
                        .getQuestionCorrectionStrategy(studentTestQuestion);
                studentTestQuestion = questionCorrectionStrategy.getMark(studentTestQuestion);
                return studentTestQuestion.getTestQuestionMark();
            } else if (!studentTestQuestion.getTestQuestionMark().equals(Double.parseDouble("0"))) {
                newMark = (newValue * studentTestQuestion.getTestQuestionMark())
                        * (java.lang.Math.pow(studentTestQuestion.getTestQuestionValue(), -1));
            }

        }
        return newMark;
    }

}