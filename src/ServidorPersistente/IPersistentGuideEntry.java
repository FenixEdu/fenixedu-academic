
package ServidorPersistente;

import java.util.List;

import Dominio.IGuide;
import Dominio.IGuideEntry;
import Util.DocumentType;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGuideEntry extends IPersistentObject {

	/**
	 * 
	 * @param guide
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readByGuide(IGuide guide) throws ExcepcaoPersistencia; 
	
	
	
	/**
	 * 
	 * @param guideEntry
	 * @throws ExcepcaoPersistencia
	 */
	public void delete(IGuideEntry guideEntry)  throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param guide
	 * @param graduationType
	 * @param documentType
	 * @param description
	 * @return IGuideEntry
	 * @throws ExcepcaoPersistencia
	 */
	public IGuideEntry readByGuideAndGraduationTypeAndDocumentTypeAndDescription(IGuide guide,GraduationType graduationType,
					DocumentType documentType, String description) throws ExcepcaoPersistencia;
		
}
