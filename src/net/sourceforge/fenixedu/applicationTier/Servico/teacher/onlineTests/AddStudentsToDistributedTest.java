/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author Susana Fernandes
 */
public class AddStudentsToDistributedTest extends Service {
    private String contextPath = new String();

    public Integer run(Integer executionCourseId, Integer distributedTestId, List infoStudentList, String contextPath) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {
        if (infoStudentList == null || infoStudentList.size() == 0)
            return null;
        this.contextPath = contextPath.replace('\\', '/');
        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        Set<StudentTestQuestion> studentTestQuestions = distributedTest.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();
        for (StudentTestQuestion studentTestQuestionExample : studentTestQuestions) {
            List<Question> questionList = new ArrayList<Question>();
            questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

            for (int j = 0; j < infoStudentList.size(); j++) {
                Student student = rootDomainObject.readStudentByOID(
                        ((InfoStudent) infoStudentList.get(j)).getIdInternal());
                if (StudentTestQuestion.findStudentTestQuestions(student, distributedTest).isEmpty()) {
                    StudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                    studentTestQuestion.setStudent(student);
                    studentTestQuestion.setDistributedTest(distributedTest);
                    studentTestQuestion.setTestQuestionOrder(studentTestQuestionExample.getTestQuestionOrder());
                    studentTestQuestion.setTestQuestionValue(studentTestQuestionExample.getTestQuestionValue());
                    studentTestQuestion.setCorrectionFormula(studentTestQuestionExample.getCorrectionFormula());
                    studentTestQuestion.setTestQuestionMark(new Double(0));
                    studentTestQuestion.setResponse(null);

                    if (questionList.size() == 0)
                        questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
                    Question question = getStudentQuestion(questionList);
                    if (question == null) {
                        throw new InvalidArgumentsServiceException();
                    }
                    studentTestQuestion.setQuestion(question);
                    questionList.remove(question);

                }
            }
        }
        // create advisory for new students
        Advisory advisory = createTestAdvisory(distributedTest);

        DistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);

        return advisory.getIdInternal();
    }

    private Question getStudentQuestion(List<Question> questions) {
        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

    private Advisory createTestAdvisory(DistributedTest distributedTest) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources");
        Advisory advisory = new Advisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"), new Object[] { ((ExecutionCourse) distributedTest
                .getTestScope().getDomainObject()).getNome() }));
        
        advisory.setSubject(distributedTest.getTitle());
        final String beginHour = DateFormatUtils.format(distributedTest.getBeginHour().getTime(), "HH:mm");
        final String beginDate = DateFormatUtils.format(distributedTest.getBeginDate().getTime(), "dd/MM/yyyy");
        final String endHour = DateFormatUtils.format(distributedTest.getEndHour().getTime(), "HH:mm");
        final String endDate = DateFormatUtils.format(distributedTest.getEndDate().getTime(), "dd/MM/yyyy");

        Object[] args = { this.contextPath, distributedTest.getIdInternal().toString(), beginHour, beginDate, endHour, endDate };
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.distributeInquiryMessage"), args));
        } else {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.distributeTestMessage"), args));
        }
        return advisory;
    }
}