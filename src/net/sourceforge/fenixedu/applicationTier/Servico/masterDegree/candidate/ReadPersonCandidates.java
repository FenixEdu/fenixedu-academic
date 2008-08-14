package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;

public class ReadPersonCandidates extends Service {

    public List<InfoMasterDegreeCandidate> run(final String username) throws FenixServiceException {
	final Person person = Person.readPersonByUsername(username);
	if (person == null) {
	    throw new FenixServiceException("error.noPerson");
	}
	final List<InfoMasterDegreeCandidate> result = new ArrayList<InfoMasterDegreeCandidate>();
	for (final MasterDegreeCandidate masterDegreeCandidate : person.getMasterDegreeCandidatesSet()) {
	    result.add(InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate));
	}
	return result;
    }
}