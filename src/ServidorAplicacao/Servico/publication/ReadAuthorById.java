/*
 * Created on 10/Dez/2004
 *
 */
package ServidorAplicacao.Servico.publication;

import org.apache.commons.lang.ArrayUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.publication.InfoAuthor;
import Dominio.publication.Author;
import Dominio.publication.IAuthor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentAuthor;

/**
 * @author Ricardo Goes
 *  
 */
public class ReadAuthorById implements IService {

    public InfoAuthor run(Integer authorId) throws ExcepcaoPersistencia {
        ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();

        IPersistentAuthor persistentAuthorDAO = suportePersistente.getIPersistentAuthor();

        IAuthor author = (IAuthor) persistentAuthorDAO.readByOID(Author.class, authorId);
        InfoAuthor infoAuthor = new InfoAuthor();
        infoAuthor.copyFromDomain(author);

        return infoAuthor;
    }
}