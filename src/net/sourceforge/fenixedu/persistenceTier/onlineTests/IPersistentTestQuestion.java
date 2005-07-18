/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTestQuestion extends IPersistentObject {
    public abstract List<ITestQuestion> readByTest(Integer testId) throws ExcepcaoPersistencia;

    public abstract ITestQuestion readByTestAndQuestion(Integer testId, Integer questionId) throws ExcepcaoPersistencia;

    public abstract List<ITestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia;

}