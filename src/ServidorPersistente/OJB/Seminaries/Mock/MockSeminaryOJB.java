/*
 * Created on 28/Jul/2003, 11:01:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries.Mock;

import java.util.List;

import Dominio.Seminaries.ISeminary;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 28/Jul/2003, 11:01:39
 * 
 */
public class MockSeminaryOJB extends ObjectFenixOJB implements IPersistentSeminary
{
    public ISeminary readByName(String name) throws ExcepcaoPersistencia
        {
            throw new ExcepcaoPersistencia();
        }
        public List readAll() throws ExcepcaoPersistencia
        {
            throw new ExcepcaoPersistencia();
        }
        public void delete(ISeminary seminary) throws ExcepcaoPersistencia
        {
            throw new ExcepcaoPersistencia();
        }
}
