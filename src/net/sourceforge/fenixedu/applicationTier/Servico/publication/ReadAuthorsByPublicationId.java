/*
 * Created on Jun 7, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;

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