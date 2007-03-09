package net.sourceforge.fenixedu.domain.thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.EvaluationType;

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
        List<Thesis> result = new ArrayList<Thesis>();
        
        result.addAll(getThesisInState(degree, ThesisState.APPROVED));
        result.addAll(getThesisInState(degree, ThesisState.REVISION));
        
        return result;
    }

    public static Collection<Thesis> getConfirmedThesis(Degree degree) {
        return getThesisInState(degree, ThesisState.CONFIRMED);
    }
    
    public static Collection<Thesis> getEvaluatedThesis(Degree degree) {
        return getThesisInState(degree, ThesisState.EVALUATED);
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
        
        if (! getConditions().isEmpty()) {
            throw new DomainException("thesis.submit.hasConditions");
        }
        
        setSubmission(new DateTime());
        setState(ThesisState.SUMITTED);
    }

    public void reject() {
        if (getState() != ThesisState.SUMITTED) {
            throw new DomainException("thesis.approve.notSubmitted");
        }
        
        setState(ThesisState.DRAFT);
    }

    public void approve() {
        if (getState() != ThesisState.SUMITTED) {
            throw new DomainException("thesis.approve.notSubmitted");
        }
        
        setApproval(new DateTime());
        setState(ThesisState.APPROVED);
    }
    
    public void confirm(String mark, DateTime discussed) {
        if (getState() != ThesisState.APPROVED && getState() != ThesisState.REVISION) {
            throw new DomainException("thesis.confirm.notApprovedOrInRevision");
        }
        
        setDiscussed(discussed);
        setMark(mark);

        setConfirmation(new DateTime());
        setState(ThesisState.CONFIRMED);
    }
    
    public void allowRevision() {
        if (getState() != ThesisState.CONFIRMED) {
            throw new DomainException("thesis.confirm.notConfirmed");
        }
        
        setState(ThesisState.REVISION);
    }
    
    public void approveEvaluation() {
        if (getState() != ThesisState.CONFIRMED) {
            throw new DomainException("thesis.confirm.notConfirmed");
        }
        
        setEvaluation(new DateTime());
        setState(ThesisState.EVALUATED);
    }
    
    public void rejectEvaluation() {
        if (getState() != ThesisState.EVALUATED) {
            throw new DomainException("thesis.confirm.notEvaluated");
        }
        
        setState(ThesisState.CONFIRMED);
    }
    
    @Override
    public void setMark(String mark) {
        if (! isMarkValid(mark)) {
            throw new DomainException("thesis.mark.invalid");
        }
        
        super.setMark(mark);
    }

    public boolean isMarkValid(String mark) {
        //TODO: enrolment 
        //GradeScale scale = getEnrolment().getCurricularCourse().getGradeScaleChain();
        GradeScale scale = null;
        
        if (scale == null) {
            scale = GradeScale.TYPE20;
        }
        
        return scale.isValid(mark, EvaluationType.FINAL_TYPE);
    }

    public List<ThesisCondition> getConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        conditions.addAll(getGeneralConditions());
        conditions.addAll(getOrientationConditions());
        conditions.addAll(getPresidentConditions());
        conditions.addAll(getVowelsConditions());
        
        return conditions;
    }
    
    public Collection<ThesisCondition> getGeneralConditions() {
        Person orientator = getOrientator();
        Person coorientator = getCoorientator();
        Person president = getPresident();
        
        if (orientator != null && coorientator == null && president != null) {
            if (getVowels().isEmpty()) {
                return Arrays.asList(new ThesisCondition("thesis.condition.people.number.exceeded"));
            }
        }
        
        return Arrays.asList();
    }

    public List<ThesisCondition> getOrientationConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        boolean hasInternal = false;
        
        Person orientator = getOrientator();
        Person coorientator = getCoorientator();
        
        if (orientator == null) {
            conditions.add(new ThesisCondition("thesis.condition.orientator.required"));
        }
        else {
            if (! orientator.hasExternalPerson()) {
                hasInternal = true;
            }
        }
        
        if (coorientator != null && !coorientator.hasExternalPerson()) {
            hasInternal = true;
        }
        
        if (! hasInternal && (orientator != null || coorientator != null)) {
            conditions.add(new ThesisCondition("thesis.condition.orientation.notInternal"));
        }
        
        return conditions;
    }
    
    public List<ThesisCondition> getPresidentConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        Person president = getPresident();
        
        if (president == null) {
            conditions.add(new ThesisCondition("thesis.condition.president.required"));
        }
        else {
            if (president.hasExternalPerson()) {
                conditions.add(new ThesisCondition("thesis.condition.president.notInternal"));
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
                    conditions.add(new ThesisCondition("thesis.condition.president.notCoordinator"));
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
            return Arrays.asList(new ThesisCondition("thesis.condition.people.number.exceeded"));
        }
        else {
            return Collections.emptyList();
        }
    }

}
