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
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadAuthorsToInsert implements IServico {

    /**
     *  
     */
    public ReadAuthorsToInsert() {

    }

    public String getNome() {
        return "ReadAuthorsToInsert";
    }

    public List run(List idsInternalAuthors) throws FenixServiceException {
        List authors = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            Iterator iterator = idsInternalAuthors.iterator();

            while (iterator.hasNext()) {
                Integer idInternal = (Integer) iterator.next();
                IAuthor author = (IAuthor) persistentAuthor.readByOID(Author.class, idInternal);
                authors.add(author);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return authors;
    }

}