/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTestQuestion extends IPersistentObject {
    public abstract List readByTest(ITest test) throws ExcepcaoPersistencia;

    public abstract ITestQuestion readByTestAndQuestion(ITest test, IQuestion question)
            throws ExcepcaoPersistencia;

    public abstract List readByQuestion(IQuestion question) throws ExcepcaoPersistencia;

    public abstract void deleteByTest(ITest test) throws ExcepcaoPersistencia;

    public abstract void delete(ITestQuestion testQuestion) throws ExcepcaoPersistencia;
}