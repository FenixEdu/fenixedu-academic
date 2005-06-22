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

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;

public class MasterDegreeCandidateOJB extends PersistentObjectOJB implements
		IPersistentMasterDegreeCandidate {

	public List readMasterDegreeCandidatesByUsername(String username) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("person.username", username);
		return queryList(MasterDegreeCandidate.class, crit);
	}

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
				number = ((IMasterDegreeCandidate) result).getCandidateNumber().intValue();
		}

		return ++number;
	}

	public IMasterDegreeCandidate readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
			String idDocumentNumber, IDDocumentType idDocumentType, Integer executionDegreeID,
			Specialization specialization) throws ExcepcaoPersistencia {

		Criteria crit = new Criteria();
		crit.addEqualTo("specialization", specialization);
		crit.addEqualTo("executionDegree.idInternal", executionDegreeID);
		crit.addEqualTo("person.numeroDocumentoIdentificacao", idDocumentNumber);
		crit.addEqualTo("person.idDocumentType", idDocumentType);
		return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

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
			criteria.addEqualTo("executionDegree.academicYear", executionYearID);
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

	public IMasterDegreeCandidate readByNumberAndExecutionDegreeAndSpecialization(Integer number,
			Integer executionDegreeID, Specialization specialization) throws ExcepcaoPersistencia {

		IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
				executionDegreeID);

		Criteria crit = new Criteria();
		crit.addEqualTo("specialization", specialization);
		crit.addEqualTo("executionDegree.idInternal", executionDegreeID);
		crit.addEqualTo("candidateNumber", number);
		return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

	}

	public IMasterDegreeCandidate readByExecutionDegreeAndPerson(Integer executionDegreeID,
			Integer personID) throws ExcepcaoPersistencia {

		Criteria crit = new Criteria();
		crit.addEqualTo("person.idInternal", personID);
		crit.addEqualTo("executionDegree.idInternal", executionDegreeID);

		return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

	}

	public IMasterDegreeCandidate readByExecutionDegreeAndPersonAndNumber(Integer executionDegreeID,
			Integer personID, Integer number) throws ExcepcaoPersistencia {

		IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
				executionDegreeID);
		IPerson person = (IPerson) readByOID(Person.class, personID);

		Criteria crit = new Criteria();
		crit.addEqualTo("person.username", person.getUsername());
		crit.addEqualTo("executionDegree.idInternal", executionDegreeID);
		crit.addEqualTo("candidateNumber", number);
		return (IMasterDegreeCandidate) queryObject(MasterDegreeCandidate.class, crit);

	}

	public List readByExecutionDegree(Integer executionDegreeID) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("executionDegree.idInternal", executionDegreeID);
		return queryList(MasterDegreeCandidate.class, crit);

	}

	public List readByDegreeCurricularPlanId(Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("executionDegree.degreeCurricularPlan.idInternal", degreeCurricularPlanId);

		return queryList(MasterDegreeCandidate.class, crit);
	}

	public List readByPersonID(Integer personID) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("person.idInternal", personID);
		return queryList(MasterDegreeCandidate.class, criteria);
	}

    public List readAllCandidatesByDCPlanIDSpecSituationAndIsAssistant(Integer degreeCurricularPlanId, 
    		Specialization specialization, SituationName situation, 
    		Boolean givesClasses) throws ExcepcaoPersistencia {

    	// se nenhum valor e' dado, retorna vazio
        if (degreeCurricularPlanId == null && specialization == null && situation == null
                && givesClasses == null)
            return null;

        Criteria criteria = new Criteria();

        // id do degreecurricularplan
        if (degreeCurricularPlanId != null) {
            criteria.addEqualTo("executionDegree.degreeCurricularPlan.idInternal", degreeCurricularPlanId);
        }

        // specialization
        if (specialization != null) {
            criteria.addEqualTo("specialization", specialization);
        }

        // situacao
        if (situation != null) {
            criteria.addEqualTo("situations.situation", situation.getSituationName());
            criteria.addEqualTo("situations.validation", new Integer(State.ACTIVE));
        }

        // da aulas?
        if (givesClasses != null) {
            criteria.addEqualTo("courseAssistant", givesClasses.booleanValue());
        }
        
        return queryList(MasterDegreeCandidate.class, criteria);
    }
    
} // End of class definition
