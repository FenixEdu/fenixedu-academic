/*
 * Created on May 31, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import java.util.List;

import Dominio.IPessoa;
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
public class ReadAuthors implements IServico {
    public ReadAuthors() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadAuthors";
    }

    public List run(String stringtoSearch, IUserView userView) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();
            IAuthor author = persistentAuthor.readAuthorByKeyPerson(person.getIdInternal());

            String names[] = stringtoSearch.split(" ");
            StringBuffer authorName = new StringBuffer(names[0]);
            for (int iter = 1; iter <= names.length - 1; iter++) {
                authorName.append("%");
                authorName.append(names[iter]);
            }
            List authors = persistentAuthor.readAuthorsBySubName(authorName.toString());

            authors.remove(author);
            return authors;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}