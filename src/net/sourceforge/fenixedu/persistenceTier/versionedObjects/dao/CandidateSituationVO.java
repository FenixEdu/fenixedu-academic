/*
 * CandidateSituationOJB.java Created on 1 de Novembro de 2002, 16:13
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateSituation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class CandidateSituationVO extends VersionedObjectsBase implements
		IPersistentCandidateSituation {

	private static final List SITUATION_NAME;

	static {
		SITUATION_NAME = new ArrayList(5);
		SITUATION_NAME.add(new Integer(
				SituationName.ADMITED_CONDICIONAL_CURRICULAR));
		SITUATION_NAME.add(new Integer(
				SituationName.ADMITED_CONDICIONAL_FINALIST));
		SITUATION_NAME
				.add(new Integer(SituationName.ADMITED_CONDICIONAL_OTHER));
		SITUATION_NAME.add(new Integer(SituationName.ADMITIDO));
		SITUATION_NAME.add(new Integer(SituationName.ADMITED_SPECIALIZATION));
	}

	public List readActiveSituationsByExecutionDegreeAndNames(
			Integer executionDegreeID, List<String> situationNames)
			throws ExcepcaoPersistencia {

		final ExecutionDegree executionDegree = (ExecutionDegree) readByOID(
				ExecutionDegree.class, executionDegreeID);

		if (executionDegree != null && executionDegree.getMasterDegreeCandidates() != null) {

			final List result = new ArrayList();
			
			for (final Iterator iterator = executionDegree
					.getMasterDegreeCandidates().iterator(); iterator.hasNext();) {
				final MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) iterator
						.next();

				final List candidateSituations = masterDegreeCandidate
						.getSituations();

				for (final Iterator iterator2 = candidateSituations.iterator(); iterator2
						.hasNext();) {

					final CandidateSituation candidateSituation = (CandidateSituation) iterator2
							.next();

					if (candidateSituation.getValidation().getState() == State.ACTIVE
							&& candidateSituations.contains(candidateSituation
									.getSituation())) {
						result.add(candidateSituation);

					}

				}

			}
			return result;

		}
		return new ArrayList(0);
	}

	public List readCandidateListforRegistration(Integer executionDegreeID)
			throws ExcepcaoPersistencia {
		return readActiveSituationsByExecutionDegreeAndNames(executionDegreeID,
				SITUATION_NAME);

	}

} // End of class definition

