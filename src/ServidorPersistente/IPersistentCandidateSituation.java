/*
 * IPersistentCandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 16:07
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;

public interface IPersistentCandidateSituation {
	/**
	 * 
	 * @param masterDegreeCandidate
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	 public ICandidateSituation readActiveCandidateSituation(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia;

    /**
     * 
     * @param masterDegreeCandidate
     * @return
     * @throws ExcepcaoPersistencia
     */
    public List readCandidateSituations(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @param candidateSituation
     * @throws ExcepcaoPersistencia
     */
    public void writeCandidateSituation(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @param candidateSituation
     * @throws ExcepcaoPersistencia
     */
    public void delete(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia;
    
    /**
     * REMINDER: When we delete a Candidate Situation we must ensure that one stays active
     * @throws ExcepcaoPersistencia
     */
    public void deleteAll() throws ExcepcaoPersistencia;

} // End of class definition



