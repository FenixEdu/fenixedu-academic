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

import Dominio.publication.Author;
import Dominio.publication.IAuthor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentAuthor;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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