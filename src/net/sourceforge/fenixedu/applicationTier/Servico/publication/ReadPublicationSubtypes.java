/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.publication.IPublicationSubtype;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadPublicationSubtypes implements IServico {
    private static ReadPublicationSubtypes service = new ReadPublicationSubtypes();

    /**
     *  
     */
    private ReadPublicationSubtypes() {

    }

    public static ReadPublicationSubtypes getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadPublicationSubtypes";
    }

    public List run(String user, int publicationTypeId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            //            IPersistentTeacher persistentTeacher =
            // persistentSuport.getIPersistentTeacher();
            //            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            //            InfoTeacher infoTeacher =
            // Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentPublicationType persistentPublicationType = persistentSuport
                    .getIPersistentPublicationType();
            PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, new Integer(publicationTypeId));

            List publicationSubtypeList = publicationType.getSubtypes();

            List result = (List) CollectionUtils.collect(publicationSubtypeList, new Transformer() {
                public Object transform(Object o) {
                    IPublicationSubtype publicationSubtype = (IPublicationSubtype) o;
                    return Cloner.copyIPublicationSubtype2InfoPublicationSubtype(publicationSubtype);
                }
            });

            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}