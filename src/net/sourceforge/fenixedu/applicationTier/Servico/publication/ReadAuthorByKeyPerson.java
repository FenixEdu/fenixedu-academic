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
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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