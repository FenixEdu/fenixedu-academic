/*
 * Created on Jun 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            IAuthor iauthor = persistentAuthor.readAuthorByKeyPerson(personId);
            author.copyFromDomain(iauthor);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return author;
    }

}