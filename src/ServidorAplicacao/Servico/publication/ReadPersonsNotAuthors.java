/*
 * Created on Jun 1, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadPersonsNotAuthors implements IServico {

	/**
	 *  
	 */
	public ReadPersonsNotAuthors() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadPersonsNotAuthors";
	}

	public List run(String nameString, IUserView userView) throws FenixServiceException {
		List persons = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			persons = persistentPerson.findPersonByName(nameString);
			persons.remove(person);
			
			return persons;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}
