/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoGuideSituation;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IGuideSituation;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CalculateGuideTotal;
import Util.DocumentType;
import Util.GraduationType;
import Util.PaymentType;
import Util.SituationOfGuide;
import Util.State;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateGuide implements IServico {


	private static CreateGuide servico = new CreateGuide();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateGuide getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateGuide() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "CreateGuide";
	}

	public InfoGuide run(InfoGuide infoGuide, String othersRemarks, Double othersPrice, String remarks,
		 SituationOfGuide situationOfGuide, Integer paymentTypeInteger) throws Exception {

		ISuportePersistente sp = null;
		IContributor contributor = null;
		IMasterDegreeCandidate masterDegreeCandidate = null;
		IGuide guide = new Guide();
		IGuideSituation guideSituation = null;
		IPessoa person = null;		


		// Check the Guide Situation
		if (situationOfGuide.equals(SituationOfGuide.ANNULLED_TYPE))
			throw new InvalidSituationServiceException();

		InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
		if (othersPrice != null){
			infoGuideEntry.setDescription(othersRemarks);
			infoGuideEntry.setPrice(othersPrice);
			infoGuideEntry.setInfoGuide(infoGuide);
			infoGuideEntry.setDocumentType(DocumentType.OTHERS_TYPE);
			infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
			infoGuideEntry.setQuantity(new Integer(1));
			List entries = infoGuide.getInfoGuideEntries();
			entries.add(infoGuideEntry);
			infoGuide.setInfoGuideEntries(entries);
		}
			
		// Calculate the Guide Total Price
		
		infoGuide.setTotal(CalculateGuideTotal.calculate(infoGuide));
		
		// Get the Guide Number
		
		Integer guideNumber = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			guideNumber = sp.getIPersistentGuide().generateGuideNumber(infoGuide.getYear());
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		infoGuide.setNumber(guideNumber);
		
		// Create the new Guide Situation
		InfoGuideSituation infoGuideSituation = new InfoGuideSituation();
		infoGuideSituation.setState(new State(State.ACTIVE));
		infoGuideSituation.setRemarks(remarks);
		infoGuideSituation.setInfoGuide(infoGuide);
		
		Calendar calendar = Calendar.getInstance();
		infoGuideSituation.setDate(calendar.getTime());
		infoGuideSituation.setSituation(situationOfGuide);
		
		guide = Cloner.copyInfoGuide2IGuide(infoGuide);

		if (situationOfGuide.equals(SituationOfGuide.PAYED_TYPE))
			guide.setPaymentType(new PaymentType(paymentTypeInteger));
		

		//FIXME: Nuno Nunes
		guide.setGuideEntries(null);
		try {
			sp = SuportePersistenteOJB.getInstance();

			// Get the Execution Degree
			ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
						infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getSigla(),
						infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(),
						Cloner.copyInfoExecutionYear2IExecutionYear(infoGuide.getInfoExecutionDegree().getInfoExecutionYear()));

			contributor = sp.getIPersistentContributor().readByContributorNumber(infoGuide.getInfoContributor().getContributorNumber());
			person = sp.getIPessoaPersistente().lerPessoaPorUsername(infoGuide.getInfoPerson().getUsername());


			guide.setExecutionDegree(executionDegree);
			guide.setContributor(contributor);
			guide.setPerson(person);
			
			// Write the new Guide
			sp.getIPersistentGuide().write(guide);
			
			// Write the new Entries of the Guide
			Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
			List guideEntries = new ArrayList();
			while(iterator.hasNext()){
				IGuideEntry guideEntry = Cloner.copyInfoGuideEntry2IGuideEntry((InfoGuideEntry) iterator.next());
				guideEntries.add(guideEntry);

				guideEntry.setGuide(guide);
				sp.getIPersistentGuideEntry().write(guideEntry);
			}
			
			// Write the New Guide Situation
			guideSituation = Cloner.copyInfoGuideSituation2IGuideSituation(infoGuideSituation);
			guideSituation.setGuide(guide);
			sp.getIPersistentGuideSituation().write(guideSituation);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		InfoGuide result = Cloner.copyIGuide2InfoGuide(guide); 
		result.setInfoGuideEntries(infoGuide.getInfoGuideEntries());

		return result;
	}
	
}
