/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IGuide;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationOfGuide;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ListGuidesByState implements IServico {

	private static ListGuidesByState servico = new ListGuidesByState();

	/**
	 * The singleton access method of this class.
	 **/
	public static ListGuidesByState getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ListGuidesByState() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ListGuidesByState";
	}

	public List run(Integer guideYear, SituationOfGuide situationOfGuide) throws Exception {

		ISuportePersistente sp = null;
		List guides = new ArrayList();

		try {
			sp = SuportePersistenteOJB.getInstance();
			guides = sp.getIPersistentGuide().readByYearAndState(guideYear, situationOfGuide);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		Iterator iterator = guides.iterator();
		
		List result = new ArrayList();
		while(iterator.hasNext()) {
			result.add(Cloner.copyIGuide2InfoGuide((IGuide) iterator.next()));
		}
		
		return result;
	}

}
