/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.Calendar;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGuide;
import DataBeans.util.Cloner;
import Dominio.Contributor;
import Dominio.Guide;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IGuide;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.NonExistingContributorServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.GuideRequester;
import Util.Specialization;
/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PrepareCreateGuide implements IServico {


	private static PrepareCreateGuide servico = new PrepareCreateGuide();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static PrepareCreateGuide getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private PrepareCreateGuide() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "PrepareCreateGuide";
	}

	public InfoGuide run(String graduationType, InfoExecutionDegree infoExecutionDegree, Integer number, String requesterType, Integer contributorNumber, String contributorName, String contributorAddress ) throws Exception{

		ISuportePersistente sp = null;
		IContributor contributor = null;
		Integer guideNumber = null;
		IMasterDegreeCandidate masterDegreeCandidate = null;
		IGuide guide = new Guide();
				
		//	Read the Contributor
		try {
			sp = SuportePersistenteOJB.getInstance();
			contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		if ((contributor == null) && (contributorAddress == null) && (contributorName == null))
			throw new NonExistingContributorServiceException();
		else if ((contributorAddress != null) && (contributorName != null)) {
			// Create the Contributor
			contributor = new Contributor();
			contributor.setContributorNumber(contributorNumber);
			contributor.setContributorAddress(contributorAddress);
			contributor.setContributorName(contributorName);
			sp.getIPersistentContributor().write(contributor);
		}
		
		
		// Get the Guide Number

		Integer year = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			Calendar calendar = Calendar.getInstance();
			year = new Integer(calendar.get(Calendar.YEAR));
			
			guideNumber = sp.getIPersistentGuide().generateGuideNumber(year);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		//	Check if the Requester is a Candidate
		if (requesterType.equals(GuideRequester.CANDIDATE_STRING)) {
			try {
				ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
				masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readByNumberAndExecutionDegreeAndSpecialization(
						number, executionDegree, new Specialization(graduationType));
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			}
			
			// Check if the Candidate Exists
				
			if (masterDegreeCandidate == null) 
			 throw new NonExistingServiceException("O Candidato", null);
	
			guide.setContributor(contributor);
			guide.setNumber(guideNumber);
			guide.setPerson(masterDegreeCandidate.getPerson());
			guide.setYear(year);
		}
		
		if (requesterType.equals(GuideRequester.STUDENT_STRING)) {
			
			// Fill in the blank
		}


		return Cloner.copyIGuide2InfoGuide(guide);
	}
	
}
