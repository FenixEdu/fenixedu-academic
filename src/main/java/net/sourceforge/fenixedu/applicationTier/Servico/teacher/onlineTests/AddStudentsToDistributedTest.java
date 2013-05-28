/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 */
public class AddStudentsToDistributedTest {

    protected void run(Integer executionCourseId, Integer distributedTestId, List<InfoStudent> infoStudentList, String contextPath)
            throws InvalidArgumentsServiceException {
        if (infoStudentList == null || infoStudentList.size() == 0) {
            return;
        }
        DistributedTest distributedTest = AbstractDomainObject.fromExternalId(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestions =
                distributedTest.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();
        int order = 0;
        for (StudentTestQuestion studentTestQuestionExample : studentTestQuestions) {
            if (studentTestQuestionExample.getQuestion().getSubQuestions() == null
                    || studentTestQuestionExample.getQuestion().getSubQuestions().size() == 0) {
                try {
                    new ParseSubQuestion().parseSubQuestion(studentTestQuestionExample.getQuestion(),
                            contextPath.replace('\\', '/'));
                } catch (ParseQuestionException e) {
                    throw new InvalidArgumentsServiceException();
                }
            }

            if (!studentTestQuestionExample.isSubQuestion()) {
                order++;
                List<Question> questionList = new ArrayList<Question>();
                questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

                for (InfoStudent infoStudent : infoStudentList) {
                    Registration registration = AbstractDomainObject.fromExternalId(infoStudent.getExternalId());
                    StudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                    studentTestQuestion.setStudent(registration);
                    studentTestQuestion.setDistributedTest(distributedTest);
                    studentTestQuestion.setTestQuestionOrder(order);
                    studentTestQuestion.setTestQuestionValue(studentTestQuestionExample.getTestQuestionValue());
                    studentTestQuestion.setCorrectionFormula(studentTestQuestionExample.getCorrectionFormula());
                    studentTestQuestion.setTestQuestionMark(new Double(0));
                    studentTestQuestion.setResponse(null);

                    if (questionList.size() == 0) {
                        questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
                    }
                    Question question = null;
                    try {
                        question = getStudentQuestion(questionList, contextPath.replace('\\', '/'));
                    } catch (ParseQuestionException e) {
                        throw new InvalidArgumentsServiceException();
                    }
                    if (question == null) {
                        throw new InvalidArgumentsServiceException();
                    }
                    if (question.getSubQuestions().size() >= 1 && question.getSubQuestions().get(0).getItemId() != null) {
                        studentTestQuestion.setItemId(question.getSubQuestions().get(0).getItemId());
                    }
                    studentTestQuestion.setQuestion(question);
                    questionList.remove(question);
                }
            }
        }
    }

    private Question getStudentQuestion(List<Question> questions, String path) throws ParseQuestionException {
        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question.getSubQuestions() == null || question.getSubQuestions().size() == 0 ? new ParseSubQuestion()
                .parseSubQuestion(question, path) : question;
    }

    // Service Invokers migrated from Berserk

    private static final AddStudentsToDistributedTest serviceInstance = new AddStudentsToDistributedTest();

    @Service
    public static void runAddStudentsToDistributedTest(Integer executionCourseId, Integer distributedTestId,
            List<InfoStudent> infoStudentList, String contextPath) throws InvalidArgumentsServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, distributedTestId, infoStudentList, contextPath);
    }

}