/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadPublicationTypes implements IServico {
    private static ReadPublicationTypes service = new ReadPublicationTypes();

    /**
     *  
     */
    private ReadPublicationTypes() {

    }

    public static ReadPublicationTypes getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadPublicationTypes";
    }

    public List run(String user) throws FenixServiceException {
        try {

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentPublicationType persistentPublicationType = persistentSuport
                    .getIPersistentPublicationType();
            List publicationTypeList = (List)persistentPublicationType.readAll(PublicationType.class);

            List result = (List) CollectionUtils.collect(publicationTypeList, new Transformer() {
                public Object transform(Object o) {
                    IPublicationType publicationType = (IPublicationType) o;
                    return Cloner.copyIPublicationType2InfoPublicationType(publicationType);
                }
            });

            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}