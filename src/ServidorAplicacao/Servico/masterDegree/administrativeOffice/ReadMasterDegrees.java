/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadMasterDegrees implements IServico {

	private static ReadMasterDegrees servico = new ReadMasterDegrees();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadMasterDegrees getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadMasterDegrees() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadMasterDegrees";
	}

	public ArrayList run() throws FenixServiceException {

		ISuportePersistente sp = null;
		List result = new ArrayList();
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			// Get the Actual Execution Year
			IExecutionYear executionYear = null;

			executionYear = sp.getIPersistentExecutionYear().readActualExecutionYear();
			
			// Read the degrees
			
			result = sp.getICursoExecucaoPersistente().readMasterDegrees(executionYear.getYear());		
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 

		ArrayList degrees = new ArrayList();
		Iterator iterator = result.iterator();
		while(iterator.hasNext())
			degrees.add(Cloner.copyIExecutionDegree2InfoExecutionDegree((ICursoExecucao) iterator.next()));

		return degrees;
		
	}
}
