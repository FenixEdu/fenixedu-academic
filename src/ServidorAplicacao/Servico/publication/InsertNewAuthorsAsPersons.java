/*
 * Created on Jun 2, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.IPerson;
import Dominio.Person;
import Dominio.publication.Author;
import Dominio.publication.IAuthor;
import ServidorAplicacao.IServico;
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
public class InsertNewAuthorsAsPersons implements IServico {

    /**
     *  
     */
    public InsertNewAuthorsAsPersons() {
    }

    public String getNome() {
        return "InsertNewAuthorsAsPersons";
    }

    public List run(List idsInternalPersons) throws FenixServiceException {
        List authors = new ArrayList();
        try {
            Iterator iterator = idsInternalPersons.iterator();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            while (iterator.hasNext()) {
                Integer idInternal = (Integer) iterator.next();
                IPerson person = (IPerson) persistentPerson.readByOID(Person.class, idInternal);
                IAuthor newAuthor = new Author();
                IAuthor newAuthor1 = persistentAuthor.readAuthorByKeyPerson(idInternal);
                if (newAuthor1 == null) {
                    newAuthor.setKeyPerson(idInternal);
                    newAuthor.setPerson(person);
                    persistentAuthor.lockWrite(newAuthor);
                    authors.add(newAuthor);
                } else {
                    authors.add(newAuthor1);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return authors;

    }
}