/*
 * Created on Jul 31, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries.Mock;

import java.util.List;

import Dominio.Seminaries.IModality;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryModality;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at Jul 31, 2003, 14:47:01
 * 
 */
public class MockModalityOJB extends ObjectFenixOJB implements IPersistentSeminaryModality
{
    public IModality readByName(String name) throws ExcepcaoPersistencia
	{
        throw new ExcepcaoPersistencia();
	}
    
    public List readAll() throws ExcepcaoPersistencia
    {
        throw new ExcepcaoPersistencia();
    }
    
    public void delete(IModality modality) throws ExcepcaoPersistencia
    {
        throw new ExcepcaoPersistencia();
    }    
}
