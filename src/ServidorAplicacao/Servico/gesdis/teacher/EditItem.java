/*
 * Created on 31/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.gesdis.InfoItem;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
 */
public class EditItem implements IServico {


	private static EditItem service = new EditItem();
    

	/**

	 * The singleton access method of this class.

	 **/

	public static EditItem getService() {

		return service;

	}

    
	public String getNome() {

		return "EditItem";

	}

    

	/**
	 * Executes the service.
	 *
	 **/
public Boolean run(InfoItem oldInfoItem, InfoItem newInfoItem)
	throws FenixServiceException {

	ISection fatherSection = null;
	IItem item = null;
	try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentSection persistentSection = sp.getIPersistentSection();
		IPersistentItem persistentItem = sp.getIPersistentItem();

		ISection section =
			Cloner.copyInfoSection2ISection(oldInfoItem.getInfoSection());

		item =
			persistentItem.readBySectionAndName(section, oldInfoItem.getName());

		item.setInformation(newInfoItem.getInformation());
		item.setItemOrder(newInfoItem.getItemOrder());
		item.setName(newInfoItem.getName());
		item.setUrgent(newInfoItem.getUrgent());
		persistentItem.lockWrite(item);
	} catch (ExcepcaoPersistencia e) {
		throw new FenixServiceException(e);
	}
	return new Boolean(true);
}
}

