/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import Dominio.GuideSituation;
import Dominio.IGuide;
import Dominio.IGuideSituation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PaymentType;
import Util.SituationOfGuide;
import Util.State;

public class ChangeGuideSituation implements IServico {
    
    private static ChangeGuideSituation servico = new ChangeGuideSituation();
    
    /**
     * The singleton access method of this class.
     **/
    public static ChangeGuideSituation getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ChangeGuideSituation() { 
    }
    
    /**
     * Returns the service name
     **/
    
    public final String getNome() {
        return "ChangeGuideSituation";
    }
    
    
    public void run(Integer guideNumber, Integer guideYear, Integer guideVersion, Date paymentDate, String remarks, String situationOfGuideString, String paymentType) 
	    throws ExcepcaoInexistente, FenixServiceException, ExistingPersistentException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;

		IGuide guide = null;
        try {
	        sp = SuportePersistenteOJB.getInstance();
			guide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);
			
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx; 
        } 

		if (guide == null)
			throw new ExcepcaoInexistente("Unknown Guide !!");	

		SituationOfGuide situationOfGuide = new SituationOfGuide(situationOfGuideString);
		
		// Get the active Situation 
		Iterator iterator = guide.getGuideSituations().iterator();
		IGuideSituation guideSituation = new GuideSituation();
		while(iterator.hasNext()){
			IGuideSituation guideSituationTemp = (IGuideSituation) iterator.next();
			if (guideSituationTemp.getState().equals(new State(State.ACTIVE)))
				guideSituation = guideSituationTemp;
		}
		
				
		// check if the change is valid
		if (verifyChangeValidation(guideSituation, situationOfGuide) == false) {
			throw new NonValidChangeServiceException();
	    }
	    
		sp.getIPersistentGuideSituation().write(guideSituation);
		if (situationOfGuide.equals(guideSituation.getSituation())){
			guideSituation.setRemarks(remarks);
			if (guideSituation.getSituation().equals(SituationOfGuide.PAYED_TYPE)){
				guide.setPaymentDate(paymentDate);
				guide.setPaymentType(new PaymentType(paymentType));
			}
		} else {
			// Create The New Situation
			
			guideSituation.setState(new State(State.INACTIVE));
		
			IGuideSituation newGuideSituation = new GuideSituation();
			Calendar date = Calendar.getInstance();
			newGuideSituation.setDate(date.getTime());
			newGuideSituation.setGuide(guide);
			newGuideSituation.setRemarks(remarks);
			newGuideSituation.setSituation(situationOfGuide);
			newGuideSituation.setState(new State(State.ACTIVE));

			if (situationOfGuide.equals(SituationOfGuide.PAYED_TYPE)){
				sp.getIPersistentGuide().write(guide);
				guide.setPaymentDate(paymentDate);
				guide.setPaymentType(new PaymentType(paymentType));
			}
			
		
			// Write the new Situation
			try {
				sp.getIPersistentGuideSituation().write(newGuideSituation);
			} catch (ExcepcaoPersistencia ex) {
				FenixServiceException newEx = new FenixServiceException("Persistence layer error");
				throw newEx; 
			}

		}
			
		
    }
    
    private boolean verifyChangeValidation(IGuideSituation activeGuideSituation, SituationOfGuide situationOfGuide) {
		if (activeGuideSituation.equals(SituationOfGuide.ANNULLED_TYPE))
			return false; 
			
    	if ((activeGuideSituation.getSituation().equals(SituationOfGuide.PAYED_TYPE)) && (situationOfGuide.equals(SituationOfGuide.NON_PAYED_TYPE)))
			return false;
			
		return true;
    }
    
}