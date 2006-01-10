/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Susana Fernandes
 */
public class TestQuestionVO extends VersionedObjectsBase implements IPersistentTestQuestion {

    public List<TestQuestion> readByTest(Integer testId) {
        final List<TestQuestion> testQuestionList = (List<TestQuestion>) readAll(TestQuestion.class);
        List<TestQuestion> result = new ArrayList<TestQuestion>();
        for (TestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyTest().equals(testId)) {
                result.add(testQuestion);
            }
        }
        Collections.sort(result, new BeanComparator("testQuestionOrder"));
        return result;
    }

    public TestQuestion readByTestAndQuestion(Integer testId, Integer questionId) throws ExcepcaoPersistencia {
        final List<TestQuestion> testQuestionList = (List<TestQuestion>) readAll(TestQuestion.class);
        for (TestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyTest().equals(testId) && testQuestion.getKeyQuestion().equals(questionId)) {
                return testQuestion;
            }
        }
        return null;
    }

    public List<TestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        final List<TestQuestion> testQuestionList = (List<TestQuestion>) readAll(TestQuestion.class);
        List<TestQuestion> result = new ArrayList<TestQuestion>();
        for (TestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyQuestion().equals(questionId)) {
                result.add(testQuestion);
            }
        }
        return result;
    }
}