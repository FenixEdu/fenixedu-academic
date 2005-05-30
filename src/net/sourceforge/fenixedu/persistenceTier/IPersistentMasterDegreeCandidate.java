/*
 * IPersistentMasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 11:25
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;

public interface IPersistentMasterDegreeCandidate extends IPersistentObject {
	/**
	 * @param username
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readMasterDegreeCandidatesByUsername(String username) throws ExcepcaoPersistencia;

	/**
	 * @param executionYear
	 * @param degreeName
	 * @param specialization
	 * @return Candidate Number
	 * @throws ExcepcaoPersistencia
	 */
	public Integer generateCandidateNumber(String executionYear, String degreeName,
			Specialization specialization) throws ExcepcaoPersistencia;

	/**
	 * @param idDocumentNumber
	 * @param idDocumentType
	 * @param executionDegree
	 * @param specialization
	 * @return IMasterDegreeCandidate
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
			String idDocumentNumber, IDDocumentType idDocumentType, Integer executionDegreeID,
			Specialization specialization) throws ExcepcaoPersistencia;

	/**
	 * @param degreeName
	 * @param degreeType
	 * @param candidateSituation
	 * @param candidateNumber
	 * @param executionYear
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readCandidateList(Integer executionDegreeID, Specialization degreeType,
			SituationName candidateSituation, Integer candidateNumber, Integer executionYearID)
			throws ExcepcaoPersistencia;


	/**
	 * @param number
	 * @param executionDegree
	 * @param specialization
	 * @return IMasterDegreeCandidate
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByNumberAndExecutionDegreeAndSpecialization(Integer number,
			Integer executionDegreeID, Specialization specialization) throws ExcepcaoPersistencia;

	/**
	 * @param executionDegree
	 * @param person
	 * @return IMasterDegreeCandidate
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByExecutionDegreeAndPerson(Integer executionDegreeID,
			Integer personID) throws ExcepcaoPersistencia;

	/**
	 * @param executionDegree
	 * @param person
	 * @param number
	 * @return IMasterDegreeCandidate
	 * @throws ExcepcaoPersistencia
	 */
	public IMasterDegreeCandidate readByExecutionDegreeAndPersonAndNumber(Integer executionDegreeID,
			Integer personID, Integer number) throws ExcepcaoPersistencia;

	/**
	 * @param executionDegree
	 * @return List
	 * @throws ExcepcaoPersistencia
	 */
	public List readByExecutionDegree(Integer executionDegreeID) throws ExcepcaoPersistencia;

	/**
	 * @param degreeCurricularPlanId
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readByDegreeCurricularPlanId(Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

	/**
	 * @param personID
	 * @return The Master degree candidate's for this person
	 * @throws ExcepcaoPersistencia
	 */
	public List readByPersonID(Integer personID) throws ExcepcaoPersistencia;

} // End of class definition
