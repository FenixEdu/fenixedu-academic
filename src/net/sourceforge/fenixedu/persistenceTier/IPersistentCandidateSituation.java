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
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

public interface IPersistentCandidateSituation extends IPersistentObject {

	/**
	 * @param executionDegree
	 * @param situations
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readActiveSituationsByExecutionDegreeAndNames(Integer executionDegreeID,
			List<String> situationNames) throws ExcepcaoPersistencia;

	/**
	 * @param executionDegree
	 * @return A List of Candidates with Registration status for this Execution
	 *         Degree
	 */
	public List readCandidateListforRegistration(Integer executionDegreeID)
			throws ExcepcaoPersistencia;

}

