/*
 * Created on Jan 21, 2004
 */
package ServidorPersistente.grant;

import Dominio.grant.contract.IGrantProject;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentGrantProject extends IPersistentObject
{
	public IGrantProject readGrantProjectByNumber(String number) throws ExcepcaoPersistencia;
}
