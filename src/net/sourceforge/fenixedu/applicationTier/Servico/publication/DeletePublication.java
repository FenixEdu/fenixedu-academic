/*
 * Created on Jun 11, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Carlos Pereira & Francisco Passos
 *  
 */
public class DeletePublication implements IService {

	public void run(final Integer publicationId) throws ExcepcaoPersistencia {
		final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentPublication persistentPublication = sp.getIPersistentPublication();
        final IPersistentPublicationAuthor persistentPublicationAuthor = sp.getIPersistentPublicationAuthor();

        final IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class, publicationId);

        final List publicationAuthors = publication.getPublicationAuthors();
        for (final Iterator iterator = publicationAuthors.iterator(); iterator.hasNext(); ) {
            final IPublicationAuthor publicationAuthor = (IPublicationAuthor) iterator.next();
            publicationAuthor.setPublication(null);
            publicationAuthor.setAuthor(null);
            persistentPublicationAuthor.deleteByOID(PublicationAuthor.class, publicationAuthor.getIdInternal());
        }
        publicationAuthors.clear();

        final List publicationTeachers = publication.getPublicationTeachers();
        for (final Iterator iterator = publicationTeachers.iterator(); iterator.hasNext(); ) {
            final IPublicationTeacher publicationTeacher = (IPublicationTeacher) iterator.next();
            publicationTeacher.setPublication(null);
            publicationTeacher.setTeacher(null);
            persistentPublicationAuthor.deleteByOID(PublicationTeacher.class, publicationTeacher.getIdInternal());
        }
        publicationTeachers.clear();
        

		persistentPublication.deleteByOID(Publication.class, publicationId);		
	}

}