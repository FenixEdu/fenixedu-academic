/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.util.Cloner;
import Dominio.IContributor;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CalculateGuideTotal;
import Util.SituationOfGuide;


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
				
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				newEx.fillInStackTrace();
				throw newEx;
			}
			
			infoGuide.setInfoContributor(Cloner.copyIContributor2InfoContributor(contributor));
		}

		// Check the quantities of the Guide Entries
		
		Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
		List newInfoGuideEntries = new ArrayList();
		List guideEntriesToRemove = new ArrayList();

		int quantityListIndex = 0;		
		while(iterator.hasNext()){
			InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();
			if ((quantityList[quantityListIndex] == null) || (quantityList[quantityListIndex].equals("0"))) {
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
			
		} else if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.ANNULLED_TYPE)){
			throw new InvalidChangeServiceException();
		}
		
		
		InfoGuide newInfoGuide = null;
		IGuide newGuide = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			// Make sure that it writes before reading ...
			sp.confirmarTransaccao();
			sp.iniciarTransaccao();

			newGuide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(infoGuide.getNumber(), infoGuide.getYear(), infoGuide.getVersion());

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		
		InfoGuide infoGuideTemp = new InfoGuide();
		infoGuideTemp.setInfoGuideEntries(newInfoGuideEntries);

		InfoGuide result = Cloner.copyIGuide2InfoGuide(newGuide); 
		result.setTotal(CalculateGuideTotal.calculate(infoGuideTemp));

		guide.setTotal(result.getTotal());
		return result;


	}
	
}
