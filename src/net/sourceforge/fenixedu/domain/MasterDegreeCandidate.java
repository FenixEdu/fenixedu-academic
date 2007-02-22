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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;
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
    
    public boolean hasCandidateSituationWith(final SituationName situationName) {
        final State candidateSituationState = new State(State.ACTIVE);
        for (final CandidateSituation candidateSituation : this.getSituationsSet()) {
            if (candidateSituation.getSituation().equals(situationName) &&
                    candidateSituation.getValidation().equals(candidateSituationState)) {
                return true;
            }
        }
        return false;
    }
    
    public static Set<MasterDegreeCandidate> readByExecutionDegreeOrSpecializationOrCandidateNumberOrSituationName(
            final ExecutionDegree executionDegree, final Specialization specialization,
            final Integer candidateNumber, final SituationName situationName) {
        
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : RootDomainObject.getInstance().getMasterDegreeCandidatesSet()) {
            if (executionDegree != null && masterDegreeCandidate.getExecutionDegree() != executionDegree) {
                continue;
            }
            if (specialization != null && masterDegreeCandidate.getSpecialization() != specialization) {
                continue;
            }
            if (candidateNumber != null && ! masterDegreeCandidate.getCandidateNumber().equals(candidateNumber)) {
                continue;
            }
            if (situationName != null && ! masterDegreeCandidate.hasCandidateSituationWith(situationName)) {
                continue;
            }
            result.add(masterDegreeCandidate);
        }
        return result;
    }
    
    public String getPrecedentDegreeDescription(){
	StringBuilder description = new StringBuilder();
	if(getMajorDegree() != null){
	    description.append(getMajorDegree());
	}	
	description.append(" - ");
	if(getMajorDegreeYear() != null){
	    description.append(getMajorDegreeYear());
	}	
	description.append(" - ");
	if(getMajorDegreeSchool() != null){
	    description.append(getMajorDegreeSchool());
	}	
	return description.toString();
    }

}
