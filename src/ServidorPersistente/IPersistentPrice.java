
package ServidorPersistente;

import java.util.List;

import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentPrice extends IPersistentObject {
    
	/**
	 * 
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readAll() throws ExcepcaoPersistencia; 
	
	/**
	 * 
	 * @param graduationType
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readByGraduationType(GraduationType graduationType) throws ExcepcaoPersistencia; 
		

}
