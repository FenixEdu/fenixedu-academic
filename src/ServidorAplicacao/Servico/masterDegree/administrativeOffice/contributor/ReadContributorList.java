/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoContributor;
import DataBeans.util.Cloner;
import Dominio.IContributor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadContributorList implements IServico {

	private static ReadContributorList servico = new ReadContributorList();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadContributorList getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadContributorList() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadContributorList";
	}

	public List run(Integer contributorNumber) throws FenixServiceException {

		ISuportePersistente sp = null;
		List contributors = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
		
			// Read the contributor
			
			contributors = sp.getIPersistentContributor().readContributorListByNumber(contributorNumber);		
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
		
		if (contributors == null) 
			throw new ExcepcaoInexistente("No Contributors Found !!");
		
		List result = new ArrayList();
		Iterator iterator = contributors.iterator();
		while(iterator.hasNext()) {
			InfoContributor infoContributor = null;
			infoContributor = Cloner.copyIContributor2InfoContributor((IContributor) iterator.next());
			result.add(infoContributor);
		}
		
		return result;		
	}
}
