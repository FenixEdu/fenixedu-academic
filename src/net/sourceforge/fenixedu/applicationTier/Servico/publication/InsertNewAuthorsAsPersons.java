/*
 * Created on Jun 2, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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