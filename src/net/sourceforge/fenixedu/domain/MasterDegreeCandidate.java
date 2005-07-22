/*
 * MasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 9:53
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.domain;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.State;

public class MasterDegreeCandidate extends MasterDegreeCandidate_Base {

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    // from old application service 'GenerateUsername'
    public static String generateUsernameForNewCandidate(
            IMasterDegreeCandidate newMasterDegreeCandidate, List<IPerson> persons) {

        // FIXME : temporary solution: in the future create username by name
        int start = 0;
        int end = 3;
        char buf[] = new char[end - start];
        newMasterDegreeCandidate.getSpecialization().toString().getChars(start, end, buf, 0);

        // Generate Username
        String username = newMasterDegreeCandidate.getCandidateNumber()
                + String.valueOf(buf)
                + newMasterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                        .getSigla();

        // Verify if the Username already Exists
        String generatedUsername = new String(username);
        int i = 1;

        while (Person.checkIfUsernameExists(generatedUsername, persons)) {
            generatedUsername = new String(username + i++);
        }

        return generatedUsername;
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    public MasterDegreeCandidate() {
        this.setHasGuider(false);
        this.setCourseAssistant(false);
    }

    public MasterDegreeCandidate(IPerson person, IExecutionDegree executionDegree,
            Integer candidateNumber, Specialization specialization, String majorDegree,
            String majorDegreeSchool, Integer majorDegreeYear, Double average, ITeacher guider,
            Boolean hasGuider, Boolean courseAssistant, String coursesToAssist) {
        this.setPerson(person);
        this.setExecutionDegree(executionDegree);
        this.setCandidateNumber(candidateNumber);
        this.setSpecialization(specialization);
        this.setMajorDegree(majorDegree);
        this.setMajorDegreeSchool(majorDegreeSchool);
        this.setMajorDegreeYear(majorDegreeYear);
        this.setAverage(average);
        this.setGuider(guider);
        this.setHasGuider(hasGuider);
        this.setCourseAssistant(courseAssistant);
        this.setCoursesToAssist(coursesToAssist);

    }

    public String toString() {
        String result = "Master Degree Candidate :\n";
        result += "\n  - Internal Code : " + getIdInternal();
        result += "\n  - Person : " + getPerson();
        result += "\n  - Major Degree : " + getMajorDegree();
        result += "\n  - Candidate Number : " + getCandidateNumber();
        result += "\n  - Specialization : " + getSpecialization();
        result += "\n  - Major Degree School : " + getMajorDegreeSchool();
        result += "\n  - Major Degree Year : " + getMajorDegreeYear();
        result += "\n  - Major Degree Average : " + getAverage();
        result += "\n  - Master Degree : " + getExecutionDegree();
        result += "\n  - Specialization Area : " + getSpecializationArea();
        result += "\n  - Substitute Order : " + getSubstituteOrder();
        result += "\n  - Given Credits : " + getGivenCredits();
        result += "\n  - Given Credits Remarks : " + getGivenCreditsRemarks();
        if (getCourseAssistant())
            result += "\n  - Wishes To Assist Courses : " + getCoursesToAssist();
        else
            result += "\n  - Does Not Wish To Assist Courses";
        if (getGuider() == null)
            result += "\n  - The Candidate Has Not A Guider/Councilour";
        else {
            if (getHasGuider())
                result += "\n  - The Guider Is : " + getGuider().getPerson().getNome();
            else
                result += "\n  - The Councilour Is : " + getGuider().getPerson().getNome();
        }

        return result;
    }

    public ICandidateSituation getActiveCandidateSituation() {
        Iterator iterator = this.getSituations().iterator();
        while (iterator.hasNext()) {
            ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator.next();
            if (candidateSituationTemp.getValidation().equals(new State(State.ACTIVE))) {
                return candidateSituationTemp;
            }
        }
        return null;
    }

}
