/*
 * Created on Jun 7, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import Dominio.publication.IPublication;
import Dominio.publication.Publication;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadAuthorsByPublicationId implements IServico {

    /**
     *  
     */
    public ReadAuthorsByPublicationId() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {

        return "ReadAuthorsByPublicationId";
    }

    public List run(Integer publicationId, IUserView userView) throws FenixServiceException {

        List authors = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentPublication persitentPublication = sp.getIPersistentPublication();
            //IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            //IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();
            //IPerson person =
            //	persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            //IAuthor author =
            //	persistentAuthor.readAuthorByKeyPerson(person.getIdInternal());

            IPublication publication = (IPublication) persitentPublication.readByOID(Publication.class,
                    publicationId);

            authors = publication.getAuthors();

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return authors;
    }

}