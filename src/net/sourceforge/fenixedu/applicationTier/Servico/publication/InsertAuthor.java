/*
 * Created on Jun 5, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InsertAuthor implements IServico {

    /**
     *  
     */
    public InsertAuthor() {
    }

    public String getNome() {
        return "InsertAuthor";
    }

    public IAuthor run(String name, String organization) throws FenixServiceException {

        IAuthor author = new Author();

        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

            author.setAuthor(name);
            author.setOrganization(organization);

            persistentAuthor.lockWrite(author);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return author;

    }

}