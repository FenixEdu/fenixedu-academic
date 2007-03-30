package net.sourceforge.fenixedu.domain.thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
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
        setDeclarationAccepted(false);
        
        create();
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
    
    public boolean isDeclarationAccepted() {
        Boolean accepted = getDeclarationAccepted();
        
        return accepted == null ? false : accepted;
    }
    
    @Override
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.isEmpty()) {
            throw new FieldIsRequiredException("title", "thesis.title.required");
        }
        
        super.setTitle(title);
    }

    public MultiLanguageString getFinalTitle() {
        ThesisFile dissertation = getDissertation();
        
        if (dissertation == null) {
            return getTitle();
        }
        else {
            return new MultiLanguageString(dissertation.getLanguage(), dissertation.getTitle());
        }
    }
    
    public MultiLanguageString getFinalSubtitle() {
        ThesisFile dissertation = getDissertation();
        
        if (dissertation == null) {
            return null;
        }
        else {
            return new MultiLanguageString(dissertation.getLanguage(), dissertation.getTitle());
        }
    }
    
    @Override
    @Checked("ThesisPredicates.student")
    public void setDiscussed(DateTime discussed) {
        super.setDiscussed(discussed);
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

    public List<ThesisEvaluationParticipant> getOrientation() {
        ThesisEvaluationParticipant orientator = getOrientator();
        ThesisEvaluationParticipant coorientator = getCoorientator();

        List<ThesisEvaluationParticipant> result = new ArrayList<ThesisEvaluationParticipant>();
        
        if (orientator != null) {
            result.add(orientator);
        }

        if (coorientator != null) {
            result.add(coorientator);
        }
        
        return result;
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
    
    public ThesisEvaluationParticipant getCreator() {
        return getParticipant(ThesisParticipationType.STATE_CREATOR);
    }
    
    public ThesisEvaluationParticipant getSubmitter() {
        return getParticipant(ThesisParticipationType.STATE_SUBMITTER);
    }
    
    public ThesisEvaluationParticipant getProposalApprover() {
        return getParticipant(ThesisParticipationType.STATE_PROPOSAL_APPROVER);
    }
    
    public ThesisEvaluationParticipant getConfirmer() {
        return getParticipant(ThesisParticipationType.STATE_CONFIRMER);
    }
    
    public ThesisEvaluationParticipant getEvaluationApprover() {
        return getParticipant(ThesisParticipationType.STATE_EVALUATION_APPROVER);
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

    // -> DRAFT
    private void create() {
        setCreation(new DateTime());
        setCreator(AccessControl.getPerson());
        
        setState(ThesisState.DRAFT);
    }
    
    /// DRAFT -> SUBMITTED
    public void submit() {
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.submit.notDraft");
        }
        
        if (! isValid()) {
            throw new DomainException("thesis.submit.hasConditions");
        }
        
        Person person = AccessControl.getPerson();
        
        if (! person.hasTeacher()) {
            throw new DomainException("thesis.submit.needsTeacher");
        }
        
        setSubmission(new DateTime());
        setSubmitter(person);
        
        setRejectionComment(null);
        setState(ThesisState.SUBMITTED);
    }

    public boolean isValid() {
        return getConditions().isEmpty();
    }

    // SUBMITTED -> DRAFT
    public void reject() {
        if (getState() != ThesisState.SUBMITTED) {
            throw new DomainException("thesis.approve.notSubmitted");
        }
        
        setSubmitter(null);
        setState(ThesisState.DRAFT);
    }

    // SUBMITTED -> APPROVED
    public void approveProposal() {
        if (getState() != ThesisState.SUBMITTED) {
            throw new DomainException("thesis.approve.notSubmitted");
        }
        
        setApproval(new DateTime());
        setProposalApprover(AccessControl.getPerson());
        
        setState(ThesisState.APPROVED);
    }
    
    // (SUBMITTED | APPROVED) -> DRAFT
    @Checked("ThesisPredicates.isScientificCouncil")
    public void rejectProposal(String rejectionComment) {
        	if (getState() != ThesisState.SUBMITTED && getState() != ThesisState.APPROVED) {
        	    throw new DomainException("thesis.reject.notSubmittedNorApproved");
        	}

        setSubmitter(null);
        setProposalApprover(null);
        
        	setRejectionComment(rejectionComment);
        	setState(ThesisState.DRAFT);
    }
    
    // APPROVED -> CONFIRMED
    public void confirm(Integer mark) {
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
        
        setMark(mark);

        setConfirmation(new DateTime());
        setConfirmer(AccessControl.getPerson());
        
        setState(ThesisState.CONFIRMED);
    }
    
    // CONFIRMED -> REVISION
    public void allowRevision() {
        if (getState() != ThesisState.CONFIRMED) {
            throw new DomainException("thesis.confirm.notConfirmed");
        }

        setConfirmer(null);
        setState(ThesisState.REVISION);
    }
    
    // CONFIRMED -> EVALUATED
    public void approveEvaluation() {
        if (getState() != ThesisState.CONFIRMED) {
            throw new DomainException("thesis.confirm.notConfirmed");
        }
        
        setEvaluation(new DateTime());
        setEvaluationApprover(AccessControl.getPerson());
        
        setState(ThesisState.EVALUATED);
    }
    
    public void acceptDeclaration(ThesisVisibilityType visibility) {
        if (visibility == null) {
            throw new DomainException("thesis.acceptDeclaration.visibility.required");
        }

        if (! isWaitingConfirmation()) {
            throw new DomainException("thesis.acceptDeclaration.notAllowed");
        }
        
        setDeclarationAccepted(true);
        setVisibility(visibility);
        setDeclarationAcceptedTime(new DateTime());
    }
    
    public void rejectDeclaration() {
        setDeclarationAccepted(false);
        setVisibility(null);
        setDeclarationAcceptedTime(null);
        
        if (! isWaitingConfirmation()) {
            throw new DomainException("thesis.rejectDeclaration.notAllowed");
        }
        
        if (hasDissertation()) {
            getDissertation().delete();
        }
        
        if (hasExtendedAbstract()) {
            getExtendedAbstract().delete();
        }
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
    
    public boolean isEvaluated() {
        return getState() == ThesisState.EVALUATED;
    }

    public boolean isRejected() {
        return isDraft() && getSubmission() != null;
    }

    @Override
    public void setMark(Integer mark) {
        if (! isMarkValid(mark)) {
            throw new DomainException("thesis.mark.invalid");
        }
        
        super.setMark(mark);
    }

    public boolean isMarkValid(Integer mark) {
        GradeScale scale = getEnrolment().getCurricularCourse().getGradeScaleChain();
        
        if (scale == null) {
            scale = GradeScale.TYPE20;
        }
        
        return scale.isValid(mark.toString(), EvaluationType.FINAL_TYPE);
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

    public List<ThesisCondition> getStudentConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();

        if (getDiscussed() == null) {
            conditions.add(new ThesisCondition("thesis.student.discussionDate.missing"));
        }

        if (! isThesisAbstractInBothLanguages()) {
            conditions.add(new ThesisCondition("thesis.student.abstract.missing"));
        }
        
        if (! isKeywordsInBothLanguages()) {
            conditions.add(new ThesisCondition("thesis.student.keywords.missing"));
        }
        
        if (isDeclarationAccepted()) {
            if (getDissertation() == null) {
                conditions.add(new ThesisCondition("thesis.student.dissertation.missing"));
            }
            
            if (getExtendedAbstract() == null) {
                conditions.add(new ThesisCondition("thesis.student.extendedAbstract.missing"));
            }
        }
        else {
            conditions.add(new ThesisCondition("thesis.student.declaration.notAccepted"));
        }
        
        return conditions;
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

        Set<Person> vowelsPersons = new HashSet<Person>();
        for (ThesisEvaluationParticipant vowel : getVowels()) {
            vowelsPersons.add(vowel.getPerson());
        }
        
        for (Person person : Arrays.asList(orientator, coorientator, president)) {
            if (person != null && vowelsPersons.contains(person)) {
                result.add(new ThesisCondition("thesis.condition.people.repeated.vowels.inOtherPosition"));
                break;
            }
        }
        
        // check situation where orientator is filled, president is filled, we
        // have 1 vowel but still need one more person because orientator is
        // the same as the president
        int count = getJuryPersonCount();
        if (count == 2 && orientator != null && orientator == president) {
            result.add(new ThesisCondition("thesis.condition.people.few"));
        }
        
        if (isCreditsDistributionNeeded() && getOrientatorCreditsDistribution() == null) {
            result.add(new ThesisCondition("thesis.condition.orientation.credits.notDefined"));
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
            
            // check for duplicated persons
            if (orientator == coorientator) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.coordination"));
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
                boolean isMember = false;
                
                for (ScientificCommission member : getDegree().getCurrentScientificCommissionMembers()) {
                    isMember = isMember || president == member.getPerson();
                    
                    if (isMember) {
                        break;
                    }
                }
                
                if (! isMember) {
                    conditions.add(new ThesisCondition("thesis.condition.president.scientificCommission.notMember"));
                }
            }
        }
        
        return conditions;
    }
    
    public List<ThesisCondition> getVowelsConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();
        
        if (getVowels().isEmpty()) {
            conditions.add(new ThesisCondition("thesis.condition.people.vowels.one.required"));
        }
        else {
            // check duplicated person
            Set<Person> vowelsPersons = new HashSet<Person>();
            for (ThesisEvaluationParticipant vowel : getVowels()) {
                vowelsPersons.add(vowel.getPerson());
            }
            
            if (getVowels().size() != vowelsPersons.size()) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.vowels"));
            }
            
            // check too many persons
            int count = getJuryPersonCount();
            if (count > 5) {
                conditions.add(new ThesisCondition("thesis.condition.people.number.exceeded"));
            }
        }
        
        return conditions;
    }

    private int getJuryPersonCount() {
        Set<Person> persons = new HashSet<Person>();
        
        ThesisEvaluationParticipant orientator = getOrientator();
        if (orientator != null) {
            persons.add(orientator.getPerson());
        }
        
        ThesisEvaluationParticipant coorientator = getCoorientator();
        if (coorientator != null) {
            persons.add(coorientator.getPerson());
        }

        ThesisEvaluationParticipant president = getPresident();
        if (president != null) {
            persons.add(president.getPerson());
        }
        
        for (ThesisEvaluationParticipant vowel : getVowels()) {
            persons.add(vowel.getPerson());
        }
        
        return persons.size();
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
        setParticipation(person, ThesisParticipationType.ORIENTATOR);
        
        if (! isCreditsDistributionNeeded()) {
            setCoorientatorCreditsDistribution(null);
        }
    }

    public void setCoorientator(Person person) {
        setParticipation(person, ThesisParticipationType.COORIENTATOR);
        
        if (! isCreditsDistributionNeeded()) {
            setCoorientatorCreditsDistribution(null);
        }
    }

    public void setPresident(Person person) {
        setParticipation(person, ThesisParticipationType.PRESIDENT);
    }

    public void setCreator(Person person) {
        setParticipation(person, ThesisParticipationType.STATE_CREATOR);
    }
    
    public void setSubmitter(Person person) {
        setParticipation(person, ThesisParticipationType.STATE_SUBMITTER);
    }
    
    public void setProposalApprover(Person person) {
        setParticipation(person, ThesisParticipationType.STATE_PROPOSAL_APPROVER);
    }
    
    public void setConfirmer(Person person) {
        setParticipation(person, ThesisParticipationType.STATE_CONFIRMER);
    }
    
    public void setEvaluationApprover(Person person) {
        setParticipation(person, ThesisParticipationType.STATE_EVALUATION_APPROVER);
    }
    
    private void setParticipation(Person person, ThesisParticipationType type) {
        if (person == null) {
            removeParticipation(getParticipant(type));
        }
        else {
            new ThesisEvaluationParticipant(this, person, type);
        }
    }

    public void addVowel(Person person) {
        if (person != null) {
            new ThesisEvaluationParticipant(this, person, ThesisParticipationType.VOWEL);
        }
    }

    public boolean isCreditsDistributionNeeded() {
        return isOrientatorCreditsDistributionNeeded() || isCoorientatorCreditsDistributionNeeded();
    }
    
    public boolean isOrientatorCreditsDistributionNeeded() {
        return isInternalPerson(getParticipationPerson(getOrientator()));
    }

    public boolean isCoorientatorCreditsDistributionNeeded() {
        return isInternalPerson(getParticipationPerson(getCoorientator()));
    }

    private boolean isInternalPerson(Person person) {
        return person != null && !person.hasExternalPerson() && person.hasTeacher();
    }
    
    @Override
    public void setOrientatorCreditsDistribution(Integer percent) {
        if (percent != null && (percent < 0 || percent > 100)) {
            throw new DomainException("thesis.orietation.credits.notValid");
        }
        
        super.setOrientatorCreditsDistribution(percent);
    }
    
    public Integer getCoorientatorCreditsDistribution() {
        Integer distribution = getOrientatorCreditsDistribution();
        
        return distribution != null ? 100 - distribution : null;
    }

    public void setCoorientatorCreditsDistribution(Integer percent) {
        if (percent != null && (percent < 0 || percent > 100)) {
            throw new DomainException("thesis.orietation.credits.notValid");
        }
        
        setOrientatorCreditsDistribution(percent != null ? 100 - percent : null);
    }

}
