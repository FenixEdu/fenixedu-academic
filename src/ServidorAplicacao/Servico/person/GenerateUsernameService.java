/*
 * Created on 18/Mar/2003
 *
 */
package ServidorAplicacao.Servico.person;

import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.NotExecutedException;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RandomStringGenerator;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GenerateUsernameService implements IServico {
    
	private static GenerateUsernameService servico = new GenerateUsernameService();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static GenerateUsernameService getService() {
		return servico;
	}
    
	/**
	 * The actor of this class.
	 **/
	private GenerateUsernameService() { 
	}
    
	/**
	 * Devolve o nome do service
	 **/
	public final String getNome() {
		return "GenerateUsername";
	}
    
    
	public String run()
		throws NotExecutedException, ExcepcaoInexistente  {

		ISuportePersistente sp = null;
        
		// Generate Username
		String username = RandomStringGenerator.getRandomStringGenerator(6);

		// Verify if the Username already Exists 
		try {
			sp = SuportePersistenteOJB.getInstance();
            IPessoa person = null;
			while ((person = sp.getIPessoaPersistente().lerPessoaPorUsername(username)) != null)
				username = RandomStringGenerator.getRandomStringGenerator(6);
		} catch (ExcepcaoPersistencia ex) {
			NotExecutedException newEx = new NotExecutedException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		return username;
	}

}
