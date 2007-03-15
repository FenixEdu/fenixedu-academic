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
    
    public Thesis(Degree degree, Enrolment enrolment) {
        this();
        
        setDegree(degree);
        setEnrolment(enrolment);
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

    public void delete() {
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.delete.notDraft");
        }
        
        removeRootDomainObject();

        if (hasOrientator()) {
            getOrientator().delete();
        }
        if (hasCoorientator()) {
            getCoorientator().delete();
        }
        if (hasPresident()) {
            getPresident().delete();
        }
        for (; !getVowelsSet().isEmpty(); getVowelsSet().iterator().next().delete()); 

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
        if (enrolment == null) {
           throw new FieldIsRequiredException("enrolment", "thesis.enrolment.required");
        }
        
        CurricularCourse curricularCourse = enrolment.getCurricularCourse();
        if (! curricularCourse.isDissertation()) {
            throw new DomainException("thesis.enrolment.notDissertationEnrolment");
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
    
    public boolean isConfirmed() {
        switch (getState()) {
        case CONFIRMED:
        case REVISION:
        case EVALUATED:
            return true;
        default:
            return false;
        }
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
        
        Person orientator = getOrientator().getPerson();
        Person coorientator = getCoorientator().getPerson();
        Person president = getPresident().getPerson();

        // check for duplicated persons
        List<Person> persons = Arrays.asList(orientator, coorientator, president);
        for (final ThesisEvaluationParticipant vowel : getVowelsSet()) {
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
            if (getVowels().isEmpty()) {
                result.add(new ThesisCondition("thesis.condition.people.number.few"));
            }
        }
        
        return result;
    }

    public List<ThesisCondition> getOrientationConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        boolean hasInternal = false;
        
        Person orientator = getOrientator().getPerson();
        Person coorientator = getCoorientator().getPerson();
        
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
        
        Person president = getPresident().getPerson();
        
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

    public boolean isWaitingConfirmation() {
        ThesisState state = getState();
        
        return state == ThesisState.APPROVED || state == ThesisState.REVISION;
    }

    public String getThesisAbstractPt() {
        return getThesisAbstractLanguage("pt");
    }
    
    public void setThesisAbstractPt(String text) {
        setThesisAbstractLanguage("pt", text);
    }
    
    public String getThesisAbstractEn() {
        return getThesisAbstractLanguage("en");
    }
    
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
    
    public void setKeywordsPt(String text) {
        setKeywordsLanguage("pt", normalizeKeywords(text));
    }
    
    public String getKeywordsEn() {
        return getKeywordsLanguage("en");
    }
    
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
                    if (trimmed.length() != 0) {
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

    public void setOrientcator(final Person person) {
        if (hasOrientator()) {
            getOrientator().delete();
        }
        super.setOrientator(new ThesisEvaluationParticipant(person, this, null, null, null));
    }

    public void setCoorientator(final Person person) {
        if (hasCoorientator()) {
            getCoorientator().delete();
        }
        super.setCoorientator(new ThesisEvaluationParticipant(person, null, this, null, null));
    }

    public void setPresident(final Person person) {
        if (hasPresident()) {
            getPresident().delete();
        }
        super.setPresident(new ThesisEvaluationParticipant(person, null, null, this, null));
    }

    public void addVowel(final Person person) {
        super.addVowels(new ThesisEvaluationParticipant(person, null, null, null, this));
    }

    public void removeVowel(final Person person) {
	final ThesisEvaluationParticipant thesisEvaluationParticipant = findVowel(person);
	if (thesisEvaluationParticipant != null) {	    
	    super.addVowels(new ThesisEvaluationParticipant(person, null, null, null, this));
	}
    }

    private ThesisEvaluationParticipant findVowel(final Person person) {
	for (final ThesisEvaluationParticipant thesisEvaluationParticipant : getVowelsSet()) {
	    if (thesisEvaluationParticipant.getPerson() == person) {
		return thesisEvaluationParticipant;
	    }
	}
	return null;
    }

}
