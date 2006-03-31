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

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.State;

public class MasterDegreeCandidate extends MasterDegreeCandidate_Base {

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    public MasterDegreeCandidate() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
        this.setHasGuider(false);
        this.setCourseAssistant(false);
    }

    public MasterDegreeCandidate(Person person, ExecutionDegree executionDegree,
            Integer candidateNumber, Specialization specialization, String majorDegree,
            String majorDegreeSchool, Integer majorDegreeYear, Double average, Teacher guider,
            Boolean hasGuider, Boolean courseAssistant, String coursesToAssist) {
    	this();
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

    public CandidateSituation getActiveCandidateSituation() {
        for (CandidateSituation candidateSituation : getSituations()) {
            if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
                return candidateSituation;
            }
        }
        return null;
    }

}
