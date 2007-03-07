package net.sourceforge.fenixedu.domain.thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.DateTime;

public class Thesis extends Thesis_Base {
    
    public static class ThesisCondition {
        private String key;

        public ThesisCondition(String key) {
            super();
            
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
        
    }
    
    protected Thesis() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());

        setCreation(new DateTime());
        setState(ThesisState.DRAFT);
    }
    
    public Thesis(Degree degree, Enrolment enrolment) {
        this();
        
        setDegree(degree);
        setEnrolment(enrolment);
    }
    
    public void delete() {
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.delete.notDraft");
        }
        
        removeRootDomainObject();

        removeOrientator();
        removeCoorientator();
        removePresident();
        
        getVowels().clear();
        
        removeDissertation();
        removeExtendedAbstract();
        
        removeEnrolment();
        
        deleteDomainObject();
    }
    
    protected static Collection<Thesis> getThesisInState(Degree degree, ThesisState state) {
        List<Thesis> theses = new ArrayList<Thesis>();
        
        for (Thesis thesis : RootDomainObject.getInstance().getTheses()) {
            if (thesis.getState() == state && thesis.getDegree() == degree) {
                theses.add(thesis);
            }
        }
        
        return theses;
    }
    
    public static Collection<Thesis> getDraftThesis(Degree degree) {
        return getThesisInState(degree, ThesisState.DRAFT);
    }

    public static Collection<Thesis> getSubmittedThesis(Degree degree) {
        return getThesisInState(degree, ThesisState.SUMITTED);
    }

    public static Collection<Thesis> getApprovedThesis(Degree degree) {
        return getThesisInState(degree, ThesisState.APPROVED);
    }

    @Override
    public void setEnrolment(Enrolment enrolment) {
        // TODO: check enrolment (must be valid in degree)
        // if (enrolment == null) {
        //    throw new FieldIsRequiredException("enrolment", "thesis.enrolment.required");
        // }
        //
        //((CurricularCourse) enrolment.getDegreeModule()).isDissertation();
        
        super.setEnrolment(enrolment);
    }

    public Student getStudent() {
        // TODO: code to get student
        //return getEnrolment().getStudentCurricularPlan().getRegistration().getStudent();
        
        return RootDomainObject.getInstance().readStudentByOID(25000);
    }

    public void submit() {
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.submit.notDraft");
        }
        
        setSubmission(new DateTime());
        setState(ThesisState.SUMITTED);
    }

    public List<ThesisCondition> getConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        conditions.addAll(getOrientationConditions());
        conditions.addAll(getPresidentConditions());
        conditions.addAll(getVowelsConditions());
        
        return conditions;
    }
    
    public List<ThesisCondition> getOrientationConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        boolean hasInternal = false;
        
        Person orientator = getOrientator();
        Person coorientator = getCoorientator();
        
        if (orientator == null) {
            // orientator is required
        }
        else {
            if (! orientator.hasExternalPerson()) {
                hasInternal = true;
            }
        }
        
        if (coorientator != null && !coorientator.hasExternalPerson()) {
            hasInternal = true;
        }
        
        if (! hasInternal) {
            // one of them must be internal
        }
        
        return conditions;
    }
    
    public List<ThesisCondition> getPresidentConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        Person president = getPresident();
        
        if (president == null) {
            // president is required
        }
        else {
            if (president.hasExternalPerson()) {
                // president must be internal
            }
            else {
                boolean isCoordinator = false;
                
                for (Coordinator coordinator : getDegree().getCurrentResponsibleCoordinators()) {
                    isCoordinator = isCoordinator || president == coordinator.getPerson();
                    
                    if (isCoordinator) {
                        break;
                    }
                }
                
                if (! isCoordinator) {
                    // must be coordinator
                }
            }
        }
        
        return conditions;
    }
    
    public List<ThesisCondition> getVowelsConditions() {
        int count = 0;
        
        if (hasOrientator()) {
            count++;
        }
        
        if (hasCoorientator()) {
            count++;
        }

        if (hasPresident()) {
            count++;
        }
        
        count += getVowels().size();
        
        if (count > 5) {
            return Arrays.asList(new ThesisCondition(""));
        }
        else {
            return Collections.emptyList();
        }
    }
}
