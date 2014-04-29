package net.sourceforge.fenixedu.domain.thesis;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.ThesisPredicates;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Thesis extends Thesis_Base {

    static {
        getRelationThesisEnrolment().addListener(new RelationAdapter<Thesis, Enrolment>() {

            @Override
            public void beforeAdd(Thesis thesis, Enrolment enrolment) {
                super.beforeAdd(thesis, enrolment);

                if (thesis == null || enrolment == null) {
                    return;
                }

                Collection<Thesis> theses = enrolment.getTheses();

                String number = enrolment.getStudentCurricularPlan().getRegistration().getNumber().toString();
                switch (theses.size()) {
                case 0: // can have at least one
                    return;
                case 1: // can have another if existing is not final
                    Thesis existing = theses.iterator().next();
                    if (existing.isFinalThesis() || !existing.isEvaluated()) {
                        throw new DomainException("thesis.enrolment.thesis.notEvaluated", number);
                    }
                default: // never more than 2
                    throw new DomainException("thesis.enrolment.hasFinalThesis", number);
                }
            }

        });
    }

    public static final Comparator<Thesis> COMPARATOR_BY_STUDENT = new Comparator<Thesis>() {
        @Override
        public int compare(Thesis t1, Thesis t2) {
            final int n = Student.NUMBER_COMPARATOR.compare(t1.getStudent(), t2.getStudent());
            return n == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(t1, t2) : n;
        }
    };

    private final static double CREDITS = 1;

    public static class ThesisCondition {
        private final String key;

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

        setRootDomainObject(Bennu.getInstance());
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

        checkIsScientificCommissionMember();
    }

    private void checkIsScientificCommissionMember() {
        final Enrolment enrolment = getEnrolment();
        final ExecutionYear executionYear = enrolment.getExecutionYear();
        final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfStudent();
        final Person person = AccessControl.getPerson();

        if (person != null) {
            for (final ScientificCommission scientificCommission : person.getScientificCommissionsSet()) {
                final ExecutionDegree executionDegree = scientificCommission.getExecutionDegree();
                if (executionDegree.getExecutionYear() == executionYear
                        && executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
                    return;
                }
            }
        }
        throw new DomainException("degree.scientificCommission.notMember");
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
        } else {
            final Language dlanguage = dissertation.getLanguage();
            final Language language = dlanguage == null ? Language.getDefaultLanguage() : dlanguage;
            return new MultiLanguageString(language, dissertation.getTitle());
        }
    }

    public MultiLanguageString getFinalSubtitle() {
        ThesisFile dissertation = getDissertation();

        if (dissertation == null) {
            return null;
        } else {

            String subTitle = dissertation.getSubTitle();

            if (subTitle == null) {
                return null;
            }

            return new MultiLanguageString(dissertation.getLanguage(), subTitle);
        }
    }

    public void setFinalTitle(final MultiLanguageString finalTitle) {
        setTitle(finalTitle);
        final ThesisFile dissertation = getDissertation();
        if (dissertation != null) {
            final Language language = dissertation.getLanguage();
            if (language == null) {
                dissertation.setLanguage(finalTitle.getContentLanguage());
                dissertation.setTitle(finalTitle.getContent());
            } else {
                final String content = finalTitle.getContent(language);
                if (content == null) {
                    dissertation.setLanguage(finalTitle.getContentLanguage());
                    dissertation.setTitle(finalTitle.getContent());
                } else {
                    dissertation.setTitle(content);
                }
            }
        }
    }

    public void setFinalSubtitle(final MultiLanguageString finalSubtitle) {
        final ThesisFile dissertation = getDissertation();
        if (dissertation != null) {
            final Language language = dissertation.getLanguage();
            if (language == null) {
                dissertation.setLanguage(finalSubtitle.getContentLanguage());
                dissertation.setSubTitle(finalSubtitle.getContent());
            } else {
                final String content = finalSubtitle.getContent(language);
                if (content == null) {
                    dissertation.setLanguage(finalSubtitle.getContentLanguage());
                    dissertation.setSubTitle(finalSubtitle.getContent());
                } else {
                    dissertation.setSubTitle(content);
                }
            }
        }
    }

    public Language getLanguage() {
        ThesisFile dissertation = getDissertation();
        return dissertation == null ? null : dissertation.getLanguage();
    }

    final public MultiLanguageString getFinalFullTitle() {
        final ThesisFile dissertation = getDissertation();

        if (dissertation == null) {
            return getTitle();
        } else {
            final StringBuilder result = new StringBuilder();
            result.append(dissertation.getTitle());
            result.append(StringUtils.isEmpty(dissertation.getSubTitle()) ? "" : ": " + dissertation.getSubTitle());
            final Language language = dissertation.getLanguage();
            return language == null ? new MultiLanguageString(result.toString()) : new MultiLanguageString(language,
                    result.toString());
        }
    }

    @Override
    public void setDiscussed(DateTime discussed) {
        check(this, ThesisPredicates.studentOrAcademicAdministrativeOfficeOrScientificCouncil);
        if (discussed != null && getEnrolment().getCreationDateDateTime().isAfter(discussed)) {
            throw new DomainException("thesis.invalid.discussed.date.time");
        }
        super.setDiscussed(discussed);
    }

    @Override
    public void setThesisAbstract(MultiLanguageString thesisAbstract) {
        check(this, ThesisPredicates.waitingConfirmation);
        super.setThesisAbstract(thesisAbstract);
    }

    @Override
    public void setKeywords(MultiLanguageString keywords) {
        check(this, ThesisPredicates.waitingConfirmation);
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
        check(this, ThesisPredicates.isScientificCommission);

        if (!canBeDeleted()) {
            throw new DomainException("thesis.delete.notDraft");
        }

        setRootDomainObject(null);

        for (; !getParticipations().isEmpty(); getParticipations().iterator().next().delete()) {
            ;
        }

        // Unnecessary, the student could not submit files while in draft
        // setDissertation(null);
        // setExtendedAbstract(null);

        setDegree(null);
        removeEnrolment();

        deleteDomainObject();
    }

    public boolean canBeDeleted() {
        return getState() == ThesisState.DRAFT && getReaders() == null;
    }

    protected static Collection<Thesis> getThesisInState(Degree degree, ExecutionYear year, ThesisState state) {
        List<Thesis> theses = new ArrayList<Thesis>();

        for (Thesis thesis : Bennu.getInstance().getThesesSet()) {
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

        return result;
    }

    public static Collection<Thesis> getRevisionThesis() {
        return getRevisionThesis(null);
    }

    public static Collection<Thesis> getRevisionThesis(Degree degree) {
        return getRevisionThesis(degree, null);
    }

    public static Collection<Thesis> getRevisionThesis(Degree degree, ExecutionYear executionYear) {
        List<Thesis> result = new ArrayList<Thesis>();
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

    // the credits calculation always depends on the current credits' value for
    // the thesis (no history)
    public static double getCredits() {
        return CREDITS;
    }

    public boolean hasCredits() {
        if (isEvaluated() && hasFinalEnrolmentEvaluation()) {
            return true;
        } else {
            return false;
        }
    }

    private ExecutionDegree getExecutionDegree(final Enrolment enrolment) {
        final ExecutionYear executionYear = enrolment.getExecutionYear();
        final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfDegreeModule();
        return degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
    }

    @Override
    public void setEnrolment(Enrolment enrolment) {
        final ExecutionDegree executionDegree = getExecutionDegree(enrolment);
        final YearMonthDay beginThesisCreationPeriod = executionDegree.getBeginThesisCreationPeriod();
        final YearMonthDay endThesisCreationPeriod = executionDegree.getEndThesisCreationPeriod();

        final YearMonthDay today = new YearMonthDay();
        if (beginThesisCreationPeriod == null || beginThesisCreationPeriod.isAfter(today)) {
            throw new DomainException("thesis.creation.not.allowed.because.out.of.period");
        }
        if (endThesisCreationPeriod != null && endThesisCreationPeriod.isBefore(today)) {
            throw new DomainException("thesis.creation.not.allowed.because.out.of.period");
        }

        if (enrolment != null) {
            CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            if (!curricularCourse.isDissertation()) {
                throw new DomainException("thesis.enrolment.notDissertationEnrolment");
            }
        }

        super.setEnrolment(enrolment);
    }

    public void removeEnrolment() {
        super.setEnrolment(null);
    }

    public String getDepartmentName() {
        final CompetenceCourse competenceCourse = getEnrolment().getCurricularCourse().getCompetenceCourse();
        if (getEnrolment().isBolonhaDegree()) {
            return competenceCourse.getDepartmentUnit().getDepartment().getRealName();
        }
        if (competenceCourse.hasAnyDepartments()) {
            return competenceCourse.getDepartments().iterator().next().getRealName();
        }
        return null;
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

    // / DRAFT -> SUBMITTED
    public void submit() {
        check(this, ThesisPredicates.isScientificCommission);
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.submit.notDraft");
        }

        if (!isValid()) {
            throw new DomainException("thesis.submit.hasConditions");
        }

        Person person = AccessControl.getPerson();

        if (!person.hasTeacher()) {
            throw new DomainException("thesis.submit.needsTeacher");
        }

        setSubmission(new DateTime());
        setSubmitter(person);

        setRejectionComment(null);
        setState(ThesisState.SUBMITTED);
    }

    // / SUBMITTED -> DRAFT
    public void cancelSubmit() {
        check(this, ThesisPredicates.isScientificCommission);
        switch (getState()) {
        case SUBMITTED:
            break;
        case APPROVED:
            throw new DomainException("thesis.submit.cancel.alreadyApproved");
        case DRAFT:
            if (isRejected()) {
                throw new DomainException("thesis.submit.cancel.alreadyRejected");
            }
        default:
            throw new DomainException("thesis.submit.cancel.unable");
        }

        setSubmission(null); // really undo step
        setSubmitter(null);

        setState(ThesisState.DRAFT);
    }

    public boolean isValid() {
        return getConditions().isEmpty();
    }

    // SUBMITTED -> DRAFT
    // public void reject() {
    // if (getState() != ThesisState.SUBMITTED) {
    // throw new DomainException("thesis.approve.notSubmitted");
    // }
    //
    // setSubmitter(null);
    // setState(ThesisState.DRAFT);
    // }

    // SUBMITTED -> APPROVED
    public void approveProposal() {
        check(this, ThesisPredicates.isScientificCouncilOrCoordinatorAndNotOrientatorOrCoorientator);
        if (getState() != ThesisState.APPROVED) {
            if (getState() != ThesisState.SUBMITTED) {
                throw new DomainException("thesis.approve.notSubmitted");
            }

            setApproval(new DateTime());
            setProposalApprover(AccessControl.getPerson());

            setState(ThesisState.APPROVED);
        }
    }

    // (SUBMITTED | APPROVED) -> DRAFT
    public void rejectProposal(String rejectionComment) {
        check(this, ThesisPredicates.isScientificCouncilOrCoordinatorAndNotOrientatorOrCoorientator);
        if (getState() != ThesisState.SUBMITTED && getState() != ThesisState.APPROVED) {
            throw new DomainException("thesis.reject.notSubmittedNorApproved");
        }

        setSubmitter(null);
        setProposalApprover(null);

        setRejectionComment(rejectionComment);
        setState(ThesisState.DRAFT);

        sendRejectionEmail(rejectionComment);
    }

    private void sendRejectionEmail(final String rejectionComment) {

        final Collection<Person> persons = new HashSet<Person>();
        final ExecutionYear executionYear = getEnrolment().getExecutionYear();
        for (ScientificCommission member : getDegree().getScientificCommissionMembers(executionYear)) {
            if (member.isContact()) {
                persons.add(member.getPerson());
            }
        }
        for (final ThesisEvaluationParticipant thesisEvaluationParticipant : getParticipationsSet()) {
            final ThesisParticipationType thesisParticipationType = thesisEvaluationParticipant.getType();
            if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
                    || thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
                persons.add(thesisEvaluationParticipant.getPerson());
            }
        }
        final Recipient recipient = new Recipient("Membros da tese " + getTitle().toString(), new FixedSetGroup(persons));
        final String studentNumber = getStudent().getNumber().toString();
        final String title = getFinalFullTitle().getContent();
        final String subject = getMessage("message.thesis.reject.submission.email.subject", studentNumber);
        final String body = getMessage("message.thesis.reject.submission.email.body", studentNumber, title, rejectionComment);

        //
        final Sender sender = ScientificCouncilUnit.getScientificCouncilUnit().getUnitBasedSender().iterator().next();

        new Message(sender, sender.getConcreteReplyTos(), recipient.asCollection(), subject, body, "");
    }

    protected String getMessage(final String key, final Object... args) {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.ScientificCouncilResources", Language.getLocale());
        final String message = bundle.getString(key);
        return MessageFormat.format(message, args);
    }

    // Not an actual state change... but it is an aparent state change (whatever
    // that means).
    @Override
    public void setConfirmmedDocuments(final DateTime confirmmedDocuments) {
        if (getState() != ThesisState.APPROVED && getState() != ThesisState.REVISION) {
            throw new DomainException("thesis.confirm.notApprovedOrInRevision");
        }

        if (!isThesisAbstractInBothLanguages()) {
            throw new DomainException("thesis.confirm.noAbstract");
        }

        if (!isKeywordsInBothLanguages()) {
            throw new DomainException("thesis.confirm.noKeywords");
        }

        if (!hasExtendedAbstract()) {
            throw new DomainException("thesis.confirm.noExtendedAbstract");
        }

        if (!hasDissertation()) {
            throw new DomainException("thesis.confirm.noDissertation");
        }

        if (getDiscussed() == null) {
            throw new DomainException("thesis.confirm.noDiscussionDate");
        }

        setConfirmer(AccessControl.getPerson());

        super.setConfirmmedDocuments(confirmmedDocuments);
    }

    // APPROVED -> CONFIRMED
    public void confirm(Integer mark) {
        if (getState() != ThesisState.APPROVED && getState() != ThesisState.REVISION) {
            throw new DomainException("thesis.confirm.notApprovedOrInRevision");
        }

        if (!isThesisAbstractInBothLanguages()) {
            throw new DomainException("thesis.confirm.noAbstract");
        }

        if (!isKeywordsInBothLanguages()) {
            throw new DomainException("thesis.confirm.noKeywords");
        }

        if (!hasExtendedAbstract()) {
            throw new DomainException("thesis.confirm.noExtendedAbstract");
        }

        if (!hasDissertation()) {
            throw new DomainException("thesis.confirm.noDissertation");
        }

        if (getDiscussed() == null) {
            throw new DomainException("thesis.confirm.noDiscussionDate");
        }

        setMark(mark);

        setConfirmation(new DateTime());
        setConfirmer(AccessControl.getPerson());

        setState(ThesisState.CONFIRMED);
    }

    // CONFIRMED -> REVISION
    public void allowRevision() {
        if (getState() != ThesisState.REVISION) {
            if (getState() != ThesisState.CONFIRMED) {
                throw new DomainException("thesis.confirm.notConfirmed");
            }

            setConfirmer(null);
            setState(ThesisState.REVISION);
        }
    }

    // CONFIRMED -> EVALUATED
    public void approveEvaluation() {
        if (getState() != ThesisState.EVALUATED) {
            if (getState() != ThesisState.CONFIRMED) {
                throw new DomainException("thesis.confirm.notConfirmed");
            }

            setEvaluation(new DateTime());
            setEvaluationApprover(AccessControl.getPerson());

            setState(ThesisState.EVALUATED);

            updateMarkSheet();

            setDocumentsAvailableAfter(new DateTime().plusMonths(6));
        } else {
            throw new DomainException("thesis.already.evaluated");
        }
    }

    public ThesisLibraryState getLibraryState() {
        if (hasLastLibraryOperation()) {
            return getLastLibraryOperation().getState();
        }
        return ThesisLibraryState.NOT_DEALT;
    }

    /**
     * Do not use this. Create instances of {@link ThesisLibraryOperation} instead.
     */
    public void setLibraryState(ThesisLibraryState state) {
        throw new UnsupportedOperationException();
    }

    public String getLibraryReference() {
        if (hasLastLibraryOperation()) {
            return getLastLibraryOperation().getLibraryReference();
        }
        return null;
    }

    /**
     * Do not use this. Create instances of {@link ThesisLibraryOperation} instead.
     */
    public void setLibraryReference(String reference) {
        throw new UnsupportedOperationException();
    }

    public Person getLibraryOperationPerformer() {
        if (hasLastLibraryOperation()) {
            return getLastLibraryOperation().getPerformer();
        }
        return null;
    }

    /**
     * Do not use this. Create instances of {@link ThesisLibraryOperation} instead.
     */
    public void setLibraryOperationPerformer(Person performer) {
        throw new UnsupportedOperationException();
    }

    public boolean hasFinalEnrolmentEvaluation() {
        return getEnrolment().isApproved();
    }

    /**
     * Generates a new mark sheet in the administrative office or merges the
     * grade for this enrolment in an existing, unconfirmed, mark sheet for this
     * enrolment.
     * 
     * <p>
     * This is only done if there isn't already a MarkSheet with an evaluation for the Enrolment related to this Thesis and if
     * this Thesis has a positive grade or is the second Thesis of the enrolment.
     */
    public void updateMarkSheet() {
        if (hasAnyEvaluations()) {
            return;
        }

        if (!isFinalThesis()) {
            return;
        }

        MarkSheet markSheet = getExistingMarkSheet();

        if (markSheet == null) {
            markSheet = createMarkSheet();
        } else {
            mergeInMarkSheet(markSheet);
        }
    }

    /**
     * Indicates if this thesis is the last one that can be created for this
     * enrolment, that is, if the student can have any other thesis after this
     * one for the same enrolment. A student can present up to two theses in a
     * single enrolment. This corresponds to 2 distinct evaluation chances:
     * first semester, second semester.
     * 
     * @return <code>true</code> if the student can have a second thesis
     */
    public boolean isFinalThesis() {
        final Enrolment enrolment = getEnrolment();
        return enrolment.getTheses().size() != 1 || !getEvaluationMark().getValue().equals(GradeScale.RE);
    }

    /**
     * Same as the above but also ensures that the student had a positive grade.
     * 
     * @return <code>true</code> if the student had a positive grade
     */
    public boolean isFinalAndApprovedThesis() {
        return !getEvaluationMark().getValue().equals(GradeScale.RE);
    }

    /**
     * Verifies if the student has any EnrolmentEvaluation crated by a
     * MarkSheet.
     * 
     * @return <code>true</code> if the Enrolment related to this Thesis has at
     *         least one EnrolmentEvaluation connected to a MarkSheet
     */
    public boolean hasAnyEvaluations() {
        for (EnrolmentEvaluation evaluation : getEnrolment().getEvaluations()) {
            if (!evaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL)) {
                continue;
            }

            if (!evaluation.hasMarkSheet()) {
                continue;
            }

            final String gradeValue = evaluation.getGradeValue();
            final Grade evaluationMark = getEvaluationMark();
            final String evaluationMarkValue = evaluationMark == null ? null : evaluationMark.getValue();
            if (gradeValue.equals(evaluationMarkValue)) {
                return true;
            } else {
                throw new DomainException("thesis.approve.evaluation.has.different.mark", gradeValue);
            }
        }

        return false;
    }

    protected MarkSheet getExistingMarkSheet() {
        CurricularCourse curricularCourse = getEnrolment().getCurricularCourse();

        Teacher teacher = getResponsibleTeacher();

        for (MarkSheet markSheet : curricularCourse.getMarkSheets()) {
            if (getEnrolment().getExecutionPeriod() != markSheet.getExecutionPeriod()) {
                continue;
            }

            if (markSheet.isConfirmed()) {
                continue;
            }

            if (markSheet.getMarkSheetType() != MarkSheetType.SPECIAL_AUTHORIZATION) {
                continue;
            }

            if (markSheet.getResponsibleTeacher() != teacher) {
                continue;
            }

            return markSheet;
        }

        return null;
    }

    private Teacher getResponsibleTeacher() {
        Teacher responsible = getExecutionCourseTeacher();

        if (responsible == null) {
            responsible = AccessControl.getPerson().getTeacher();
        }

        return responsible;
    }

    private void mergeInMarkSheet(MarkSheet markSheet) {
        List<MarkSheetEnrolmentEvaluationBean> empty = Collections.emptyList();
        markSheet.editNormal(empty, getStudentEvalutionBean(), empty);
    }

    protected MarkSheet createMarkSheet() {
        CurricularCourse curricularCourse = getEnrolment().getCurricularCourse();
        ExecutionSemester executionSemester = getEnrolment().getExecutionPeriod();
        Teacher responsible = getExecutionCourseTeacher();
        Date evaluationDate = getDiscussed().toDate();
        MarkSheetType type = MarkSheetType.SPECIAL_AUTHORIZATION;

        if (responsible == null) {
            responsible = AccessControl.getPerson().getTeacher();
        }

        List<MarkSheetEnrolmentEvaluationBean> evaluations = getStudentEvalutionBean();

        return curricularCourse.createNormalMarkSheet(executionSemester, responsible, evaluationDate, type, true, evaluations,
                responsible.getPerson());
    }

    private List<MarkSheetEnrolmentEvaluationBean> getStudentEvalutionBean() {
        return Arrays.asList(new MarkSheetEnrolmentEvaluationBean(getEnrolment(), getDiscussed().toDate(), getEvaluationMark()));
    }

    private Teacher getExecutionCourseTeacher() {
        final List<Teacher> teachers = new ArrayList<Teacher>();

        final ExecutionCourse executionCourse = getExecutionCourse();
        if (executionCourse != null) {
            for (Professorship professorship : executionCourse.getProfessorships()) {
                if (professorship.isResponsibleFor() && professorship.hasTeacher()) {
                    return professorship.getTeacher();
                }
            }
        }

        CurricularCourse curricularCourse = getEnrolment().getCurricularCourse();
        Degree degree = curricularCourse.getDegree();

        for (final ExecutionDegree executionDegree : curricularCourse.getDegreeCurricularPlan().getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == getEnrolment().getExecutionYear()) {
                for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    if (coordinator.isResponsible()) {
                        return coordinator.getPerson().getTeacher();
                    }
                }
            }
        }

        return null;
    }

    private ExecutionCourse getExecutionCourse() {
        Enrolment enrolment = getEnrolment();
        CurricularCourse curricularCourse = enrolment.getCurricularCourse();

        List<ExecutionCourse> executionCourses =
                curricularCourse.getExecutionCoursesByExecutionPeriod(enrolment.getExecutionPeriod());
        if (!executionCourses.isEmpty()) {
            return executionCourses.iterator().next();
        }

        executionCourses = curricularCourse.getExecutionCoursesByExecutionYear(enrolment.getExecutionYear());
        if (!executionCourses.isEmpty()) {
            return executionCourses.iterator().next();
        }

        return null;
    }

    private Grade getEvaluationMark() {
        Integer mark = getMark();

        GradeScale scale = getEnrolment().getCurricularCourse().getGradeScaleChain();
        if (scale != GradeScale.TYPE20) {
            throw new DomainException("thesis.grade.type20.expected");
        }

        return Grade.createGrade((mark == null || mark < 10) ? GradeScale.RE : mark.toString(), scale);
    }

    public void acceptDeclaration(ThesisVisibilityType visibility, DateTime availableAfter) {
        if (visibility == null) {
            throw new DomainException("thesis.acceptDeclaration.visibility.required");
        }

        if (!isWaitingConfirmation()) {
            throw new DomainException("thesis.acceptDeclaration.notAllowed");
        }

        setDeclarationAccepted(true);
        setVisibility(visibility);
        setDocumentsAvailableAfter(availableAfter);
        setDeclarationAcceptedTime(new DateTime());
    }

    public void rejectDeclaration() {
        setDeclarationAccepted(false);
        setVisibility(null);
        setDocumentsAvailableAfter(null);
        setDeclarationAcceptedTime(null);

        if (!isWaitingConfirmation()) {
            throw new DomainException("thesis.rejectDeclaration.notAllowed");
        }

        if (hasDissertation()) {
            getDissertation().delete();
        }

        if (hasExtendedAbstract()) {
            getExtendedAbstract().delete();
        }
    }

    @Atomic
    public void swapFilesVisibility() {
        ThesisVisibilityType visibility = getVisibility();

        if (visibility == null) {
            throw new DomainException("thesis.acceptDeclaration.visibility.required");
        }

        FileResultPermittedGroupType groupType;
        if (visibility.equals(ThesisVisibilityType.INTRANET)) {
            setVisibility(ThesisVisibilityType.PUBLIC);
            groupType = FileResultPermittedGroupType.PUBLIC;
        } else {
            setVisibility(ThesisVisibilityType.INTRANET);
            groupType = FileResultPermittedGroupType.INSTITUTION;
        }
        final net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication = getPublication();
        if (publication != null) {
            for (final ResearchResultDocumentFile researchResultDocumentFile : publication.getResultDocumentFilesSet()) {
                researchResultDocumentFile.setFileResultPermittedGroupType(groupType);
            }
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

    public boolean isSubmittedAndIsCoordinatorAndNotOrientator() {
        return isSubmitted() && isCoordinatorAndNotOrientator();
    }

    public boolean isCoordinatorAndNotOrientator() {
        final Person loggedPerson = AccessControl.getPerson();
        return loggedPerson != null && isCoordinator(loggedPerson) && !isOrientatorOrCoorientator(loggedPerson);
    }

    public boolean isCoordinator() {
        final Person loggedPerson = AccessControl.getPerson();
        return loggedPerson != null && isCoordinator(loggedPerson);
    }

    private boolean isCoordinator(final Person loggedPerson) {
        for (final Coordinator coordinator : loggedPerson.getCoordinatorsSet()) {
            if (coordinator.isResponsible() && getDegree() == coordinator.getExecutionDegree().getDegree()) {
                return true;
            }
        }
        return false;
    }

    private boolean isOrientatorOrCoorientator(final Person person) {
        for (final ThesisEvaluationParticipant thesisEvaluationParticipant : getParticipationsSet()) {
            final ThesisParticipationType type = thesisEvaluationParticipant.getType();
            if ((type == ThesisParticipationType.ORIENTATOR || type == ThesisParticipationType.COORIENTATOR)
                    && thesisEvaluationParticipant.getPerson() == person) {
                return true;
            }
        }
        return false;
    }

    public boolean isConfirmed() {
        return getState() == ThesisState.CONFIRMED;
    }

    public boolean isEvaluated() {
        return getState() == ThesisState.EVALUATED;
    }

    public boolean isInRevision() {
        return getState() == ThesisState.REVISION;
    }

    public boolean isRejected() {
        return isDraft() && getSubmission() != null;
    }

    public void removeMark() {
        super.setMark(null);
    }

    @Override
    public void setMark(Integer mark) {
        if (!isMarkValid(mark)) {
            throw new DomainException("thesis.mark.invalid");
        }

        super.setMark(mark);
    }

    public boolean isMarkValid(Integer mark) {
        GradeScale scale = getEnrolment().getCurricularCourse().getGradeScaleChain();

        if (scale == null) {
            scale = GradeScale.TYPE20;
        }

        return scale.isValid(mark.toString(), EvaluationType.EXAM_TYPE); // TODO:
        // thesis
        // ,
        // check
        // grade
        // type
    }

    private Person getParticipationPerson(ThesisEvaluationParticipant participant) {
        if (participant == null) {
            return null;
        } else {
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

        if (!isThesisAbstractInBothLanguages()) {
            conditions.add(new ThesisCondition("thesis.student.abstract.missing"));
        }

        if (!isKeywordsInBothLanguages()) {
            conditions.add(new ThesisCondition("thesis.student.keywords.missing"));
        }

        if (isDeclarationAccepted()) {
            if (getDissertation() == null) {
                conditions.add(new ThesisCondition("thesis.student.dissertation.missing"));
            }

            if (getExtendedAbstract() == null) {
                conditions.add(new ThesisCondition("thesis.student.extendedAbstract.missing"));
            }
        } else {
            conditions.add(new ThesisCondition("thesis.student.declaration.notAccepted"));
        }

        return conditions;
    }

    public List<ThesisCondition> getConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();

        conditions.addAll(getGeneralConditions());
        conditions.addAll(getOrientationConditions());
        conditions.addAll(getJuryConditions());

        return conditions;
    }

    public Collection<ThesisCondition> getGeneralConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();

        // check too few persons
        int count = getJuryPersonCount();
        if (count < 3) {
            conditions.add(new ThesisCondition("thesis.condition.people.number.few"));
        }

        // check too many persons
        if (count > 5) {
            conditions.add(new ThesisCondition("thesis.condition.people.number.exceeded"));
        }

        return conditions;
    }

    public List<ThesisCondition> getOrientationConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();

        Person orientator = getParticipationPerson(getOrientator());
        Person coorientator = getParticipationPerson(getCoorientator());

        Integer orientatorCreditsDistribution = getOrientatorCreditsDistribution();

        if (orientator == null) {
            conditions.add(new ThesisCondition("thesis.condition.orientator.required"));
        } else {

            if (orientatorCreditsDistribution != null) {
                if ((orientatorCreditsDistribution < 20) || (coorientator != null && getCoorientatorCreditsDistribution() < 20)) {
                    conditions.add(new ThesisCondition("thesis.condition.orientation.credits.low"));
                }
            } else if (isCreditsDistributionNeeded()) {
                conditions.add(new ThesisCondition("thesis.condition.orientation.credits.notDefined"));
            }

            // check for duplicated persons
            if (orientator == coorientator) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.orientation"));
            }
        }

        return conditions;
    }

    public List<ThesisCondition> getJuryConditions() {
        List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();

        Person president = getParticipationPerson(getPresident());

        if (president == null) {
            conditions.add(new ThesisCondition("thesis.condition.president.required"));
        } else {
            if (president.hasExternalContract()) {
                conditions.add(new ThesisCondition("thesis.condition.president.notInternal"));
            } else {
                boolean isMember = false;

                final Enrolment enrolment = getEnrolment();
                final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfDegreeModule();
                final ExecutionYear executionYear = enrolment.getExecutionYear();
                final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
                if (executionDegree != null) {
                    for (ScientificCommission member : executionDegree.getScientificCommissionMembersSet()) {
                        isMember = isMember || president == member.getPerson();

                        if (isMember) {
                            break;
                        }
                    }
                }

                if (!isMember) {
                    conditions.add(new ThesisCondition("thesis.condition.president.scientificCommission.notMember"));
                }
            }
        }

        if (getVowels().size() < 2) {
            conditions.add(new ThesisCondition("thesis.condition.people.vowels.two.required"));
        } else {
            // check duplicated person
            Set<Person> vowelsPersons = new HashSet<Person>();
            for (ThesisEvaluationParticipant vowel : getVowels()) {
                vowelsPersons.add(vowel.getPerson());
            }

            if (getVowels().size() != vowelsPersons.size()) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.vowels"));
            }

            if (president != null && vowelsPersons.contains(president)) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.vowels.president"));
            }

        }

        return conditions;
    }

    private int getJuryPersonCount() {
        Set<Person> persons = new HashSet<Person>();

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

    public void setThesisAbstractPt(String text) {
        check(this, ThesisPredicates.waitingConfirmation);
        setThesisAbstractLanguage("pt", text);
    }

    public String getThesisAbstractEn() {
        return getThesisAbstractLanguage("en");
    }

    public void setThesisAbstractEn(String text) {
        check(this, ThesisPredicates.waitingConfirmation);
        setThesisAbstractLanguage("en", text);
    }

    public String getThesisAbstractLanguage(String language) {
        MultiLanguageString thesisAbstract = getThesisAbstract();

        if (thesisAbstract == null) {
            return null;
        } else {
            Language realLanguage = Language.valueOf(language);
            String value = thesisAbstract.getContent(realLanguage);

            if (value == null || value.length() == 0) {
                return null;
            } else {
                return value;
            }
        }
    }

    public void setThesisAbstractLanguage(String language, String text) {
        MultiLanguageString thesisAbstract = getThesisAbstract();
        Language realLanguage = Language.valueOf(language);

        if (thesisAbstract == null) {
            setThesisAbstract(new MultiLanguageString(realLanguage, text));
        } else {
            thesisAbstract = thesisAbstract.with(realLanguage, text);
            setThesisAbstract(thesisAbstract);
        }

        if (hasPublication()) {
            getPublication().replaceAbstract(thesisAbstract);
        }
    }

    public String getKeywordsPt() {
        return getKeywordsLanguage("pt");
    }

    public void setKeywordsPt(String text) {
        check(this, ThesisPredicates.waitingConfirmation);
        setKeywordsLanguage("pt", normalizeKeywords(text));
    }

    public String getKeywordsEn() {
        return getKeywordsLanguage("en");
    }

    public void setKeywordsEn(String text) {
        check(this, ThesisPredicates.waitingConfirmation);
        setKeywordsLanguage("en", normalizeKeywords(text));
    }

    private String normalizeKeywords(String keywords) {
        StringBuilder builder = new StringBuilder();

        if (keywords == null) {
            return null;
        } else {
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
        } else {
            Language realLanguage = Language.valueOf(language);
            String value = thesisAbstract.getContent(realLanguage);

            if (value == null || value.length() == 0) {
                return null;
            } else {
                return value;
            }
        }
    }

    public void setKeywordsLanguage(String language, String text) {
        if (text != null) {
            final String[] parts = text.split(",");
            if (parts.length > 6) {
                throw new DomainException("label.message.thesis.keyword.number.of.words.exceeded");
            } else {
                for (final String part : parts) {
                    if (part.length() > 42) {
                        throw new DomainException("label.message.thesis.keyword.length.limit.exceeded");
                    }
                }
            }
        }

        MultiLanguageString keywords = getKeywords();
        Language realLanguage = Language.valueOf(language);

        if (keywords == null) {
            setKeywords(new MultiLanguageString(realLanguage, text));
        } else {
            keywords = keywords.with(realLanguage, text);
            setKeywords(keywords);
        }
    }

    public void setOrientator(Person person) {
        check(this, ThesisPredicates.isScientificCommissionOrScientificCouncil);
        setParticipation(person, ThesisParticipationType.ORIENTATOR);

        if (!isCreditsDistributionNeeded()) {
            setCoorientatorCreditsDistribution(null);
        }
    }

    public void setCoorientator(Person person) {
        check(this, ThesisPredicates.isScientificCommissionOrScientificCouncil);
        setParticipation(person, ThesisParticipationType.COORIENTATOR);

        if (!isCreditsDistributionNeeded()) {
            setCoorientatorCreditsDistribution(null);
        }
    }

    public void setPresident(Person person) {
        check(this, ThesisPredicates.isScientificCommissionOrScientificCouncil);
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
        } else {
            new ThesisEvaluationParticipant(this, person, type);
        }
    }

    public void addVowel(Person person) {
        check(this, ThesisPredicates.isScientificCommissionOrScientificCouncil);
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
        return person != null && !person.hasExternalContract() && person.hasTeacher();
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

    public DateTime getCurrentDiscussedDate() {
        if (isConfirmed() || isEvaluated() || isInRevision()) {
            return getDiscussed();
        }
        return getProposedDiscussed();
    }

    @Override
    public void setState(final ThesisState state) {
        if (hasFutureEnrolment()) {
            throw new DomainException("thesis.has.enrolment.in.future");
        }
        if (state == ThesisState.REVISION) {
            super.setConfirmmedDocuments(null);
        }
        super.setState(state);
    }

    protected boolean hasFutureEnrolment() {
        final Enrolment enrolment = getEnrolment();
        if (enrolment != null) {
            final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
            for (final Enrolment otherEnrolment : studentCurricularPlan.getEnrolments(curricularCourse)) {
                if (otherEnrolment.getExecutionPeriod().isAfter(executionSemester)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Set<Thesis> getThesesByParticipationType(final Person person,
            final ThesisParticipationType thesisParticipationType) {
        final Set<Thesis> theses = new HashSet<Thesis>();
        for (final ThesisEvaluationParticipant thesisEvaluationParticipant : person.getThesisEvaluationParticipantsSet()) {
            if (thesisParticipationType == thesisEvaluationParticipant.getType()) {
                final Thesis thesis = thesisEvaluationParticipant.getThesis();
                theses.add(thesis);
            }
        }
        return theses;
    }

    public boolean hasCurrentDiscussedDate() {
        return getCurrentDiscussedDate() != null;
    }

    public void checkIsScientificCommission() {
        check(this, ThesisPredicates.isScientificCommission);
        // Nothing to do here... just the access control stuff that is injected
        // whenever necessary
    }

    public ExecutionYear getExecutionYear() {
        return getEnrolment().getExecutionYear();
    }

    public boolean isAnual() {
        final Enrolment enrolment = getEnrolment();
        return enrolment.isAnual();
    }

    public boolean getHasMadeProposalPreviousYear() {
        ExecutionYear enrolmentExecutionYear = getEnrolment().getExecutionYear();
        for (GroupStudent groupStudent : getEnrolment().getRegistration().getAssociatedGroupStudents()) {
            Proposal proposal = groupStudent.getFinalDegreeWorkProposalConfirmation();
            if (proposal != null && proposal.isForExecutionYear(enrolmentExecutionYear.getPreviousExecutionYear())
                    && proposal.getAttributionStatus().isFinalAttribution()) {
                return true;
            }
        }
        return false;
    }

    public List<Language> getLanguages() {
        final List<Language> result = new ArrayList<Language>();

        add(result, getKeywords());
        add(result, getThesisAbstract());
        add(result, getTitle());

        final ThesisFile dissertation = getDissertation();
        if (dissertation != null) {
            add(result, dissertation.getLanguage());
        }
        final ThesisFile extendedAbstract = getExtendedAbstract();
        if (extendedAbstract != null) {
            add(result, extendedAbstract.getLanguage());
        }

        return result;
    }

    private void add(final List<Language> result, final MultiLanguageString mls) {
        if (mls != null) {
            for (final Language language : mls.getAllLanguages()) {
                add(result, language);
            }
        }
    }

    private void add(final List<Language> result, final Language language) {
        if (language != null && !result.contains(language)) {
            if (language == Language.pt) {
                result.add(0, language);
            } else if (language == Language.en) {
                if (result.size() > 0 && result.iterator().next() == Language.pt) {
                    result.add(1, language);
                } else if (result.size() > 0) {
                    result.add(0, language);
                } else {
                    result.add(language);
                }
            } else {
                result.add(language);
            }
        }
    }

    public boolean areThesisFilesReadable() {
        final ThesisFile thesisFile = getDissertation();
        return thesisFile != null && thesisFile.areThesisFilesReadable();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant> getParticipations() {
        return getParticipationsSet();
    }

    @Deprecated
    public boolean hasAnyParticipations() {
        return !getParticipationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasConfirmation() {
        return getConfirmation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEnrolment() {
        return getEnrolment() != null;
    }

    @Deprecated
    public boolean hasSubmission() {
        return getSubmission() != null;
    }

    @Deprecated
    public boolean hasDeclarationAcceptedTime() {
        return getDeclarationAcceptedTime() != null;
    }

    @Deprecated
    public boolean hasDeclarationAccepted() {
        return getDeclarationAccepted() != null;
    }

    @Deprecated
    public boolean hasEvaluation() {
        return getEvaluation() != null;
    }

    @Deprecated
    public boolean hasExtendedAbstract() {
        return getExtendedAbstract() != null;
    }

    @Deprecated
    public boolean hasMark() {
        return getMark() != null;
    }

    @Deprecated
    public boolean hasDocumentsAvailableAfter() {
        return getDocumentsAvailableAfter() != null;
    }

    @Deprecated
    public boolean hasKeywords() {
        return getKeywords() != null;
    }

    @Deprecated
    public boolean hasThesisAbstract() {
        return getThesisAbstract() != null;
    }

    @Deprecated
    public boolean hasRejectionComment() {
        return getRejectionComment() != null;
    }

    @Deprecated
    public boolean hasDiscussed() {
        return getDiscussed() != null;
    }

    @Deprecated
    public boolean hasSite() {
        return getSite() != null;
    }

    @Deprecated
    public boolean hasCreation() {
        return getCreation() != null;
    }

    @Deprecated
    public boolean hasConfirmmedDocuments() {
        return getConfirmmedDocuments() != null;
    }

    @Deprecated
    public boolean hasProposedPlace() {
        return getProposedPlace() != null;
    }

    @Deprecated
    public boolean hasComment() {
        return getComment() != null;
    }

    @Deprecated
    public boolean hasLastLibraryOperation() {
        return getLastLibraryOperation() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasBennuFromPendingPublication() {
        return getRootDomainObjectFromPendingPublication() != null;
    }

    @Deprecated
    public boolean hasPublication() {
        return getPublication() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasProposedDiscussed() {
        return getProposedDiscussed() != null;
    }

    @Deprecated
    public boolean hasApproval() {
        return getApproval() != null;
    }

    @Deprecated
    public boolean hasVisibility() {
        return getVisibility() != null;
    }

    @Deprecated
    public boolean hasOrientatorCreditsDistribution() {
        return getOrientatorCreditsDistribution() != null;
    }

    @Deprecated
    public boolean hasDissertation() {
        return getDissertation() != null;
    }

}
