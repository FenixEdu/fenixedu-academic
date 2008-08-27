package net.sourceforge.fenixedu.domain.thesis;

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
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;
import dml.runtime.RelationAdapter;

public class Thesis extends Thesis_Base {

    static {
	ThesisEnrolment.addListener(new RelationAdapter<Thesis, Enrolment>() {

	    @Override
	    public void beforeAdd(Thesis thesis, Enrolment enrolment) {
		super.beforeAdd(thesis, enrolment);

		if (thesis == null || enrolment == null) {
		    return;
		}

		List<Thesis> theses = enrolment.getTheses();

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
	public int compare(Thesis t1, Thesis t2) {
	    final int n = Student.NUMBER_COMPARATOR.compare(t1.getStudent(), t2.getStudent());
	    return n == 0 ? COMPARATOR_BY_ID.compare(t1, t2) : n;
	}
    };

    private final static double CREDITS = 0.5;

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
	    return new MultiLanguageString(dissertation.getLanguage(), result.toString());
	}
    }

    @Override
    @Checked("ThesisPredicates.student")
    public void setDiscussed(DateTime discussed) {
	if (discussed != null && getEnrolment().getCreationDateDateTime().isAfter(discussed)) {
	    throw new DomainException("thesis.invalid.discussed.date.time");
	}
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

	for (; !getParticipations().isEmpty(); getParticipations().iterator().next().delete())
	    ;

	// Unnecessary, the student could not submit files while in draft
	// removeDissertation();
	// removeExtendedAbstract();

	removeDegree();
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

    @Override
    public void removeEnrolment() {
	super.setEnrolment(null);
    }

    public String getDepartmentName() {
	final CompetenceCourse competenceCourse = getEnrolment().getCurricularCourse().getCompetenceCourse();
	if (getEnrolment().isBolonhaDegree())
	    return competenceCourse.getDepartmentUnit().getDepartment().getRealName();
	if (competenceCourse.hasAnyDepartments())
	    return competenceCourse.getDepartments().get(0).getRealName();
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
    @Checked("ThesisPredicates.isScientificCouncil")
    public void rejectProposal(String rejectionComment) {
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
	final Person person = AccessControl.getPerson();
	final String emailAddr = person == null ? "no-reply@ist.utl.pt" : person.getEmail();

	final Collection<String> tos = new HashSet<String>();
	final ExecutionYear executionYear = getEnrolment().getExecutionYear();
	for (ScientificCommission member : getDegree().getScientificCommissionMembers(executionYear)) {
	    if (member.isContact()) {
		final Person memberPerson = member.getPerson();
		final String toAddr = memberPerson.getEmail();
		if (toAddr != null && toAddr.length() > 0) {
		    tos.add(toAddr);
		}
	    }
	}
	for (final ThesisEvaluationParticipant thesisEvaluationParticipant : getParticipationsSet()) {
	    final ThesisParticipationType thesisParticipationType = thesisEvaluationParticipant.getType();
	    if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
		    || thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
		final Person memberPerson = thesisEvaluationParticipant.getPerson();
		final String toAddr = memberPerson.getEmail();
		if (toAddr != null && toAddr.length() > 0) {
		    tos.add(toAddr);
		}
	    }
	}

	final String studentNumber = getStudent().getNumber().toString();
	final String title = getFinalFullTitle().getContent();
	final String subject = getMessage("message.thesis.reject.submission.email.subject", studentNumber);
	final String body = getMessage("message.thesis.reject.submission.email.body", studentNumber, title, rejectionComment);

	new Email(RoleType.SCIENTIFIC_COUNCIL.getDefaultLabel(), emailAddr, null, tos, null, null, subject, body);
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
	}
    }

    public ThesisLibraryState getLibraryState() {
	if (hasLastLibraryOperation())
	    return getLastLibraryOperation().getState();
	return ThesisLibraryState.NOT_DEALT;
    }

    /**
     * Do not use this. Create instances of {@link ThesisLibraryOperation}
     * instead.
     */
    public void setLibraryState(ThesisLibraryState state) {
	throw new UnsupportedOperationException();
    }

    public String getLibraryReference() {
	if (hasLastLibraryOperation())
	    return getLastLibraryOperation().getLibraryReference();
	return null;
    }

    /**
     * Do not use this. Create instances of {@link ThesisLibraryOperation}
     * instead.
     */
    public void setLibraryReference(String reference) {
	throw new UnsupportedOperationException();
    }

    public Person getLibraryOperationPerformer() {
	if (hasLastLibraryOperation())
	    return getLastLibraryOperation().getPerformer();
	return null;
    }

    /**
     * Do not use this. Create instances of {@link ThesisLibraryOperation}
     * instead.
     */
    public void setLibraryOperationPerformer(Person performer) {
	throw new UnsupportedOperationException();
    }

    public boolean hasFinalEnrolmentEvaluation() {
	Enrolment enrolment = getEnrolment();
	EnrolmentEvaluationState finalState = EnrolmentEvaluationState.FINAL_OBJ;

	return !enrolment.getEnrolmentEvaluationsByEnrolmentEvaluationState(finalState).isEmpty();
    }

    /**
     * Generates a new mark sheet in the administrative office or merges the
     * grade for this enrolment in an existing, unconfirmed, mark sheet for this
     * enrolment.
     * 
     * <p>
     * This is only done if there isn't already a MarkSheet with an evaluation
     * for the Enrolment related to this Thesis and if this Thesis has a
     * positive grade or is the second Thesis of the enrolment.
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

	Employee employee = responsible.getPerson().getEmployee();

	List<MarkSheetEnrolmentEvaluationBean> evaluations = getStudentEvalutionBean();

	return curricularCourse.createNormalMarkSheet(executionSemester, responsible, evaluationDate, type, true, evaluations,
		employee);
    }

    private List<MarkSheetEnrolmentEvaluationBean> getStudentEvalutionBean() {
	return Arrays.asList(new MarkSheetEnrolmentEvaluationBean(getEnrolment(), getDiscussed().toDate(), getEvaluationMark()));
    }

    private Teacher getExecutionCourseTeacher() {
	final List<Teacher> teachers = new ArrayList<Teacher>();

	final ExecutionCourse executionCourse = getExecutionCourse();
	if (executionCourse != null) {
	    for (Professorship professorship : executionCourse.getProfessorships()) {
		if (professorship.isResponsibleFor()) {
		    teachers.add(professorship.getTeacher());
		}
	    }
	}

	CurricularCourse curricularCourse = getEnrolment().getCurricularCourse();
	Degree degree = curricularCourse.getDegree();

	if (teachers.isEmpty()) {
	    for (Coordinator coordinator : degree.getCurrentResponsibleCoordinators()) {
		if (coordinator.isResponsible()) {
		    teachers.add(coordinator.getPerson().getTeacher());
		}
	    }
	}

	List<Department> departments = degree.getDepartments();

	for (Teacher teacher : teachers) {
	    if (departments.contains(teacher.getCurrentWorkingDepartment())) {
		return teacher;
	    }
	}

	return null;
    }

    private ExecutionCourse getExecutionCourse() {
	Enrolment enrolment = getEnrolment();
	CurricularCourse curricularCourse = enrolment.getCurricularCourse();

	List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(enrolment
		.getExecutionPeriod());
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

    public boolean isInRevision() {
	return getState() == ThesisState.REVISION;
    }

    public boolean isRejected() {
	return isDraft() && getSubmission() != null;
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

	return scale.isValid(mark.toString(), EvaluationType.EXAM_TYPE); //TODO:
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
	} else {
	    if (!orientator.hasExternalContract()) {
		hasInternal = true;
	    }

	    // check for duplicated persons
	    if (orientator == coorientator) {
		conditions.add(new ThesisCondition("thesis.condition.people.repeated.coordination"));
	    }
	}

	if (coorientator != null && !coorientator.hasExternalContract()) {
	    hasInternal = true;
	}

	if (!hasInternal && (orientator != null || coorientator != null)) {
	    conditions.add(new ThesisCondition("thesis.condition.orientation.notInternal"));
	}

	return conditions;
    }

    public List<ThesisCondition> getPresidentConditions() {
	List<ThesisCondition> conditions = new ArrayList<ThesisCondition>();

	Person president = getParticipationPerson(getPresident());

	if (president == null) {
	    conditions.add(new ThesisCondition("thesis.condition.president.required"));
	} else {
	    if (president.hasExternalContract()) {
		conditions.add(new ThesisCondition("thesis.condition.president.notInternal"));
	    } else {
		boolean isMember = false;

		for (ScientificCommission member : getDegree().getCurrentScientificCommissionMembers()) {
		    isMember = isMember || president == member.getPerson();

		    if (isMember) {
			break;
		    }
		}

		if (!isMember) {
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
	} else {
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
	MultiLanguageString keywords = getKeywords();
	Language realLanguage = Language.valueOf(language);

	if (keywords == null) {
	    setKeywords(new MultiLanguageString(realLanguage, text));
	} else {
	    keywords.setContent(realLanguage, text);
	    setKeywords(keywords);
	}
    }

    public void setOrientator(Person person) {
	setParticipation(person, ThesisParticipationType.ORIENTATOR);

	if (!isCreditsDistributionNeeded()) {
	    setCoorientatorCreditsDistribution(null);
	}
    }

    public void setCoorientator(Person person) {
	setParticipation(person, ThesisParticipationType.COORIENTATOR);

	if (!isCreditsDistributionNeeded()) {
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
	} else {
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

}
