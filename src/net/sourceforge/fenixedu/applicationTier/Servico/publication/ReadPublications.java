/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

/**
 * @author TJBF & PFON
 * 
 */
public class ReadPublications implements IServico {
    private static ReadPublications service = new ReadPublications();

    /**
     * 
     */
    private ReadPublications() {

    }

    public static ReadPublications getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadPublications";
    }

    public SiteView run(String user, String publicationType) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);

            // the cientific publication has the value zero
            Integer typePublication = new Integer(0);

            if (publicationType.equalsIgnoreCase(PublicationConstants.DIDATIC_STRING)) {
                typePublication = PublicationConstants.DIDATIC;
            }

            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);

            IPersistentPublicationTeacher persistentPublicationTeacher = persistentSuport
                    .getIPersistentPublicationTeacher();

            PublicationArea publicationArea = null;
            if (PublicationConstants.CIENTIFIC.equals(typePublication))
                publicationArea = PublicationArea.CIENTIFIC;
            else
                publicationArea = PublicationArea.DIDATIC;

            List publicationsTeacher = persistentPublicationTeacher.readByTeacherAndPublicationArea(
                    teacher, publicationArea);

            List infoPublications = new ArrayList();
            Iterator iter = publicationsTeacher.iterator();
            while (iter.hasNext()) {
                IPublicationTeacher publicationTeacher = (IPublicationTeacher) iter.next();
                IPublication publication = publicationTeacher.getPublication();
                publication.setPublicationString(publicationTeacher.getPublication().toString());
                infoPublications.add(InfoPublication.newInfoFromDomain(publication));
            }

            InfoSitePublications bodyComponent = new InfoSitePublications();
            bodyComponent.setInfoPublications(infoPublications);
            bodyComponent.setInfoTeacher(infoTeacher);

            SiteView siteView = new SiteView(bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}