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
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.GuideSituation;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IGuideSituation;
import Dominio.IPessoa;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CalculateGuideTotal;
import Util.SituationOfGuide;
import Util.State;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class EditGuideInformation implements IServico {


	private static EditGuideInformation servico = new EditGuideInformation();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static EditGuideInformation getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditGuideInformation() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "EditGuideInformation";
	}

	public InfoGuide run(InfoGuide infoGuide, String[] quantityList, Integer contributorNumber) throws Exception {

		ISuportePersistente sp = null;

		// check if it's needed to change the Contributor
		IContributor contributor = null;
		IGuide guide = null;
		
		
		// Safety check to see if the Guide can be changed
		this.chekIfChangeable(infoGuide);
		

		// Read The Guide		
		try {
			sp = SuportePersistenteOJB.getInstance();
			guide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(infoGuide.getNumber(), infoGuide.getYear(), infoGuide.getVersion());
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		
		if ((contributorNumber != null) && (!infoGuide.getInfoContributor().getContributorNumber().equals(contributorNumber))){
			// Read the new Contributor
			try {
				sp = SuportePersistenteOJB.getInstance();
				
				contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);
				guide.setContributor(contributor);
				
				// Make sure that vaerithing is written before reading ...
				sp.confirmarTransaccao();
				sp.iniciarTransaccao();

				
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			}
			
			
			infoGuide.setInfoContributor(Cloner.copyIContributor2InfoContributor(contributor));
		}

		// Check the quantities of the Guide Entries
		// The items without a quantity or with a 0 quantity will be deleted if the guide is NON PAYED or
		// they won't appear in the new guide version if the guide has been payed 
		
		Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
		List newInfoGuideEntries = new ArrayList();
		List guideEntriesToRemove = new ArrayList();

		int quantityListIndex = 0;		
		while(iterator.hasNext()){
			InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();
			if ((quantityList[quantityListIndex] == null) || (quantityList[quantityListIndex].length() == 0) ||	
				(quantityList[quantityListIndex].equals("0"))) {
				// Add to items to remove
				guideEntriesToRemove.add(infoGuideEntry);
			} else {
				infoGuideEntry.setQuantity(new Integer(quantityList[quantityListIndex]));
				newInfoGuideEntries.add(infoGuideEntry);
			}
			quantityListIndex++;
		}

		
		if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.NON_PAYED_TYPE)){
			// Remove the Guide entries wich have been deleted
			Iterator entryIterator = guideEntriesToRemove.iterator();
			while(entryIterator.hasNext()){
				InfoGuideEntry infoGuideEntry = (InfoGuideEntry) entryIterator.next();
				try {
					sp = SuportePersistenteOJB.getInstance();
					IGuideEntry guideEntry = sp.getIPersistentGuideEntry().readByGuideAndGraduationTypeAndDocumentTypeAndDescription(
									guide, infoGuideEntry.getGraduationType(), infoGuideEntry.getDocumentType(), infoGuideEntry.getDescription());
					sp.getIPersistentGuideEntry().delete(guideEntry);
				} catch (ExcepcaoPersistencia ex) {
					FenixServiceException newEx = new FenixServiceException("Persistence layer error");
					newEx.fillInStackTrace();
					throw newEx;
				}
			}
			
			// Update the remaing guide entries
			entryIterator = newInfoGuideEntries.iterator();
			while(entryIterator.hasNext()){
				InfoGuideEntry infoGuideEntry = (InfoGuideEntry) entryIterator.next();
				try {
					sp = SuportePersistenteOJB.getInstance();
					IGuideEntry guideEntry = sp.getIPersistentGuideEntry().readByGuideAndGraduationTypeAndDocumentTypeAndDescription(
									guide, infoGuideEntry.getGraduationType(), infoGuideEntry.getDocumentType(), infoGuideEntry.getDescription());
					guideEntry.setQuantity(infoGuideEntry.getQuantity());
					
				} catch (ExcepcaoPersistencia ex) {
					FenixServiceException newEx = new FenixServiceException("Persistence layer error");
					newEx.fillInStackTrace();
					throw newEx;
				}
			}
						
		} else if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.PAYED_TYPE)){
			// Create a new Guide Version
			IGuide newGuideVersion = null;
			newGuideVersion = this.createNewGuideVersion(infoGuide);
			
			// Create The new Situation			
			IGuideSituation guideSituation = new GuideSituation();
			guideSituation.setDate(infoGuide.getInfoGuideSituation().getDate());
			guideSituation.setGuide(newGuideVersion);
			guideSituation.setRemarks(infoGuide.getRemarks());
			guideSituation.setSituation(infoGuide.getInfoGuideSituation().getSituation());
			guideSituation.setState(new State(State.ACTIVE));
			


			// Write the new Guide Version			
			try {
				sp = SuportePersistenteOJB.getInstance();

				sp.getIPersistentGuide().write(newGuideVersion);
				sp.getIPersistentGuideSituation().write(guideSituation);
				// Make sure that everything is written before reading ...
				sp.confirmarTransaccao();
				sp.iniciarTransaccao();
				
				// Write the Guide Entries
				Iterator guideEntryIterator = newInfoGuideEntries.iterator();
				while(guideEntryIterator.hasNext()) {
			
					IGuideEntry guideEntry = Cloner.copyInfoGuideEntry2IGuideEntry((InfoGuideEntry) guideEntryIterator.next());

					guideEntry.setGuide(newGuideVersion);
					sp.getIPersistentGuideEntry().write(guideEntry);
				}

				// Update the version number for the next Database Access
				infoGuide.setVersion(newGuideVersion.getVersion());
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			}
			
		}
		
		InfoGuide newInfoGuide = null;
		IGuide newGuide = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			// Make sure that vaerithing is written before reading ...
			sp.confirmarTransaccao();
			sp.iniciarTransaccao();

			newGuide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(infoGuide.getNumber(), infoGuide.getYear(), infoGuide.getVersion());

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		
		// Update the Guide Total
		InfoGuide infoGuideTemp = new InfoGuide();
		infoGuideTemp.setInfoGuideEntries(newInfoGuideEntries);

		InfoGuide result = Cloner.copyIGuide2InfoGuide(newGuide); 
		result.setTotal(CalculateGuideTotal.calculate(infoGuideTemp));

		newGuide.setTotal(result.getTotal());
		
		return result;


	}
	
	private void chekIfChangeable(InfoGuide infoGuide) throws FenixServiceException{
		SuportePersistenteOJB sp = null;		
		List guides = null;

		// Annuled Guides cannot be changed		
		if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.ANNULLED_TYPE))
			throw new InvalidChangeServiceException("Situation of Guide Is Annulled");
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			guides = sp.getIPersistentGuide().readByNumberAndYear(infoGuide.getNumber(), infoGuide.getYear());
			
		} catch (ExcepcaoPersistencia e) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

	
		// If it's not the latest version ...	
		if (guides.size() != infoGuide.getVersion().intValue())
			throw new InvalidChangeServiceException("Not the Latest Version");
	}
	
	private IGuide createNewGuideVersion(InfoGuide infoGuide) throws FenixServiceException{
		IGuide guide = new Guide();
		SuportePersistenteOJB sp = null;
		IContributor contributor = null;
		IPessoa person = null;
		ICursoExecucao executionDegree = null;
		
		// Read the needed information from the DataBase
		try {
			sp = SuportePersistenteOJB.getInstance();
			person = sp.getIPessoaPersistente().lerPessoaPorUsername(infoGuide.getInfoPerson().getUsername());
			contributor = sp.getIPersistentContributor().readByContributorNumber(infoGuide.getInfoContributor().getContributorNumber());
			IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(infoGuide.getInfoExecutionDegree().getInfoExecutionYear().getYear());
			
			executionDegree = sp.getICursoExecucaoPersistente().readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
								infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getSigla(),
								infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(),
								executionYear);
			
		} catch (ExcepcaoPersistencia e) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		// Set the fields
		guide.setContributor(contributor);
		guide.setPerson(person);
		guide.setExecutionDegree(executionDegree);
		
		guide.setCreationDate(Calendar.getInstance().getTime());
		guide.setGuideRequester(infoGuide.getGuideRequester());
		guide.setNumber(infoGuide.getNumber());
		guide.setYear(infoGuide.getYear());
		guide.setVersion(new Integer(infoGuide.getVersion().intValue() + 1));
		guide.setPaymentDate(infoGuide.getPaymentDate());
		guide.setPaymentType(infoGuide.getPaymentType());
		guide.setRemarks(null);
		
		// The total will be calculated afterwards
		guide.setTotal(new Double(0));
		
		return guide;
	}
	
}
	
	
	
		
