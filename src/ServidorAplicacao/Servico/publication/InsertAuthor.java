/*
 * Created on Jun 5, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

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
public class InsertAuthor implements IServico {

	/**
	 *  
	 */
	public InsertAuthor() {
	}

	public String getNome() {
		return "InsertAuthor";
	}

	public IAuthor run(String name, String organisation)
		throws FenixServiceException {

		IAuthor author = new Author();

		ISuportePersistente sp;
		try {
			sp = SuportePersistenteOJB.getInstance();

			IPersistentAuthor persistentAuthor = sp.getIPersistentAuthor();

			author.setAuthor(name);
			author.setOrganisation(organisation);
			
			persistentAuthor.lockWrite(author);
			
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return author;

	}

}
