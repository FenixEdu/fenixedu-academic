
package ServidorPersistente;

import java.util.List;

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
	 * @return A List of the Guide Versions
	 * @throws ExcepcaoPersistencia
	 */
	public List readByNumberAndYear(Integer number, Integer year) throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param number
	 * @param year
	 * @param version
	 * @return IGuide
	 * @throws ExcepcaoPersistencia
	 */
	public IGuide readByNumberAndYearAndVersion(Integer number, Integer year, Integer version) throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param year
	 * @return Guide Number
	 * @throws ExcepcaoPersistencia
	 */
	public Integer generateGuideNumber(Integer year)  throws ExcepcaoPersistencia;
	
	
	/**
	 * 
	 * @param year
	 * @return A list with diferente Guides for this Year
	 * @throws ExcepcaoPersistencia
	 */
	public List readByYear(Integer year) throws ExcepcaoPersistencia;
	
	
}
