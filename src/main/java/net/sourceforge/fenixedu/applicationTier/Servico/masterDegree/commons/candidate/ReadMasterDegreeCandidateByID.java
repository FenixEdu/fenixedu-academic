package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;


import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadMasterDegreeCandidateByID {

    @Service
    public static InfoMasterDegreeCandidate run(Integer candidateID) {
        MasterDegreeCandidate masterDegreeCandidate = RootDomainObject.getInstance().readMasterDegreeCandidateByOID(candidateID);
        return (masterDegreeCandidate != null) ? InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate) : null;
    }

}