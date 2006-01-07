/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTestQuestion extends IPersistentObject {
    public abstract List<TestQuestion> readByTest(Integer testId) throws ExcepcaoPersistencia;

    public abstract TestQuestion readByTestAndQuestion(Integer testId, Integer questionId) throws ExcepcaoPersistencia;

    public abstract List<TestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia;

}