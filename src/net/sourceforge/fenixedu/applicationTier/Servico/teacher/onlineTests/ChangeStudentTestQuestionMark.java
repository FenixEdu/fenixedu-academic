/*
 * Created on Oct 20, 2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestionMark implements IService {
    public List<InfoSiteDistributedTestAdvisory> run(Integer executionCourseId, Integer distributedTestId, Double newMark, Integer questionId,
            Integer studentId, TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        path = path.replace('\\', '/');
        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();
        List<IStudentTestQuestion> studentsTestQuestionList = new ArrayList<IStudentTestQuestion>();
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
            studentsTestQuestionList.add(persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(questionId, studentId,
                    distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST_VARIATION) {
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByQuestionAndDistributedTest(questionId, distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
            IStudentTestQuestion studentTestQuestion = persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(questionId,
                    studentId, distributedTestId);
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByOrderAndDistributedTest(studentTestQuestion.getTestQuestionOrder(),
                    distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS) {
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByQuestion(questionId));
        }
        for (IStudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
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
                        response = new ResponseSTR();
                        ((ResponseSTR) response).setResponse("");
                    } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM) {
                        response = new ResponseNUM();
                        ((ResponseNUM) response).setResponse("");
                    } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID) {
                        response = new ResponseLID();
                        ((ResponseLID) response).setResponse(new String[1]);
                    }
                    response.setResponsed();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    XMLEncoder encoder = new XMLEncoder(out);
                    encoder.writeObject(response);
                    encoder.close();
                    final String outString = out.toString();
                    studentTestQuestion.setResponse(outString);
                } else {
                    IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(
                            studentTestQuestion.getDistributedTest().getIdInternal());
                    IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class, executionCourseId);
                    IAttends attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(studentTestQuestion.getStudent(),
                            executionCourse);
                    IMark mark = persistentSuport.getIPersistentMark().readBy(onlineTest, attend);
                    if (mark != null) {
                        mark.setMark(getNewStudentMark(persistentSuport, studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent()));
                    }
                }
            }
            IStudentTestLog studentTestLog = DomainFactory.makeStudentTestLog();
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

    private String getNewStudentMark(ISuportePersistente sp, IDistributedTest dt, IStudent s) throws ExcepcaoPersistencia {
        double totalMark = 0;
        List<IStudentTestQuestion> studentTestQuestionList = sp.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                s.getIdInternal(), dt.getIdInternal());
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        return (new java.text.DecimalFormat("#0.##").format(totalMark));
    }
}