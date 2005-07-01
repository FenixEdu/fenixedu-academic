package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Fernanda Quitério Created on 1/Jul/2004
 * 
 */
public class InfoMasterDegreeCandidateWithInfoPerson extends InfoMasterDegreeCandidate {

    public void copyFromDomain(IMasterDegreeCandidate masterDegreeCandidate) {
        super.copyFromDomain(masterDegreeCandidate);
        if (masterDegreeCandidate != null) {
            setInfoPerson(InfoPersonWithInfoCountry.newInfoFromDomain(masterDegreeCandidate.getPerson()));
            setAverage(masterDegreeCandidate.getAverage());
            setCandidateNumber(masterDegreeCandidate.getCandidateNumber());
            setGivenCredits(masterDegreeCandidate.getGivenCredits());
            setGivenCreditsRemarks(masterDegreeCandidate.getGivenCreditsRemarks());
            setMajorDegree(masterDegreeCandidate.getMajorDegree());
            setMajorDegreeSchool(masterDegreeCandidate.getMajorDegreeSchool());
            setMajorDegreeYear(masterDegreeCandidate.getMajorDegreeYear());
            setSpecializationArea(masterDegreeCandidate.getSpecializationArea());
            setSubstituteOrder(masterDegreeCandidate.getSubstituteOrder());
            setSituationList(new ArrayList<InfoCandidateSituation>(masterDegreeCandidate.getSituations()
                    .size()));

            for (ICandidateSituation candidateSituation : (List<ICandidateSituation>) masterDegreeCandidate
                    .getSituations()) {
                getSituationList().add(InfoCandidateSituation.newInfoFromDomain(candidateSituation));

                if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
                    setInfoCandidateSituation(InfoCandidateSituation
                            .newInfoFromDomain(candidateSituation));
                }
            }

        }
    }

    public static InfoMasterDegreeCandidate newInfoFromDomain(
            IMasterDegreeCandidate masterDegreeCandidate) {
        InfoMasterDegreeCandidateWithInfoPerson infoMasterDegreeCandidateWithInfoPerson = null;
        if (masterDegreeCandidate != null) {
            infoMasterDegreeCandidateWithInfoPerson = new InfoMasterDegreeCandidateWithInfoPerson();
            infoMasterDegreeCandidateWithInfoPerson.copyFromDomain(masterDegreeCandidate);
        }
        return infoMasterDegreeCandidateWithInfoPerson;
    }

}