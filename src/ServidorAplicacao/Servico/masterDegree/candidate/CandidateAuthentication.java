package ServidorAplicacao.Servico.masterDegree.candidate;

/**
 * 
 * The service CandidateAuthentication authenticates a Master Degree
 * Candidate
 *
 * Created on 06 de Dezembro de 2002, 12:47
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.ExcepcaoAutenticacao;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CandidateAuthentication implements IServico {

  private static CandidateAuthentication _servico = new CandidateAuthentication();

  /**
   * The singleton access method of this class.
   **/
  public static CandidateAuthentication getService() {
    return _servico;
  }

  /**
   * The ctor of this class.
   **/
  private CandidateAuthentication() {
  }

  /**
   * Returns the service name */
  public final String getNome() {
    return "CandidateAuthentication";
  }

// FIXME: Nuno Nunes Delete ME

  public IUserView run(String username, String password)
      throws FenixServiceException, ExcepcaoAutenticacao {
	    IMasterDegreeCandidate masterDegreeCandidate = null;

    try {
      masterDegreeCandidate = SuportePersistenteOJB.getInstance().getIPersistentMasterDegreeCandidate().readMasterDegreeCandidateByUsername(username);
    } catch (ExcepcaoPersistencia ex) {
      throw new FenixServiceException(ex.getMessage());
    }

    if (masterDegreeCandidate != null && masterDegreeCandidate.getPerson().getPassword().equals(password)) {
      return new UserView(masterDegreeCandidate.getPerson().getUsername(), null);
    } else    
      throw new ExcepcaoAutenticacao("Incorrect Authentication !!");
  }
}