package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.SituationName;

public class ReadCandidateList extends Service {

    public List run(String degreeName, Specialization degreeType, SituationName candidateSituation,
            Integer candidateNumber, String executionYearString) throws ExcepcaoPersistencia {
     
        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearString);
        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(Integer.valueOf(degreeName));

        final Set<MasterDegreeCandidate> result;
        if (executionDegree == null && degreeType == null && candidateSituation == null && candidateNumber == null) {
            result = new HashSet<MasterDegreeCandidate>();
            for (final ExecutionDegree executionDegreeIter : executionYear.getExecutionDegreesSet()) {
                result.addAll(executionDegreeIter.getMasterDegreeCandidatesSet());
            }
        } else {
           result = MasterDegreeCandidate.
               readByExecutionDegreeOrSpecializationOrCandidateNumberOrSituationName(executionDegree, degreeType,
                       candidateNumber, candidateSituation);
        }

        final List candidateList = new ArrayList();
        final Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            final MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) iterator.next();
            final InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);

            final Iterator situationIterator = masterDegreeCandidate.getSituations().iterator();
            final List situations = new ArrayList();
            while (situationIterator.hasNext()) {
                final InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation.newInfoFromDomain((CandidateSituation) situationIterator.next());
                situations.add(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);

            executionDegree = masterDegreeCandidate.getExecutionDegree();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;
    }
}