package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

public class DeleteExercise extends Service {

    public void run(Integer executionCourseId, Integer metadataId) throws InvalidArgumentsServiceException, ExcepcaoPersistencia {
	Metadata metadata = rootDomainObject.readMetadataByOID(metadataId);
	if (metadata == null) {
	    throw new InvalidArgumentsServiceException();
	}

	List<Question> questionList = metadata.getQuestions();
	boolean delete = true;
	for (Question question : questionList) {
	    List<TestQuestion> testQuestionList = question.getTestQuestions();
	    for (TestQuestion testQuestion : testQuestionList) {
		removeTestQuestionFromTest(testQuestion);
	    }
	    if (!question.hasAnyStudentTestsQuestions()) {
		question.delete();
	    } else {
		question.setVisibility(Boolean.FALSE);
		delete = false;
	    }
	}
	if (delete) {
	    metadata.delete();
	} else {
	    metadata.setVisibility(Boolean.FALSE);
	}
    }

    private void removeTestQuestionFromTest(TestQuestion testQuestion) throws ExcepcaoPersistencia {
	Test test = testQuestion.getTest();
	if (test == null) {
	    throw new ExcepcaoPersistencia();
	}

	List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
	Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));

	Integer questionOrder = testQuestion.getTestQuestionOrder();
	for (TestQuestion iterTestQuestion : testQuestionList) {
	    Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
	    if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
		iterTestQuestion.setTestQuestionOrder(iterQuestionOrder - 1);
	    }
	}
	testQuestion.delete();
	test.setLastModifiedDate(Calendar.getInstance().getTime());
    }

}
