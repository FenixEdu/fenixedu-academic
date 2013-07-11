package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadCandidateList {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static List run(String degreeName, Specialization degreeType, SituationName candidateSituation,
            Integer candidateNumber, String executionYearString) {

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(degreeName);

        final Set<MasterDegreeCandidate> result;
        if (executionDegree == null && degreeType == null && candidateSituation == null && candidateNumber == null) {
            result = new HashSet<MasterDegreeCandidate>();
            for (final ExecutionDegree executionDegreeIter : executionYear.getExecutionDegreesSet()) {
                result.addAll(executionDegreeIter.getMasterDegreeCandidatesSet());
            }
        } else {
            result =
                    MasterDegreeCandidate.readByExecutionDegreeOrSpecializationOrCandidateNumberOrSituationName(executionDegree,
                            degreeType, candidateNumber, candidateSituation);
        }

        final List candidateList = new ArrayList();
        final Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            final MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) iterator.next();
            final InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                    InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);

            final Iterator situationIterator = masterDegreeCandidate.getSituations().iterator();
            final List situations = new ArrayList();
            while (situationIterator.hasNext()) {
                final InfoCandidateSituation infoCandidateSituation =
                        InfoCandidateSituation.newInfoFromDomain((CandidateSituation) situationIterator.next());
                situations.add(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);

            executionDegree = masterDegreeCandidate.getExecutionDegree();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;
    }
}