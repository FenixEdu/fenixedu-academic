/*
 * Created on 10/Dez/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Goes
 *  
 */
public class ReadAuthorById implements IService {

    public InfoAuthor run(Integer authorId) throws ExcepcaoPersistencia {
        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentAuthor persistentAuthorDAO = suportePersistente.getIPersistentAuthor();

        IAuthor author = (IAuthor) persistentAuthorDAO.readByOID(Author.class, authorId);
        InfoAuthor infoAuthor = new InfoAuthor();
        infoAuthor.copyFromDomain(author);

        return infoAuthor;
    }
}