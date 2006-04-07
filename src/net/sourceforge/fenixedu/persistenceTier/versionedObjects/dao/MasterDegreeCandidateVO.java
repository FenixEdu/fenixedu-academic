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

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.SituationName;

public class MasterDegreeCandidateVO extends VersionedObjectsBase implements
		IPersistentMasterDegreeCandidate {

    public Integer generateCandidateNumber(final String executionYear, final String degreeName,
			final Specialization specialization) throws ExcepcaoPersistencia {

		final List<MasterDegreeCandidate> masterDegreeCandidateList = (List<MasterDegreeCandidate>) readAll(MasterDegreeCandidate.class);
		int number = 0;

		for (final MasterDegreeCandidate masterDegreeCandidate : masterDegreeCandidateList) {
			if (masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear().equals(
					executionYear)
					&& masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getDegree()
							.getNome().equals(degreeName)) {
				number = Math.max(number, masterDegreeCandidate.getCandidateNumber());
			}
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
	public List<MasterDegreeCandidate> readCandidateList(final Integer executionDegreeID,
			final Specialization specialization, final SituationName situationName,
			final Integer candidateNumber, final Integer executionYearID) throws ExcepcaoPersistencia {

		final ExecutionYear executionYear = (ExecutionYear) readByOID(ExecutionYear.class,
				executionYearID);
		final List<ExecutionDegree> executionDegrees = executionYear
				.getExecutionDegrees();

		final List<MasterDegreeCandidate> masterDegreeCandidatesByYear = new ArrayList<MasterDegreeCandidate>();

		for (ExecutionDegree degree : executionDegrees) {
			masterDegreeCandidatesByYear.addAll(degree.getMasterDegreeCandidates());
		}

		if (executionDegreeID == null && specialization == null && situationName == null
				&& candidateNumber == null) {

			return masterDegreeCandidatesByYear;
		}

		final List<MasterDegreeCandidate> result = new ArrayList<MasterDegreeCandidate>();

		final List<Boolean> mask = new ArrayList(4);

		mask.set(0, executionDegreeID == null ? false : true);
		mask.set(1, specialization == null ? false : true);
		mask.set(2, candidateNumber == null ? false : true);
		mask.set(3, situationName == null ? false : true);

		for (final MasterDegreeCandidate masterDegreeCandidate : masterDegreeCandidatesByYear) {
			boolean checked = true;

			if (mask.get(0)) {
				checked = masterDegreeCandidate.getExecutionDegree().getIdInternal().equals(
						executionDegreeID);
			}
			if (mask.get(1) && checked) {
				checked = masterDegreeCandidate.getSpecialization().equals(specialization);
			}
			if (mask.get(2) && checked) {
				checked = masterDegreeCandidate.getCandidateNumber().equals(candidateNumber);
			}
			if (mask.get(3) && checked) {
				checked = false;
				for (CandidateSituation candidateSituation : masterDegreeCandidate
						.getSituations()) {
					if (candidateSituation.getSituation().equals(situationName)) {
						checked = true;
					}
				}
			}

			if (checked) {
				result.add(masterDegreeCandidate);
			}
		}
		return result;
	}
    
} 
