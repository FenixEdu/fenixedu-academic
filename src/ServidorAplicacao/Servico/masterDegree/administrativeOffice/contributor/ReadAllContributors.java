/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IContributor;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAllContributors implements IServico {

	private static ReadAllContributors servico = new ReadAllContributors();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAllContributors getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadAllContributors() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadAllContributors";
	}

	public List run() throws FenixServiceException {

		ISuportePersistente sp = null;
		List result = new ArrayList();
		try {
			sp = SuportePersistenteOJB.getInstance();
		
			// Read the contributors
			
			result = sp.getIPersistentContributor().readAll();		
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 

		List contributors = new ArrayList();
		Iterator iterator = result.iterator();
		while(iterator.hasNext())
			contributors.add(Cloner.copyIContributor2InfoContributor((IContributor) iterator.next()));

		return contributors;
		
	}
}
