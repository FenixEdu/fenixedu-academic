/*
 * Created on Oct 20, 2003
 */

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

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestionMark extends Service {
    public List<InfoSiteDistributedTestAdvisory> run(Integer executionCourseId, Integer distributedTestId, Double newMark, Integer questionId,
            Integer studentId, TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();
        path = path.replace('\\', '/');
        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSupport.getIPersistentStudentTestQuestion();
        List<StudentTestQuestion> studentsTestQuestionList = new ArrayList<StudentTestQuestion>();
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
            studentsTestQuestionList.add(persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(questionId, studentId,
                    distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST_VARIATION) {
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByQuestionAndDistributedTest(questionId, distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
            StudentTestQuestion studentTestQuestion = persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(questionId,
                    studentId, distributedTestId);
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByOrderAndDistributedTest(studentTestQuestion.getTestQuestionOrder(),
                    distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS) {
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByQuestion(questionId));
        }
        for (StudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
            InfoSiteDistributedTestAdvisory infoSiteDistributedTestAdvisory = new InfoSiteDistributedTestAdvisory();
            infoSiteDistributedTestAdvisory.setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(studentTestQuestion.getDistributedTest()));
            infoSiteDistributedTestAdvisory.setInfoAdvisory(null);
            List<InfoStudent> group = new ArrayList<InfoStudent>();

            studentTestQuestion.setTestQuestionMark(newMark);
            if (!group.contains(studentTestQuestion.getStudent().getPerson()))
                group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));

            if (studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                if (studentTestQuestion.getResponse() == null) {
                    Response response = null;
                    InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
                            .newInfoFromDomain(studentTestQuestion);
                    try {
                        ParseQuestion parse = new ParseQuestion();
                        infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, path);
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }

                    if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR) {
                        response = new ResponseSTR("");
                    } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM) {
                        response = new ResponseNUM("");
                    } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID) {
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
                    OnlineTest onlineTest = (OnlineTest) persistentSupport.getIPersistentOnlineTest().readByDistributedTest(
                            studentTestQuestion.getDistributedTest().getIdInternal());
                    ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                            ExecutionCourse.class, executionCourseId);
                    Attends attend = persistentSupport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
                            studentTestQuestion.getStudent().getIdInternal(), executionCourse.getIdInternal());
                    Mark mark = persistentSupport.getIPersistentMark().readBy(onlineTest, attend);
                    final String markValue = getNewStudentMark(persistentSupport, studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent());
                    if (mark == null) {
                        mark = DomainFactory.makeMark(attend, onlineTest, markValue);
                    } else {
                        mark.setMark(markValue);                        
                    }
                    if (mark.getPublishedMark() != null) {
                        mark.setPublishedMark(markValue);
                    }
                }
            }
            StudentTestLog studentTestLog = DomainFactory.makeStudentTestLog();
            studentTestLog.setDistributedTest(studentTestQuestion.getDistributedTest());
            studentTestLog.setStudent(studentTestQuestion.getStudent());
            studentTestLog.setDate(Calendar.getInstance().getTime());
            ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
            studentTestLog.setEvent(MessageFormat.format(bundle.getString("message.changeStudentMarkLogMessage"), new Object[] { newMark }));
            infoSiteDistributedTestAdvisory.setInfoStudentList(group);
            infoSiteDistributedTestAdvisoryList.add(infoSiteDistributedTestAdvisory);

        }
        return infoSiteDistributedTestAdvisoryList;
    }

    private String getNewStudentMark(ISuportePersistente persistentSupport, DistributedTest dt, Student s) throws ExcepcaoPersistencia {
        double totalMark = 0;
        List<StudentTestQuestion> studentTestQuestionList = persistentSupport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                s.getIdInternal(), dt.getIdInternal());
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        DecimalFormat df =new DecimalFormat("#0.##");
        DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        return (df.format(Math.max(0, totalMark)));
    }
}