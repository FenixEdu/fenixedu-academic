/*
 * MasterDegreeCandidateOJB.java
 * 
 * Created on 17 de Outubro de 2002, 11:30
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;

public class MasterDegreeCandidateOJB extends PersistentObjectOJB implements
		IPersistentMasterDegreeCandidate {

	public Integer generateCandidateNumber(String executionYear, String degreeName,
			Specialization specialization) throws ExcepcaoPersistencia {
		int number = 0;

		Criteria crit = new Criteria();
		crit.addEqualTo("executionDegree.executionYear.year", executionYear);
		crit.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla", degreeName);
		crit.addEqualTo("specialization", specialization);

		List list = queryList(MasterDegreeCandidate.class, crit, "candidateNumber", false);
		if (list != null && list.size() > 0) {
			Object result = queryList(MasterDegreeCandidate.class, crit, "candidateNumber", false)
					.get(0);
			if (result != null)
				number = ((MasterDegreeCandidate) result).getCandidateNumber().intValue();
		}

		return ++number;
	}


	/**
	 * Reads all candidates that with certains properties. The properties are
	 * specified by the arguments of this method. If an argument is null, then
	 * the candidate can have any value concerning that argument.
	 * 
	 * @return a list with all candidates that satisfy the conditions specified
	 *         by the non-null arguments.
	 */
	public List readCandidateList(Integer executionDegreeID, Specialization degreeType,
			SituationName candidateSituation, Integer candidateNumber, Integer executionYearID)
			throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();

		if (executionDegreeID == null && degreeType == null && candidateSituation == null
				&& candidateNumber == null) {
			criteria.addEqualTo("executionDegree.keyExecutionYear", executionYearID);
			return queryList(MasterDegreeCandidate.class, criteria);
		}

		if (executionDegreeID != null) {

			criteria.addEqualTo("executionDegree.idInternal", executionDegreeID);
		}

		if (degreeType != null) {
			criteria.addEqualTo("specialization", degreeType);
		}

		if (candidateNumber != null) {
			criteria.addEqualTo("candidateNumber", candidateNumber);
		}

		if (candidateSituation != null) {
			criteria.addEqualTo("situations.situation", candidateSituation.getSituationName());
			criteria.addEqualTo("situations.validation", new Integer(State.ACTIVE));
		}
		return queryList(MasterDegreeCandidate.class, criteria);
	}
	    
} 
