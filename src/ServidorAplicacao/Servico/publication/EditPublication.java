/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.publication;

import DataBeans.InfoObject;
import DataBeans.publication.InfoPublication;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationType;
import Dominio.publication.PublicationType;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;
import ServidorPersistente.publication.IPersistentPublicationType;

public class EditPublication extends EditDomainObjectService {

	private static EditPublication service = new EditPublication();

	public static EditPublication getService() {
		return service;
	}

	/**
	 *  
	 */
	private EditPublication() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "EditPublication";
	}

	protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
		IPersistentPublication persistentPublication =
			sp.getIPersistentPublication();
		return persistentPublication;
	}

	protected IDomainObject clone2DomainObject(InfoObject infoObject) {
		ISuportePersistente persistentSuport;
		IPublication publication = null;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();

			Integer keyPublicationTypeId =
				(Integer) ((InfoPublication) infoObject)
					.getKeyPublicationType();
			
			IPersistentPublicationType persistentPublicationType =
				persistentSuport.getIPersistentPublicationType();
				
			IPublicationType publicationType =
				(IPublicationType) persistentPublicationType.readByOID(
					PublicationType.class,
					keyPublicationTypeId);

			publication =
				Cloner.copyInfoPublication2IPublication(
					(InfoPublication) infoObject);
			publication.setType(publicationType);
			
			

		} catch (ExcepcaoPersistencia e) {

			e.printStackTrace();
		}
		return publication;

	}
}
