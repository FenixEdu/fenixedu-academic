/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public interface IPersistentOnlineTest extends IPersistentObject {
    public abstract Object readByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia;
}