package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Fernanda Quitério Created on 1/Jul/2004
 * 
 */
public class InfoMasterDegreeCandidateWithInfoPerson extends InfoMasterDegreeCandidate {

    public void copyFromDomain(MasterDegreeCandidate masterDegreeCandidate) {
        super.copyFromDomain(masterDegreeCandidate);
        if (masterDegreeCandidate != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(masterDegreeCandidate.getPerson()));
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

            for (CandidateSituation candidateSituation : masterDegreeCandidate
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
            MasterDegreeCandidate masterDegreeCandidate) {
        InfoMasterDegreeCandidateWithInfoPerson infoMasterDegreeCandidateWithInfoPerson = null;
        if (masterDegreeCandidate != null) {
            infoMasterDegreeCandidateWithInfoPerson = new InfoMasterDegreeCandidateWithInfoPerson();
            infoMasterDegreeCandidateWithInfoPerson.copyFromDomain(masterDegreeCandidate);
        }
        return infoMasterDegreeCandidateWithInfoPerson;
    }

}