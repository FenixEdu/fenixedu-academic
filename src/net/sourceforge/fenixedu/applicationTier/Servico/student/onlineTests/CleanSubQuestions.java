/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.SubQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

import org.joda.time.DateTime;

/**
 * @author Susana Fernandes
 */
public class CleanSubQuestions extends Service {

    public void run(Registration registration, DistributedTest distributedTest, Integer exerciseCode, Integer itemCode,
	    String path) throws FenixServiceException, ExcepcaoPersistencia {
	if (distributedTest == null) {
	    throw new FenixServiceException();
	}
	Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(registration,
		distributedTest);
	for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
	    if (studentTestQuestion.getQuestion().getIdInternal().equals(exerciseCode)) {
		ParseSubQuestion parse = new ParseSubQuestion();
		try {
		    parse.parseStudentTestQuestion(studentTestQuestion, path.replace('\\', '/'));
		} catch (Exception e) {
		    throw new FenixServiceException(e);
		}
		SubQuestion subQuestion = studentTestQuestion.getStudentSubQuestions().get(0);
		if (subQuestion.getItemId().equals(studentTestQuestion.getItemId())) {
		    // e a 1ª
		    studentTestQuestion.setResponse(null);
		} else {
		    studentTestQuestion.delete();
		}
	    }
	}
	StudentTestLog studentTestLog = new StudentTestLog();
	studentTestLog.setDistributedTest(distributedTest);
	studentTestLog.setStudent(registration);
	studentTestLog.setDateDateTime(new DateTime());
	studentTestLog.setEvent("Apagou resposta da pergunta/alínea: " + itemCode);
	return;
    }

}