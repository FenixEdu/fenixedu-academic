/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.contributor;

import DataBeans.InfoContributor;
import DataBeans.util.Cloner;
import Dominio.IContributor;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadContributor implements IServico {

	private static ReadContributor servico = new ReadContributor();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadContributor getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadContributor() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadContributor";
	}

	public InfoContributor run(Integer contributorNumber) throws FenixServiceException {

		ISuportePersistente sp = null;
		IContributor contributor = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
		
			// Read the contributor
			
			contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);		
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
		
		if (contributor == null) 
			throw new ExcepcaoInexistente("Unknown Contributor !!");
		
		return Cloner.copyIContributor2InfoContributor(contributor);		
	}
}
