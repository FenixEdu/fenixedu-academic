/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IOnlineTest;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public interface IPersistentOnlineTest extends IPersistentObject {
    public abstract Object readByDistributedTest(IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public void delete(IOnlineTest onlineTest) throws ExcepcaoPersistencia;
}