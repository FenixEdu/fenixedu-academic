/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

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
        IPersistentPublication persistentPublication = sp.getIPersistentPublication();
        return persistentPublication;
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        ISuportePersistente persistentSuport;
        IPublication publication = null;
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();

            Integer keyPublicationTypeId = ((InfoPublication) infoObject).getKeyPublicationType();

            IPersistentPublicationType persistentPublicationType = persistentSuport
                    .getIPersistentPublicationType();

            IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, keyPublicationTypeId);

            publication = Cloner.copyInfoPublication2IPublication((InfoPublication) infoObject);
            publication.setType(publicationType);

        } catch (ExcepcaoPersistencia e) {

            e.printStackTrace();
        }
        return publication;

    }
}