package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadSubstituteCandidates {

    @Atomic
    public static List<InfoMasterDegreeCandidate> run(String[] candidateList, String[] ids) throws FenixServiceException {

        List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();

        // Read the substitute candidates
        int size = candidateList.length;

        for (int i = 0; i < size; i++) {
            if (candidateList[i].equals(SituationName.SUPLENTE_STRING)
                    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_CURRICULAR_STRING)
                    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_FINALIST_STRING)
                    || candidateList[i].equals(SituationName.SUBSTITUTE_CONDICIONAL_OTHER_STRING)) {

                String externalId = ids[i];

                MasterDegreeCandidate masterDegreeCandidateToWrite = FenixFramework.getDomainObject(externalId);
                result.add(InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidateToWrite));
            }
        }

        return result;
    }

}