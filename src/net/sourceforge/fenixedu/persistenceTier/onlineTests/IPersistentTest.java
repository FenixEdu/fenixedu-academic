/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTest extends IPersistentObject {
    public List<ITest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia;

    public List<ITest> readAll() throws ExcepcaoPersistencia;
}