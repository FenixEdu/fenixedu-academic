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

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;

public interface IPersistentMasterDegreeCandidate extends IPersistentObject {

	public Integer generateCandidateNumber(String executionYear, String degreeName,
			Specialization specialization) throws ExcepcaoPersistencia;

	public List readCandidateList(Integer executionDegreeID, Specialization degreeType,
			SituationName candidateSituation, Integer candidateNumber, Integer executionYearID)
			throws ExcepcaoPersistencia;


}
