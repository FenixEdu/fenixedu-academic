/*
 * Created on 13/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.candidate;

import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangeCandidatePassword implements IServico {
	
	private static ChangeCandidatePassword servico = new ChangeCandidatePassword();
    
		/**
		 * The singleton access method of this class.
		 **/
		public static ChangeCandidatePassword getService() {
			return servico;
		}
    
		/**
		 * The actor of this class.
		 **/
		private ChangeCandidatePassword() { 
		}
    
		/**
		 * Returns The Service Name */
    
		public final String getNome() {
			return "ChangeCandidatePassword";
		}
    
		public void run(UserView userView, String oldPassword, String newPassword)
			throws ExcepcaoInexistente, FenixServiceException , InvalidPasswordServiceException {

			// Check if the old password is equal
			
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
			if (!masterDegreeCandidate.getPerson().getPassword().equals(oldPassword))	
				throw new InvalidPasswordServiceException("Invalid Existing Password !!");

			// Change the Password
			masterDegreeCandidate.getPerson().setPassword(newPassword);

			}
}
