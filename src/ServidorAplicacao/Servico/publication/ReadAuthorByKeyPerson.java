/*
 * Created on Jun 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import Dominio.IPerson;
import Dominio.publication.Author;
import Dominio.publication.IAuthor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentAuthor;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadAuthorByKeyPerson implements IServico {

    /**
     *  
     */
    public ReadAuthorByKeyPerson() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {

        return "ReadAuthorByKeyPerson";
    }

    public IAuthor run(IUserView userView) throws FenixServiceException {
        IAuthor author = new Author();

        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            author = persistentAuthor.readAuthorByKeyPerson(person.getIdInternal());

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return author;
    }

}