/*
 * CandidateSituationOJB.java Created on 1 de Novembro de 2002, 16:13
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateSituation;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;

public class CandidateSituationOJB extends PersistentObjectOJB implements
		IPersistentCandidateSituation {

	public List readActiveSituationsByExecutionDegreeAndNames(
			Integer executionDegreeID, List<String> situationNames)
			throws ExcepcaoPersistencia {

		Criteria criteria = new Criteria();
		Criteria criteriaSituations = new Criteria();
		criteria.addEqualTo("validation", new State(State.ACTIVE));
		criteria.addEqualTo("masterDegreeCandidate.executionDegree.idInternal",
				executionDegreeID);

		if ((situationNames != null) && (situationNames.size() != 0)) {
			criteriaSituations.addIn("situation", situationNames);
			criteria.addAndCriteria(criteriaSituations);
		}

		return queryList(CandidateSituation.class, criteria);
	}

	public List readCandidateListforRegistration(Integer executionDegreeID)
			throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		Criteria criteriaDocs = new Criteria();
		criteria.addEqualTo("validation", new State(State.ACTIVE));
		criteria.addEqualTo("masterDegreeCandidate.executionDegree.idInternal",
				executionDegreeID);
		List situations = new ArrayList();

		situations
				.add(new Integer(SituationName.ADMITED_CONDICIONAL_CURRICULAR));
		situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_FINALIST));
		situations.add(new Integer(SituationName.ADMITED_CONDICIONAL_OTHER));
		situations.add(new Integer(SituationName.ADMITIDO));
		situations.add(new Integer(SituationName.ADMITED_SPECIALIZATION));

		criteriaDocs.addIn("situation", situations);
		criteria.addAndCriteria(criteriaDocs);

		List result = queryList(CandidateSituation.class, criteria);
		if ((result == null) || (result.size() == 0)) {
			return null;
		}
		return result;

	}

} // End of class definition

