/*
 * Created on 2/Fev/2004
 *
 */
package ServidorPersistente;

import Dominio.IDistributedTest;
import Dominio.IOnlineTest;

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