/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.contributor;

import DataBeans.InfoContributor;
import DataBeans.util.Cloner;
import Dominio.Contributor;
import Dominio.IContributor;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateContributor implements IServico {


	private static CreateContributor servico = new CreateContributor();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateContributor getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateContributor() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "CreateContributor";
	}

	public void run(InfoContributor newContributor) throws Exception{

		IContributor contributor = new Contributor();
		
		ISuportePersistente sp = null;
		
		contributor = Cloner.copyInfoContributor2IContributor(newContributor);

		try {
			sp = SuportePersistenteOJB.getInstance();
			
			sp.getIPersistentContributor().write(contributor);
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
	}
}
