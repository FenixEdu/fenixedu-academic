/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class AddStudentsToDistributedTest extends Service {

    public void run(Integer executionCourseId, Integer distributedTestId, List infoStudentList, String contextPath)
	    throws ExcepcaoPersistencia, InvalidArgumentsServiceException {
	if (infoStudentList == null || infoStudentList.size() == 0)
	    return;
	DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
	if (distributedTest == null)
	    throw new InvalidArgumentsServiceException();

	Set<StudentTestQuestion> studentTestQuestions = distributedTest
		.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();
	for (StudentTestQuestion studentTestQuestionExample : studentTestQuestions) {
	    List<Question> questionList = new ArrayList<Question>();
	    questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

	    for (int j = 0; j < infoStudentList.size(); j++) {
		Registration registration = rootDomainObject.readRegistrationByOID(((InfoStudent) infoStudentList.get(j))
			.getIdInternal());
		StudentTestQuestion studentTestQuestion = new StudentTestQuestion();
		studentTestQuestion.setStudent(registration);
		studentTestQuestion.setDistributedTest(distributedTest);
		studentTestQuestion.setTestQuestionOrder(studentTestQuestionExample.getTestQuestionOrder());
		studentTestQuestion.setTestQuestionValue(studentTestQuestionExample.getTestQuestionValue());
		studentTestQuestion.setCorrectionFormula(studentTestQuestionExample.getCorrectionFormula());
		studentTestQuestion.setTestQuestionMark(new Double(0));
		studentTestQuestion.setResponse(null);

		if (questionList.size() == 0)
		    questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
		Question question = null;
		try {
		    question = getStudentQuestion(questionList, contextPath.replace('\\', '/'));
		} catch (ParseException e) {
		    throw new InvalidArgumentsServiceException();
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

    private Question getStudentQuestion(List<Question> questions, String path) throws ParseException, ParseQuestionException {
	Question question = null;
	if (questions.size() != 0) {
	    Random r = new Random();
	    int questionIndex = r.nextInt(questions.size());
	    question = questions.get(questionIndex);
	}
	return question.getSubQuestions() == null || question.getSubQuestions().size() == 0 ? new ParseSubQuestion()
		.parseSubQuestion(question, path) : question;
    }

}