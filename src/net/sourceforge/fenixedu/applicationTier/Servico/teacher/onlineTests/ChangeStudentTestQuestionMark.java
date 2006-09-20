package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

public class ChangeStudentTestQuestionMark extends Service {
    public List<InfoSiteDistributedTestAdvisory> run(Integer executionCourseId,
            Integer distributedTestId, Double newMark, Integer questionId, Integer studentId,
            TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException,
            ExcepcaoPersistencia {
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();
        path = path.replace('\\', '/');
        
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
            infoSiteDistributedTestAdvisory.setInfoDistributedTest(InfoDistributedTest
                    .newInfoFromDomain(studentTestQuestion.getDistributedTest()));
            infoSiteDistributedTestAdvisory.setInfoAdvisory(null);
            List<InfoStudent> group = new ArrayList<InfoStudent>();

            studentTestQuestion.setTestQuestionMark(newMark);
            if (!group.contains(studentTestQuestion.getStudent().getPerson()))
                group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));

            if (studentTestQuestion.getDistributedTest().getTestType().equals(
                    new TestType(TestType.EVALUATION))) {
                if (studentTestQuestion.getResponse() == null) {
                    Response response = null;
                    InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
                            .newInfoFromDomain(studentTestQuestion);
                    try {
                        ParseQuestion parse = new ParseQuestion();
                        infoStudentTestQuestion = parse.parseStudentTestQuestion(
                                infoStudentTestQuestion, path);
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }

                    if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR) {
                        response = new ResponseSTR("");
                    } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType()
                            .intValue() == QuestionType.NUM) {
                        response = new ResponseNUM("");
                    } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType()
                            .intValue() == QuestionType.LID) {
                        response = new ResponseLID(new String[] { null });
                    }
                    response.setResponsed();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    XMLEncoder encoder = new XMLEncoder(out);
                    encoder.writeObject(response);
                    encoder.close();
                    final String outString = out.toString();
                    studentTestQuestion.setResponse(outString);
                } else {
                    OnlineTest onlineTest = studentTestQuestion.getDistributedTest().getOnlineTest();
                    ExecutionCourse executionCourse = rootDomainObject
                            .readExecutionCourseByOID(executionCourseId);
                    Attends attend = studentTestQuestion.getStudent().readAttendByExecutionCourse(
                            executionCourse);
                    Mark mark = onlineTest.getMarkByAttend(attend);
                    final String markValue = getNewStudentMark(studentTestQuestion.getDistributedTest(),
                            studentTestQuestion.getStudent());
                    if (mark == null) {
                        mark = new Mark(attend, onlineTest, markValue);
                    } else {
                        mark.setMark(markValue);
                    }
                    if (mark.getPublishedMark() != null) {
                        mark.setPublishedMark(markValue);
                    }
                }
            }
            StudentTestLog studentTestLog = new StudentTestLog();
            studentTestLog.setDistributedTest(studentTestQuestion.getDistributedTest());
            studentTestLog.setStudent(studentTestQuestion.getStudent());
            studentTestLog.setDate(Calendar.getInstance().getTime());
            ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
            studentTestLog.setEvent(MessageFormat.format(bundle
                    .getString("message.changeStudentMarkLogMessage"), new Object[] { newMark }));
            infoSiteDistributedTestAdvisory.setInfoStudentList(group);
            infoSiteDistributedTestAdvisoryList.add(infoSiteDistributedTestAdvisory);

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

}