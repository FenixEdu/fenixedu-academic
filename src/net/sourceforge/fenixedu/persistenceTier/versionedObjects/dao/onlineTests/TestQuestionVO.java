/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Susana Fernandes
 */
public class TestQuestionVO extends VersionedObjectsBase implements IPersistentTestQuestion {

    public List<ITestQuestion> readByTest(Integer testId) {
        final List<ITestQuestion> testQuestionList = (List<ITestQuestion>) readAll(TestQuestion.class);
        List<ITestQuestion> result = new ArrayList<ITestQuestion>();
        for (ITestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyTest().equals(testId)) {
                result.add(testQuestion);
            }
        }
        Collections.sort(result, new BeanComparator("testQuestionOrder"));
        return result;
    }

    public ITestQuestion readByTestAndQuestion(Integer testId, Integer questionId) throws ExcepcaoPersistencia {
        final List<ITestQuestion> testQuestionList = (List<ITestQuestion>) readAll(TestQuestion.class);
        for (ITestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyTest().equals(testId) && testQuestion.getKeyQuestion().equals(questionId)) {
                return testQuestion;
            }
        }
        return null;
    }

    public List<ITestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        final List<ITestQuestion> testQuestionList = (List<ITestQuestion>) readAll(TestQuestion.class);
        List<ITestQuestion> result = new ArrayList<ITestQuestion>();
        for (ITestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyQuestion().equals(questionId)) {
                result.add(testQuestion);
            }
        }
        return result;
    }
}