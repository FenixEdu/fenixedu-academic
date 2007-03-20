package net.sourceforge.fenixedu.domain.thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

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
    
    public Thesis(Degree degree, Enrolment enrolment, MultiLanguageString title) {
        this();
        
        if (degree == null) {
            throw new FieldIsRequiredException("enrolment", "thesis.degree.required");
        }

        if (enrolment == null) {
            throw new FieldIsRequiredException("enrolment", "thesis.enrolment.required");
        }
        
        setDegree(degree);
        setEnrolment(enrolment);
        setTitle(title);
    }
    
    @Override
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.isEmpty()) {
            throw new FieldIsRequiredException("title", "thesis.title.required");
        }
        
        super.setTitle(title);
    }

    @Override
    @Checked("ThesisPredicates.waitingConfirmation")
    public void setThesisAbstract(MultiLanguageString thesisAbstract) {
        super.setThesisAbstract(thesisAbstract);
    }

    @Override
    @Checked("ThesisPredicates.waitingConfirmation")
    public void setKeywords(MultiLanguageString keywords) {
        super.setKeywords(keywords);
    }

    public ThesisEvaluationParticipant getOrientator() {
        return getParticipant(ThesisParticipationType.ORIENTATOR);
    }
    
    public ThesisEvaluationParticipant getCoorientator() {
        return getParticipant(ThesisParticipationType.COORIENTATOR);
    }
    
    public ThesisEvaluationParticipant getPresident() {
        return getParticipant(ThesisParticipationType.PRESIDENT);
    }
    
    public List<ThesisEvaluationParticipant> getVowels() {
        return getAllParticipants(ThesisParticipationType.VOWEL);
    }
    
    public ThesisEvaluationParticipant getParticipant(ThesisParticipationType type) {
        for (ThesisEvaluationParticipant participant : getParticipations()) {
            if (participant.getType() == type) {
                return participant;
            }
        }
        
        return null;
    }
    
    public List<ThesisEvaluationParticipant> getAllParticipants(ThesisParticipationType type) {
        List<ThesisEvaluationParticipant> result = new ArrayList<ThesisEvaluationParticipant>();
        
        for (ThesisEvaluationParticipant participant : getParticipations()) {
            if (participant.getType() == type) {
                result.add(participant);
            }
        }
        
        return result;
    }
    
    public void delete() {
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.delete.notDraft");
        }
        
        removeRootDomainObject();
        
        for (; !getParticipations().isEmpty(); getParticipations().iterator().next().delete()); 

        removeDissertation();
        removeExtendedAbstract();
        
        removeEnrolment();
        
        deleteDomainObject();
    }
    
    protected static Collection<Thesis> getThesisInState(Degree degree, ExecutionYear year, ThesisState state) {
        List<Thesis> theses = new ArrayList<Thesis>();
        
        for (Thesis thesis : RootDomainObject.getInstance().getTheses()) {
            if (thesis.getState() != state) {
                continue;
            }
            
            if (degree != null && thesis.getDegree() != degree) {
                continue;
            }
            
            if (year != null && thesis.getEnrolment().getExecutionYear() != year) {
                continue;
            }
            
            theses.add(thesis);
        }
        
        return theses;
    }
    
    public static Collection<Thesis> getDraftThesis(Degree degree) {
        return getThesisInState(degree, null, ThesisState.DRAFT);
    }
 
    public static Collection<Thesis> getSubmittedThesis() {
        return getSubmittedThesis(null);
    }

    public static Collection<Thesis> getSubmittedThesis(Degree degree) {
        return getSubmittedThesis(degree, null);
    }

    public static Collection<Thesis> getSubmittedThesis(Degree degree, ExecutionYear executionYear) {
        return getThesisInState(degree, executionYear, ThesisState.SUBMITTED);
    }
    
    public static Collection<Thesis> getApprovedThesis() {
        return getApprovedThesis(null);
    }

    public static Collection<Thesis> getApprovedThesis(Degree degree) {
        return getApprovedThesis(degree, null);
    }

    public static Collection<Thesis> getApprovedThesis(Degree degree, ExecutionYear executionYear) {
	List<Thesis> result = new ArrayList<Thesis>();
        
        result.addAll(getThesisInState(degree, executionYear, ThesisState.APPROVED));
        result.addAll(getThesisInState(degree, executionYear, ThesisState.REVISION));
        
        return result;
    }

    public static Collection<Thesis> getConfirmedThesis() {
        return getConfirmedThesis(null);
    }
    
    public static Collection<Thesis> getConfirmedThesis(Degree degree) {
        return getConfirmedThesis(degree, null);
    }
    
    public static Collection<Thesis> getConfirmedThesis(Degree degree, ExecutionYear executionYear) {
        return getThesisInState(degree, executionYear, ThesisState.CONFIRMED);
    }
    
    public static Collection<Thesis> getEvaluatedThesis() {
        return getEvaluatedThesis(null);
    }
    
    public static Collection<Thesis> getEvaluatedThesis(Degree degree) {
        return getEvaluatedThesis(degree, null);
    }

    public static Collection<Thesis> getEvaluatedThesis(Degree degree, ExecutionYear executionYear) {
        return getThesisInState(degree, executionYear, ThesisState.EVALUATED);
    }

    @Override
    public void setEnrolment(Enrolment enrolment) {
        if (enrolment != null) {
            CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            if (! curricularCourse.isDissertation()) {
                throw new DomainException("thesis.enrolment.notDissertationEnrolment");
            }
        }
        
        super.setEnrolment(enrolment);
    }

    public Student getStudent() {
        return getEnrolment().getStudentCurricularPlan().getRegistration().getStudent();
    }

    public void submit() {
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.submit.notDraft");
        }
        
        if (! isValid()) {
            throw new DomainException("thesis.submit.hasConditions");
        }
        
        setSubmission(new DateTime());
        setRejectionComment(null);
        setState(ThesisState.SUBMITTED);
    }

    public boolean isValid() {
        return getConditions().isEmpty();
    }

    public void reject() {
        if (getState() != ThesisState.SUBMITTED) {
            throw new DomainException("thesis.approve.notSubmitted");
        }
        
        setState(ThesisState.DRAFT);
    }

    public void approveProposal() {
        if (getState() != ThesisState.SUBMITTED) {
            throw new DomainException("thesis.approve.notSubmitted");
        }
        
        setApproval(new DateTime());
        setState(ThesisState.APPROVED);
    }
    
    @Checked("ThesisPredicates.isScientificCouncil")
    public void rejectProposal(String rejectionComment) {
        	if (getState() != ThesisState.SUBMITTED && getState() != ThesisState.APPROVED) {
        	    throw new DomainException("thesis.reject.notSubmittedNorApproved");
        	}
        	
        	setRejectionComment(rejectionComment);
        	setState(ThesisState.DRAFT);
    }
    
    public void confirm(String mark, DateTime discussed) {
        if (getState() != ThesisState.APPROVED && getState() != ThesisState.REVISION) {
            throw new DomainException("thesis.confirm.notApprovedOrInRevision");
        }
        
        if (! isThesisAbstractInBothLanguages()) {
            throw new DomainException("thesis.confirm.noAbstract");
        }
        
        if (! isKeywordsInBothLanguages()) {
            throw new DomainException("thesis.confirm.noKeywords");
        }
        
        if (! hasExtendedAbstract()) {
            throw new DomainException("thesis.confirm.noExtendedAbstract");
        }
        
        if (! hasDissertation()) {
            throw new DomainException("thesis.confirm.noDissertation");
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
    
    public boolean isDraft() {
        return getState() == ThesisState.DRAFT;
    }

    public boolean isSubmitted() {
        return getState() == ThesisState.SUBMITTED;
    }

    public boolean isApproved() {
        ThesisState state = getState();
        
        return state == ThesisState.APPROVED;
    }

    public boolean isWaitingConfirmation() {
        ThesisState state = getState();
        
        return state == ThesisState.APPROVED || state == ThesisState.REVISION;
    }
    
    public boolean isConfirmed() {
        return getState() == ThesisState.CONFIRMED;
    }
    
    public boolean isAtLeastConfirmed() {
        switch (getState()) {
        case CONFIRMED:
        case EVALUATED:
            return true;
        default:
            return false;
        }
    }

    public boolean isEvaluated() {
        return getState() == ThesisState.EVALUATED;
    }

    public boolean isRejected() {
        // TODO: include REJECTED state or flag
        return isDraft() && getSubmission() != null;
    }

    @Override
    public void setMark(String mark) {
        if (! isMarkValid(mark)) {
            throw new DomainException("thesis.mark.invalid");
        }
        
        super.setMark(mark);
    }

    public boolean isMarkValid(String mark) {
        GradeScale scale = getEnrolment().getCurricularCourse().getGradeScaleChain();
        
        if (scale == null) {
            scale = GradeScale.TYPE20;
        }
        
        return scale.isValid(mark, EvaluationType.FINAL_TYPE);
    }

    private Person getParticipationPerson(ThesisEvaluationParticipant participant) {
        if (participant == null) {
            return null;
        }
        else {
            return participant.getPerson();
        }
    }
    
    private void removeParticipation(ThesisEvaluationParticipant participant) {
        if (participant != null) {
            participant.delete();
        }
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
        List<ThesisCondition> result = new ArrayList<ThesisCondition>();
        
        Person orientator = getParticipationPerson(getOrientator());
        Person coorientator = getParticipationPerson(getCoorientator());
        Person president = getParticipationPerson(getPresident());
        List<ThesisEvaluationParticipant> vowels = getVowels();

        // check for duplicated persons
        List<Person> persons = new ArrayList<Person>(Arrays.asList(orientator, coorientator, president));
        for (ThesisEvaluationParticipant vowel : vowels) {
            persons.add(vowel.getPerson());
        }
        
        Set<Person> personSet = new HashSet<Person>();
        for (Person person : persons) {
            if (person != null && !personSet.add(person)) {
                result.add(new ThesisCondition("thesis.condition.people.repeated"));
                break;
            }
        }
        
        if (orientator != null && coorientator == null && president != null) {
            if (vowels.isEmpty()) {
                result.add(new ThesisCondition("thesis.condition.people.number.few"));
            }
        }
        
        return result;
    }

    public List<ThesisCondition> getOrientationConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        boolean hasInternal = false;
        
        Person orientator = getParticipationPerson(getOrientator());
        Person coorientator = getParticipationPerson(getCoorientator());
        
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
        
        Person president = getParticipationPerson(getPresident());
        
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
        
        if (getOrientator() != null) {
            count++;
        }
        
        if (getCoorientator() != null) {
            count++;
        }

        if (getPresident() != null) {
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
    
    public boolean isThesisAbstractInBothLanguages() {
        return getThesisAbstractPt() != null && getThesisAbstractEn() != null;
    }
    
    public boolean isKeywordsInBothLanguages() {
        return getKeywordsPt() != null && getKeywordsEn() != null;
    }
    
    public String getThesisAbstractPt() {
        return getThesisAbstractLanguage("pt");
    }

    @Checked("ThesisPredicates.waitingConfirmation")
    public void setThesisAbstractPt(String text) {
        setThesisAbstractLanguage("pt", text);
    }
    
    public String getThesisAbstractEn() {
        return getThesisAbstractLanguage("en");
    }
    
    @Checked("ThesisPredicates.waitingConfirmation")
    public void setThesisAbstractEn(String text) {
        setThesisAbstractLanguage("en", text);
    }
    
    public String getThesisAbstractLanguage(String language) {
        MultiLanguageString thesisAbstract = getThesisAbstract();
        
        if (thesisAbstract == null) {
            return null;
        }
        else {
            Language realLanguage = Language.valueOf(language);
            String value = thesisAbstract.getContent(realLanguage);
            
            if (value == null && value.length() == 0) {
                return null;
            }
            else {
                return value;
            }
        }
    }
    
    public void setThesisAbstractLanguage(String language, String text) {
        MultiLanguageString thesisAbstract = getThesisAbstract();
        Language realLanguage = Language.valueOf(language);
        
        if (thesisAbstract == null) {
            setThesisAbstract(new MultiLanguageString(realLanguage, text));
        }
        else {
            thesisAbstract.setContent(realLanguage, text);
            setThesisAbstract(thesisAbstract);
        }
    }
    
    public String getKeywordsPt() {
        return getKeywordsLanguage("pt");
    }

    @Checked("ThesisPredicates.waitingConfirmation")
    public void setKeywordsPt(String text) {
        setKeywordsLanguage("pt", normalizeKeywords(text));
    }
    
    public String getKeywordsEn() {
        return getKeywordsLanguage("en");
    }
    
    @Checked("ThesisPredicates.waitingConfirmation")
    public void setKeywordsEn(String text) {
        setKeywordsLanguage("en", normalizeKeywords(text));
    }
    
    private String normalizeKeywords(String keywords) {
        StringBuilder builder = new StringBuilder();
        
        if (keywords == null) {
            return null;
        }
        else {
            for (String part : keywords.split(",")) {
                String trimmed = part.trim();
                
                if (trimmed.length() != 0) {
                    if (builder.length() != 0) {
                        builder.append(", ");
                    }
                    
                    builder.append(trimmed);
                }
            }
        }
        
        return builder.toString();
    }
    
    public String getKeywordsLanguage(String language) {
        MultiLanguageString thesisAbstract = getKeywords();
        
        if (thesisAbstract == null) {
            return null;
        }
        else {
            Language realLanguage = Language.valueOf(language);
            String value = thesisAbstract.getContent(realLanguage);
            
            if (value == null && value.length() == 0) {
                return null;
            }
            else {
                return value;
            }
        }
    }
    
    public void setKeywordsLanguage(String language, String text) {
        MultiLanguageString keywords = getKeywords();
        Language realLanguage = Language.valueOf(language);
        
        if (keywords == null) {
            setKeywords(new MultiLanguageString(realLanguage, text));
        }
        else {
            keywords.setContent(realLanguage, text);
            setKeywords(keywords);
        }
    }

    public void setOrientator(Person person) {
        if (person == null) {
            removeParticipation(getOrientator());
        }
        else {
            new ThesisEvaluationParticipant(this, person, ThesisParticipationType.ORIENTATOR);
        }
    }

    public void setCoorientator(Person person) {
        if (person == null) {
            removeParticipation(getCoorientator());
        }
        else {
            new ThesisEvaluationParticipant(this, person, ThesisParticipationType.COORIENTATOR);
        }
    }

    public void setPresident(Person person) {
        if (person == null) {
            removeParticipation(getPresident());
        }
        else {
            new ThesisEvaluationParticipant(this, person, ThesisParticipationType.PRESIDENT);
        }
    }

    public void addVowel(Person person) {
        if (person != null) {
            new ThesisEvaluationParticipant(this, person, ThesisParticipationType.VOWEL);
        }
    }

    public void removeVowel(Person person) {
        ThesisEvaluationParticipant participant = findVowel(person);
        
        if (participant != null) {
            participant.delete();
        }
    }

    private ThesisEvaluationParticipant findVowel(Person person) {
        for (ThesisEvaluationParticipant thesisEvaluationParticipant : getVowels()) {
            if (thesisEvaluationParticipant.getPerson() == person) {
                return thesisEvaluationParticipant;
            }
        }
        
        return null;
    }

}
