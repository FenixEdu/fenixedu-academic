/*
 * IPersistentMasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 11:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
package ServidorPersistente;

import Dominio.IMasterDegreeCandidate;

public interface IPersistentMasterDegreeCandidate {
	/**
	 * 
	 * @param username
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
    public IMasterDegreeCandidate readMasterDegreeCandidateByUsername(String username) throws ExcepcaoPersistencia;
    /**
     * 
     * @param candidateNumber
     * @param applicationYear
     * @param degreeCode
     * @return
     * @throws ExcepcaoPersistencia
     */
    public IMasterDegreeCandidate readMasterDegreeCandidateByNumberAndApplicationYearAndDegreeCode(Integer candidateNumber, String applicationYear, String degreeCode) throws ExcepcaoPersistencia;
    /**
     * 
     * @param masterDegreeCandidate
     * @throws ExcepcaoPersistencia
     */
    public void writeMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia;
    /**
     * 
     * @param masterDegreeCandidate
     * @throws ExcepcaoPersistencia
     */
    public void delete(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia;
    /**
     * 
     * @throws ExcepcaoPersistencia
     */
    public void deleteAll() throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param executionYear
	 * @param degreeName
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public Integer generateCandidateNumber(String executionYear, String degreeName) throws ExcepcaoPersistencia; 

} // End of class definition
