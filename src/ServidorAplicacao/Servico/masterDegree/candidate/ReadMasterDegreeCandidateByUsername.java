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

import DataBeans.InfoMasterDegreeCandidate;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.NotExecutedException;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
	    throws ExcepcaoInexistente, NotExecutedException {

        ISuportePersistente sp = null;
        
        String username = new String(userView.getUtilizador());
        IMasterDegreeCandidate masterDegreeCandidate = null;
         
        try {
            sp = SuportePersistenteOJB.getInstance();
            masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidateByUsername(username);
        } catch (ExcepcaoPersistencia ex) {
            NotExecutedException newEx = new NotExecutedException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

		if (masterDegreeCandidate == null)
			throw new ExcepcaoInexistente("Unknown Candidate !!");	
			
		
		return new InfoMasterDegreeCandidate(masterDegreeCandidate);
    }
}