
package ServidorPersistente;

import java.util.List;

import Dominio.IGuide;
import Dominio.IGuideSituation;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationOfGuide;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGuideSituation extends IPersistentObject {

	/**
	 * 
	 * @param guideSituation
	 * @throws ExcepcaoPersistencia
	 * @throws ExistingPersistentException
	 */
	public void write(IGuideSituation guideSituation) throws ExcepcaoPersistencia, ExistingPersistentException;
	
	/**
	 * 
	 * @param guide
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readByGuide(IGuide guide) throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param guide
	 * @return IGuideSituation
	 * @throws ExcepcaoPersistencia
	 */
	public IGuideSituation readGuideActiveSituation(IGuide guide) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param guide
	 * @param guideSituation
	 * @return IGuideSituation
	 * @throws ExcepcaoPersistencia
	 */
	public IGuideSituation readByGuideAndSituation(IGuide guide, SituationOfGuide guideSituation) throws ExcepcaoPersistencia;
	
}
