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

import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Util.SituationName;
import Util.Specialization;

public interface IPersistentMasterDegreeCandidate {
	/**
	 * 
	 * @param username
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
    public List readMasterDegreeCandidatesByUsername(String username) throws ExcepcaoPersistencia;
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
	 * @param idDocumentNumber
	 * @param idDocumentType
	 * @param executionDegree
	 * @param specialization
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(String idDocumentNumber, 
			Integer idDocumentType, ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia;

	
	/**
	 * 
	 * @param username
	 * @param executionDegree
	 * @param specialization
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByUsernameAndExecutionDegreeAndSpecialization(String username, ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param degreeName
	 * @param degreeType
	 * @param candidateSituation
	 * @param candidateNumber
	 * @param executionYear
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readCandidateList(String degreeName, Specialization degreeType, SituationName candidateSituation, Integer candidateNumber,
			IExecutionYear executionYear) throws ExcepcaoPersistencia;


	/**
	 * 
	 * @param executionYear
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia;
	
} // End of class definition
