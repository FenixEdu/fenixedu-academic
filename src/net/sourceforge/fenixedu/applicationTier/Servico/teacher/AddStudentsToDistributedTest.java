/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class AddStudentsToDistributedTest implements IService {
    private String contextPath = new String();

    public AddStudentsToDistributedTest() {
    }

    public Integer run(Integer executionCourseId, Integer distributedTestId, List infoStudentList,
            String contextPath) throws FenixServiceException {
        if (infoStudentList == null || infoStudentList.size() == 0)
            return null;
        try {
            this.contextPath = contextPath.replace('\\', '/');
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();
            IPersistentDistributedTest persistentDistributedTest = persistentSuport
                    .getIPersistentDistributedTest();
            IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(
                    DistributedTest.class, distributedTestId, true);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();

            List studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readStudentTestQuestionsByDistributedTest(distributedTest);
            for (int i = 0; i < studentTestQuestionList.size(); i++) {
                IStudentTestQuestion studentTestQuestionExample = (IStudentTestQuestion) studentTestQuestionList
                        .get(i);

                List questionList = new ArrayList();
                questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata()
                        .getVisibleQuestions());

                for (int j = 0; j < infoStudentList.size(); j++) {
                    IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(
                            Student.class, ((InfoStudent) infoStudentList.get(j)).getIdInternal());
                    if (persistentSuport.getIPersistentStudentTestQuestion()
                            .readByStudentAndDistributedTest(student, distributedTest).isEmpty()) {
                        IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                        persistentSuport.getIPersistentStudentTestQuestion().lockWrite(
                                studentTestQuestion);
                        studentTestQuestion.setStudent(student);
                        studentTestQuestion.setDistributedTest(distributedTest);
                        studentTestQuestion.setTestQuestionOrder(studentTestQuestionExample
                                .getTestQuestionOrder());
                        studentTestQuestion.setTestQuestionValue(studentTestQuestionExample
                                .getTestQuestionValue());
                        studentTestQuestion.setOldResponse(new Integer(0));
                        studentTestQuestion.setCorrectionFormula(studentTestQuestionExample
                                .getCorrectionFormula());
                        studentTestQuestion.setTestQuestionMark(new Double(0));
                        studentTestQuestion.setResponse(null);

                        if (questionList.size() == 0)
                            questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata()
                                    .getVisibleQuestions());
                        IQuestion question = getStudentQuestion(questionList);
                        if (question == null) {
                            throw new InvalidArgumentsServiceException();
                        }
                        studentTestQuestion.setQuestion(question);
                        questionList.remove(question);

                    }
                }
            }
            //create advisory for new students
            IAdvisory advisory = createTestAdvisory(distributedTest);
            persistentSuport.getIPersistentAdvisory().simpleLockWrite(advisory);

            IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
            distributedTestAdvisory.setAdvisory(advisory);
            distributedTestAdvisory.setDistributedTest(distributedTest);
            persistentSuport.getIPersistentDistributedTestAdvisory().simpleLockWrite(
                    distributedTestAdvisory);

            return advisory.getIdInternal();
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
    }

    private IQuestion getStudentQuestion(List questions) {
        IQuestion question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = (IQuestion) questions.get(questionIndex);
        }
        return question;
    }

    private String getDateFormatted(Calendar date) {
        String result = new String();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    private String getHourFormatted(Calendar hour) {
        String result = new String();
        result += hour.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (hour.get(Calendar.MINUTE) < 10)
            result += "0";
        result += hour.get(Calendar.MINUTE);
        return result;
    }

    private IAdvisory createTestAdvisory(IDistributedTest distributedTest) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender("Docente da disciplina "
                + ((IExecutionCourse) distributedTest.getTestScope().getDomainObject()).getNome());
        String msgBeginning;
        advisory.setSubject(distributedTest.getTitle());
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
            msgBeginning = new String("Tem um <a href='" + this.contextPath
                    + "/student/studentTests.do?method=prepareToDoTest&testCode="
                    + distributedTest.getIdInternal() + "'>questionário</a> para responder entre ");
        else
            msgBeginning = new String("Tem uma <a href='" + this.contextPath
                    + "/student/studentTests.do?method=prepareToDoTest&testCode="
                    + distributedTest.getIdInternal() + "'>Ficha de Trabalho</a> a realizar entre ");

        advisory.setMessage(msgBeginning + " as " + getHourFormatted(distributedTest.getBeginHour())
                + " de " + getDateFormatted(distributedTest.getBeginDate()) + " e as "
                + getHourFormatted(distributedTest.getEndHour()) + " de "
                + getDateFormatted(distributedTest.getEndDate()));
        advisory.setOnlyShowOnce(new Boolean(false));
        return advisory;
    }

}