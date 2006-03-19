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

import java.util.List;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.lang.StringUtils;

public class MasterDegreeCandidate extends MasterDegreeCandidate_Base {

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    // from old application service 'GenerateUsername'
    public static String generateUsernameForNewCandidate(
            MasterDegreeCandidate newMasterDegreeCandidate, List<Person> persons) {

    	Integer max = 0;
    	
    	for (Person person : persons) {
			if(person.getUsername().startsWith("C")) {
				Integer candidateNumber = Integer.valueOf(person.getUsername().substring(1));
				if(candidateNumber > max) {
					max = candidateNumber;
				}
			}
		}
    	return "C" + StringUtils.leftPad(String.valueOf(++max), 5, "0");
    }

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
