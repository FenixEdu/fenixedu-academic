/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTest extends IPersistentObject {
    public List<Test> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia;

    public List<Test> readAll() throws ExcepcaoPersistencia;
}