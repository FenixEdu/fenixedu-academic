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

import Util.Specialization;
import Dominio.ICursoExecucao;
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
    public IMasterDegreeCandidate readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(Integer candidateNumber, String applicationYear, String degreeCode, 
    							  Specialization specialization) throws ExcepcaoPersistencia;
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
	 * @param specialization
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public Integer generateCandidateNumber(String executionYear, String degreeName, Specialization specialization) throws ExcepcaoPersistencia; 
	
	/**
	 * 
	 * @param identificationDocumentNumber
	 * @param IdentificationDocumentType
	 * @param executionDegree
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByIdentificationDocNumberAndTypeAndExecutionDegree(String idDocumentNumber, 
			Integer idDocumentType, ICursoExecucao executionDegree) throws ExcepcaoPersistencia;

	
	/**
	 * 
	 * @param username
	 * @param executionDegree
	 * @param specialization
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByUsernameAndExecutionDegreeAndSpecialization(String username, ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia;
	
	
	
} // End of class definition
