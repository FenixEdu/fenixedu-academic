/*
 * ReadMasterDegreeCandidateByUsername.java
 *
 * The Service ReadMasterDegreeCandidateByUsername reads the information of a
 * Candidate and returns it
 * 
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.masterDegree.candidate;

import java.util.Iterator;

import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CandidateSituationValidation;

public class ReadMasterDegreeCandidateByUsername implements IServico {
    
    private static ReadMasterDegreeCandidateByUsername servico = new ReadMasterDegreeCandidateByUsername();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadMasterDegreeCandidateByUsername getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadMasterDegreeCandidateByUsername() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "ReadMasterDegreeCandidateByUsername";
    }
    
    
    public Object run(UserView userView)
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        
        String username = new String(userView.getUtilizador());
        IMasterDegreeCandidate masterDegreeCandidate = null;
         
        try {
            sp = SuportePersistenteOJB.getInstance();
            masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidateByUsername(username);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

		if (masterDegreeCandidate == null)
			throw new ExcepcaoInexistente("Unknown Candidate !!");	
			
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
		
		// Search the candidate's active situation
		Iterator iterator = masterDegreeCandidate.getSituations().iterator();

		while(iterator.hasNext()){
			ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator.next();
			
			if ((candidateSituationTemp.getValidation().getCandidateSituationValidation()).equals(new Integer(CandidateSituationValidation.ACTIVE))) {
				infoMasterDegreeCandidate.setInfoCandidateSituation(new InfoCandidateSituation(candidateSituationTemp.getDate(),
										 							candidateSituationTemp.getRemarks(),
										  							candidateSituationTemp.getSituation().toString()));
			}
		}
		
		return infoMasterDegreeCandidate;
    }
}