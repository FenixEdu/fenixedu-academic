
package ServidorPersistente;

import Dominio.IGuide;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGuide extends IPersistentObject {

	/**
	 * 
	 * @param guideToWrite
	 * @throws ExcepcaoPersistencia
	 * @throws ExistingPersistentException
	 */
	public void write(IGuide guideToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	
	/**
	 * 
	 * @param number
	 * @param year
	 * @return IGuide
	 * @throws ExcepcaoPersistencia
	 */
	public IGuide readByNumberAndYear(Integer number, Integer year) throws ExcepcaoPersistencia;

	
}
