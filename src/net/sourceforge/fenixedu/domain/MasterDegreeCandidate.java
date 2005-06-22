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

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.State;

public class MasterDegreeCandidate extends MasterDegreeCandidate_Base {

	public MasterDegreeCandidate() {
		setMajorDegree(null);
		setExecutionDegree(null);
		setCandidateNumber(null);
		setSpecialization(null);
		setMajorDegreeSchool(null);
		setMajorDegreeYear(null);
		setAverage(null);
		setSituations(null);
		setPerson(null);
        setGuider(null);
        setHasGuider(false);
        setCourseAssistant(false);
        setCoursesToAssist("");		
	}

	public MasterDegreeCandidate(IPerson person,
			IExecutionDegree executionDegree, Integer candidateNumber,
			Specialization specialization, String majorDegree,
					String majorDegreeSchool, Integer majorDegreeYear, Double average,
            ITeacher guider, Boolean hasGuider,
            Boolean courseAssistant, String coursesToAssist) {
		setPerson(person);
		this.setExecutionDegree(executionDegree);
		setCandidateNumber(candidateNumber);
        setSpecialization(specialization);
		setMajorDegree(majorDegree);
		setMajorDegreeSchool(majorDegreeSchool);
		setMajorDegreeYear(majorDegreeYear);
		setAverage(average);
        setGuider(guider);
        setHasGuider(hasGuider);
        setCourseAssistant(courseAssistant);
        setCoursesToAssist(coursesToAssist);		

	}

	public boolean equals(Object o) {

		boolean result = false;
		if (o instanceof IMasterDegreeCandidate) {

            result = getIdInternal().equals(((IMasterDegreeCandidate) o).getIdInternal());
		}
		return result;
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
        else result += "\n  - Does Not Wish To Assist Courses";
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
