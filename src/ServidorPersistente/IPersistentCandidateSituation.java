/*
 * IPersistentCandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 16:07
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICandidateSituation;
import Dominio.IExecutionDegree;
import Dominio.IMasterDegreeCandidate;

public interface IPersistentCandidateSituation extends IPersistentObject {
    /**
     * 
     * @param masterDegreeCandidate
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public ICandidateSituation readActiveCandidateSituation(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia;

    /**
     * 
     * @param masterDegreeCandidate
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readCandidateSituations(IMasterDegreeCandidate masterDegreeCandidate)
            throws ExcepcaoPersistencia;

    /**
     * 
     * @param executionDegree
     * @param situations
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readActiveSituationsBySituationList(IExecutionDegree executionDegree, List situations)
            throws ExcepcaoPersistencia;

    /**
     * 
     * @param candidateSituation
     * @throws ExcepcaoPersistencia
     */
    public void delete(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia;

    /**
     * 
     * @param executionDegree
     * @return A List of Candidates with Registration status for this Execution
     *         Degree
     */
    public List readCandidateListforRegistration(IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia;

} // End of class definition

