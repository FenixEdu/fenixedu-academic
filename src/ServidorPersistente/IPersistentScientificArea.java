/*
 * Created on 18/Dez/2003
 *
 */
package ServidorPersistente;

import Dominio.IScientificArea;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentScientificArea extends IPersistentObject
{
	public IScientificArea readByName(String name) throws ExcepcaoPersistencia;
}