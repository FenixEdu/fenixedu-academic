/*
 * Created on Jun 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import DataBeans.publication.InfoAuthor;
import Dominio.IPerson;
import Dominio.Person;
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
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadAuthorByPersonId implements IServico {

    /**
     *  
     */
    public ReadAuthorByPersonId() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {

        return "ReadAuthorByPersonId";
    }

    public InfoAuthor run(IUserView userView) throws FenixServiceException {
        InfoAuthor author = new InfoAuthor();

        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            IAuthor iauthor = persistentAuthor.readAuthorByKeyPerson(person.getIdInternal());
            author.copyFromDomain(iauthor);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return author;
    }
    
    public InfoAuthor run(String userName) throws FenixServiceException {
        InfoAuthor author = new InfoAuthor();

        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            IPerson person = persistentPerson.lerPessoaPorUsername(userName);
            IAuthor iauthor = persistentAuthor.readAuthorByKeyPerson(person.getIdInternal());
            author.copyFromDomain(iauthor);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return author;
    }
    
    public InfoAuthor run(Integer personId) throws FenixServiceException {
        InfoAuthor author = new InfoAuthor();

        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            IAuthor iauthor = persistentAuthor.readAuthorByKeyPerson(personId);
            author.copyFromDomain(iauthor);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return author;
    }

}