package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
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
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ChangeStudentTestQuestionMark {
    protected void run(String executionCourseId, String distributedTestId, Double newMark, String questionId, String studentId,
            TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException {
        path = path.replace('\\', '/');

        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        Question question = distributedTest.findQuestionByOID(questionId);

        List<StudentTestQuestion> studentsTestQuestionList = new ArrayList<StudentTestQuestion>();
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
            final Registration registration = FenixFramework.getDomainObject(studentId);
            studentsTestQuestionList.add(StudentTestQuestion.findStudentTestQuestion(question, registration, distributedTest));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST_VARIATION) {
            studentsTestQuestionList.addAll(StudentTestQuestion.findStudentTestQuestions(question, distributedTest));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
            final Registration registration = FenixFramework.getDomainObject(studentId);
            final StudentTestQuestion studentTestQuestion =
                    StudentTestQuestion.findStudentTestQuestion(question, registration, distributedTest);
            studentsTestQuestionList.addAll(distributedTest.findStudentTestQuestionsByTestQuestionOrder(studentTestQuestion
                    .getTestQuestionOrder()));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS) {
            studentsTestQuestionList.addAll(question.getStudentTestsQuestionsSet());
        }
        for (StudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
            List<InfoStudent> group = new ArrayList<InfoStudent>();

            studentTestQuestion.setTestQuestionMark(newMark);
            if (!group.contains(studentTestQuestion.getStudent().getPerson())) {
                group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));
            }

            if (studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                if (studentTestQuestion.getResponse() == null) {
                    Response response = null;
                    try {
                        ParseSubQuestion parse = new ParseSubQuestion();
                        studentTestQuestion = parse.parseStudentTestQuestion(studentTestQuestion, path);
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }

                    if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                        response = new ResponseSTR("");
                    } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
                        response = new ResponseNUM("");
                    } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
                        response = new ResponseLID(new String[] { null });
                    }
                    response.setResponsed();
                    studentTestQuestion.setResponse(response);
                }
                OnlineTest onlineTest = studentTestQuestion.getDistributedTest().getOnlineTest();
                ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
                Attends attend = studentTestQuestion.getStudent().readAttendByExecutionCourse(executionCourse);
                Mark mark = onlineTest.getMarkByAttend(attend);
                final String markValue =
                        getNewStudentMark(studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent());
                if (mark == null) {
                    mark = new Mark(attend, onlineTest, markValue);
                } else {
                    mark.setMark(markValue);
                }
                if (mark.getPublishedMark() != null) {
                    mark.setPublishedMark(markValue);
                }
            }
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
            String event =
                    MessageFormat.format(bundle.getString("message.changeStudentMarkLogMessage"), new Object[] { newMark });

            new StudentTestLog(studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent(), event);
        }
    }

    private String getNewStudentMark(DistributedTest dt, Registration s) {
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

    // Service Invokers migrated from Berserk

    private static final ChangeStudentTestQuestionMark serviceInstance = new ChangeStudentTestQuestionMark();

    @Atomic
    public static void runChangeStudentTestQuestionMark(String executionCourseId, String distributedTestId, Double newMark,
            String questionId, String studentId, TestQuestionStudentsChangesType studentsType, String path)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, distributedTestId, newMark, questionId, studentId, studentsType, path);
    }

}