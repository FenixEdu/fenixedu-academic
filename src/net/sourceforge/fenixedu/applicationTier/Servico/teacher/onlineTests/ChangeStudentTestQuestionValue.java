/*
 * Created on Oct 20, 2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.IQuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.QuestionCorrectionStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys.IQuestionCorrectionStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
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
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestionValue implements IService {
    public List<InfoSiteDistributedTestAdvisory> run(Integer executionCourseId, Integer distributedTestId, Double newValue, Integer questionId,
            Integer studentId, TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();
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

            if (!group.contains(studentTestQuestion.getStudent().getPerson())) {
                group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));
            }

            if (studentTestQuestion.getResponse() != null
                    && studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                studentTestQuestion.setTestQuestionMark(getNewQuestionMark(studentTestQuestion, newValue, path.replace('\\', '/')));

                OnlineTest onlineTest = (OnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(
                        studentTestQuestion.getDistributedTest().getIdInternal());
                ExecutionCourse executionCourse = (ExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(
                        ExecutionCourse.class, executionCourseId);
                Attends attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
                        studentTestQuestion.getStudent().getIdInternal(), executionCourse.getIdInternal());
                Mark mark = persistentSuport.getIPersistentMark().readBy(onlineTest, attend);
                if (mark != null) {
                    mark.setMark(getNewStudentMark(persistentSuport, studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent()));
                }
            }
            studentTestQuestion.setTestQuestionValue(newValue);

            infoSiteDistributedTestAdvisory.setInfoStudentList(group);
            infoSiteDistributedTestAdvisoryList.add(infoSiteDistributedTestAdvisory);
            StudentTestLog studentTestLog = DomainFactory.makeStudentTestLog();
            studentTestLog.setDistributedTest(studentTestQuestion.getDistributedTest());
            studentTestLog.setStudent(studentTestQuestion.getStudent());
            studentTestLog.setDate(Calendar.getInstance().getTime());
            ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
            studentTestLog.setEvent(MessageFormat.format(bundle.getString("message.changeStudentValueLogMessage"), new Object[] { newValue }));
        }
        return infoSiteDistributedTestAdvisoryList;
    }

    private String getNewStudentMark(ISuportePersistente sp, DistributedTest dt, Student s) throws ExcepcaoPersistencia {
        double totalMark = 0;
        List<StudentTestQuestion> studentTestQuestionList = sp.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
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

    private Double getNewQuestionMark(StudentTestQuestion studentTestQuestion, Double newValue, String path) throws FenixServiceException {
        Double newMark = new Double(0);
        if (studentTestQuestion.getResponse() != null && !newValue.equals(Double.parseDouble("0"))) {
            if (studentTestQuestion.getTestQuestionValue().equals(Double.parseDouble("0"))) {
                InfoStudentTestQuestion infoStudentTestQuestion = InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest
                        .newInfoFromDomain(studentTestQuestion);
                ParseQuestion parse = new ParseQuestion();
                try {
                    infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, path);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                infoStudentTestQuestion.setTestQuestionValue(newValue);
                infoStudentTestQuestion.setQuestion(correctQuestionValues(infoStudentTestQuestion.getQuestion(), newValue));
                IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory = QuestionCorrectionStrategyFactory.getInstance();
                IQuestionCorrectionStrategy questionCorrectionStrategy = questionCorrectionStrategyFactory
                        .getQuestionCorrectionStrategy(infoStudentTestQuestion);
                infoStudentTestQuestion = questionCorrectionStrategy.getMark(infoStudentTestQuestion);
                return infoStudentTestQuestion.getTestQuestionMark();
            } else if (!studentTestQuestion.getTestQuestionMark().equals(Double.parseDouble("0"))) {
                newMark = (newValue * studentTestQuestion.getTestQuestionMark())
                        * (java.lang.Math.pow(studentTestQuestion.getTestQuestionValue(), -1));
            }

        }
        return newMark;
    }

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion, Double questionValue) {
        Double maxValue = new Double(0);
        for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD)
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0)
                    maxValue = responseProcessing.getResponseValue();
        }
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue.doubleValue() * Math.pow(maxValue.doubleValue(), -1);
            for (ResponseProcessing responseProcessing : (List<ResponseProcessing>) infoQuestion.getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(new Double(responseProcessing.getResponseValue().doubleValue() * difValue));
            }
        }

        return infoQuestion;
    }
}