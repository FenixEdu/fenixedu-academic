/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.contributor;

import DataBeans.InfoContributor;
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
public class EditContributor implements IServico {


	private static EditContributor servico = new EditContributor();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static EditContributor getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditContributor() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "EditContributor";
	}

	public void run(InfoContributor infoContributor, Integer contributorNumber, String contributorName, String contributorAddress) throws Exception{

		IContributor contributorBD = new Contributor();
		
		ISuportePersistente sp = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			// Read the Actual Contributor
			contributorBD = sp.getIPersistentContributor().readByContributorNumber(infoContributor.getContributorNumber());
			contributorBD.setContributorNumber(contributorNumber);
			contributorBD.setContributorName(contributorName);
			contributorBD.setContributorAddress(contributorAddress);
				
			sp.getIPersistentContributor().write(contributorBD);
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
	}
}
