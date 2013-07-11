package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadMasterDegreeCandidateByID {

    @Atomic
    public static InfoMasterDegreeCandidate run(String candidateID) {
        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);
        return (masterDegreeCandidate != null) ? InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate) : null;
    }

}