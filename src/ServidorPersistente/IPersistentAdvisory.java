package ServidorPersistente;

import java.util.List;

import Dominio.IAdvisory;
import Dominio.IPessoa;
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
	public void write(IAdvisory advisory, AdvisoryRecipients advisoryRecipients) throws ExcepcaoPersistencia;
	public void write(IAdvisory advisory, List group) throws ExcepcaoPersistencia;
	public void write(IAdvisory advisory, IPessoa person) throws ExcepcaoPersistencia;
}
