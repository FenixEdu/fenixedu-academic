
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAllMasterDegrees implements IServico {

	private static ReadAllMasterDegrees servico = new ReadAllMasterDegrees();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAllMasterDegrees getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadAllMasterDegrees() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadAllMasterDegrees";
	}

	public ArrayList run(TipoCurso degreeType) throws FenixServiceException {

		ISuportePersistente sp = null;
		List result = new ArrayList();
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			// Read the master degrees
			result = sp.getICursoPersistente().readAllByDegreeType(degreeType);

			if(result == null || result.size() == 0){
				throw new NonExistingServiceException();
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 

		ArrayList degrees = new ArrayList();
		Iterator iterator = result.iterator();
		while(iterator.hasNext())
			degrees.add(Cloner.copyIDegree2InfoDegree((ICurso) iterator.next()));
		return degrees;
		
	}
}
