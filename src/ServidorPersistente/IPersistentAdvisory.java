package ServidorPersistente;

import Dominio.IAdvisory;
import Util.AdvisoryRecipients;


/**
 * Created on 2003/09/06
 * @author Luis Cruz
 * Package ServidorPersistente
 * 
 */
public interface IPersistentAdvisory extends IPersistentObject {

	/**
	 * @param advisory
	 * @param advisoryRecipients
	 */
	void write(IAdvisory advisory, AdvisoryRecipients advisoryRecipients) throws ExcepcaoPersistencia;
}
