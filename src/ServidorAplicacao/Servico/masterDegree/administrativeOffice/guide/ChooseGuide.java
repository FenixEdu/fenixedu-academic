/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGuide;
import DataBeans.util.Cloner;
import Dominio.IGuide;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChooseGuide implements IServico {


	private static ChooseGuide servico = new ChooseGuide();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ChooseGuide getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChooseGuide() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ChooseGuide";
	}

	public List run(Integer guideNumber, Integer guideYear) throws Exception {

		ISuportePersistente sp = null;
		List guides = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			guides = sp.getIPersistentGuide().readByNumberAndYear(guideNumber, guideYear);
			
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (guides == null) {
			throw new NonExistingServiceException();		
		}
		
		Iterator iterator = guides.iterator();
		List result = new ArrayList();
		while(iterator.hasNext()){
			IGuide guide = (IGuide) iterator.next();
			result.add(Cloner.copyIGuide2InfoGuide(guide));
		}
		return result;
	}
	
	public InfoGuide run(Integer guideNumber, Integer guideYear, Integer guideVersion) throws Exception {

		ISuportePersistente sp = null;
		InfoGuide infoGuide = null;
		IGuide guide = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			guide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);
			
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (guide == null) {
			throw new NonExistingServiceException();		
		}
		
		return Cloner.copyIGuide2InfoGuide(guide);
	}

	public List run(Integer guideYear) throws Exception {

		ISuportePersistente sp = null;
		List guides = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			guides = sp.getIPersistentGuide().readByYear(guideYear);
			
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (guides == null) {
			throw new NonExistingServiceException();		
		}
		
		Iterator iterator = guides.iterator();
		List result = new ArrayList();
		while(iterator.hasNext()){
			IGuide guide = (IGuide) iterator.next();
			result.add(Cloner.copyIGuide2InfoGuide(guide));
		}
		return result;
	}


}
