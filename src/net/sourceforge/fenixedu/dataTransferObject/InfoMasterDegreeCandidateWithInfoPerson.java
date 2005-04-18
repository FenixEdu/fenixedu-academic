package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;

/**
 * @author Fernanda Quitério Created on 1/Jul/2004
 *  
 */
public class InfoMasterDegreeCandidateWithInfoPerson extends InfoMasterDegreeCandidate {

    public void copyFromDomain(IMasterDegreeCandidate masterDegreeCandidate) {
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