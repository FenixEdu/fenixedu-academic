/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.thesis;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.FieldIsRequiredException;
import org.fenixedu.academic.domain.organizationalStructure.ScientificCouncilUnit;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.ThesisPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EvaluationType;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Message;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class Thesis extends Thesis_Base {

    private static final Logger logger = LoggerFactory.getLogger(Thesis.class);

    public static final String PROPOSAL_APPROVED_SIGNAL = "academic.Thesis.proposal.approved";

    static {
        getRelationThesisEnrolment().addListener(new RelationAdapter<Thesis, Enrolment>() {

            @Override
            public void beforeAdd(Thesis thesis, Enrolment enrolment) {
                super.beforeAdd(thesis, enrolment);

                if (thesis == null || enrolment == null) {
                    return;
                }

                Collection<Thesis> theses = enrolment.getThesesSet();

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

    public static final Comparator<Thesis> COMPARATOR_BY_STUDENT = (t1, t2) -> {
        final int n = Student.NUMBER_COMPARATOR.compare(t1.getStudent(), t2.getStudent());
        return n == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(t1, t2) : n;
    };

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

    public Thesis(Degree degree, Enrolment enrolment, LocalizedString title) {
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
    public void setTitle(LocalizedString title) {
        if (title == null || title.isEmpty()) {
            throw new FieldIsRequiredException("title", "thesis.title.required");
        }

        super.setTitle(title);
    }

    public LocalizedString getFinalTitle() {
        ThesisFile dissertation = getDissertation();

        if (dissertation == null) {
            return getTitle();
        } else {
            final Locale dlanguage = dissertation.getLanguage();
            final Locale language = dlanguage == null ? Locale.getDefault() : dlanguage;
            return new LocalizedString(language, dissertation.getTitle());
        }
    }

    public LocalizedString getFinalSubtitle() {
        ThesisFile dissertation = getDissertation();

        if (dissertation == null) {
            return null;
        } else {

            String subTitle = dissertation.getSubTitle();

            if (subTitle == null) {
                return null;
            }

            return new LocalizedString(dissertation.getLanguage(), subTitle);
        }
    }

    public void setFinalTitle(final LocalizedString finalTitle) {
        setTitle(finalTitle);
        final ThesisFile dissertation = getDissertation();
        if (dissertation != null) {
            final Locale language = dissertation.getLanguage();
            if (language == null) {
                final Locale l = LocaleUtils.getContentLocale(finalTitle);
                dissertation.setLanguage(l);
                dissertation.setTitle(finalTitle.getContent(l));
            } else {
                final String content = finalTitle.getContent(language);
                if (content == null) {
                    final Locale l = LocaleUtils.getContentLocale(finalTitle);
                    dissertation.setLanguage(l);
                    dissertation.setTitle(finalTitle.getContent(l));
                } else {
                    dissertation.setTitle(content);
                }
            }
        }
    }

    public void setFinalSubtitle(final LocalizedString finalSubtitle) {
        final ThesisFile dissertation = getDissertation();
        if (dissertation != null) {
            final Locale language = dissertation.getLanguage();
            if (language == null) {
                final Locale l = LocaleUtils.getContentLocale(finalSubtitle);
                dissertation.setLanguage(l);
                dissertation.setSubTitle(finalSubtitle.getContent(l));
            } else {
                final String content = finalSubtitle.getContent(language);
                if (content == null) {
                    final Locale l = LocaleUtils.getContentLocale(finalSubtitle);
                    dissertation.setLanguage(l);
                    dissertation.setSubTitle(finalSubtitle.getContent(l));
                } else {
                    dissertation.setSubTitle(content);
                }
            }
        }
    }

    public Locale getLanguage() {
        ThesisFile dissertation = getDissertation();
        return dissertation == null ? null : dissertation.getLanguage();
    }

    final public LocalizedString getFinalFullTitle() {
        final ThesisFile dissertation = getDissertation();

        if (dissertation == null) {
            return getTitle();
        } else {
            final StringBuilder result = new StringBuilder();
            result.append(dissertation.getTitle());
            result.append(StringUtils.isEmpty(dissertation.getSubTitle()) ? "" : ": " + dissertation.getSubTitle());
            final Locale language = dissertation.getLanguage();
            return language == null ? new LocalizedString(I18N.getLocale(), result.toString()) : new LocalizedString(language,
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

    public void setDiscussedWithoutRules(DateTime discussed) {
        logger.info("Thesis {} of {} was changed from {} to {} by user {}", getExternalId(), getStudent().getNumber(),
                getDiscussed() == null ? "null" : getDiscussed().toString(), discussed == null ? "null" : discussed.toString(),
                Authenticate.getUser() == null ? "-" : Authenticate.getUser().getUsername());
        super.setDiscussed(discussed);
    }

    public boolean hasInconsistentDates() {
        if (getDiscussed() == null) {
            return false;
        }
        return (getCreation() != null && getDiscussed().isBefore(getCreation())) || (getEnrolment() != null && getDiscussed()
                .isBefore(getEnrolment().getCreationDateDateTime()));
    }

    /**
     * This only exists since it is being used with fr:edit
     */
    public DateTime getDiscussedWithoutRules() {
        return super.getDiscussed();
    }

    @Override
    public void setThesisAbstract(LocalizedString thesisAbstract) {
        check(this, ThesisPredicates.waitingConfirmation);
        super.setThesisAbstract(thesisAbstract);
    }

    @Override
    public void setKeywords(LocalizedString keywords) {
        check(this, ThesisPredicates.waitingConfirmation);
        super.setKeywords(keywords);
    }

    public List<ThesisEvaluationParticipant> getOrientation() {

        return getParticipationsSet()
                .stream()
                .filter(p -> p.getType() == ThesisParticipationType.ORIENTATOR
                        || p.getType() == ThesisParticipationType.COORIENTATOR).collect(Collectors.toList());
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
        for (ThesisEvaluationParticipant participant : getParticipationsSet()) {
            if (participant.getType() == type) {
                return participant;
            }
        }

        return null;
    }

    public List<ThesisEvaluationParticipant> getAllParticipants(ThesisParticipationType type) {
        return getAllParticipants(type, new ThesisParticipationType[0]);
    }

    public List<ThesisEvaluationParticipant> getAllParticipants(ThesisParticipationType type, ThesisParticipationType... types) {
        List<ThesisParticipationType> values = Lists.asList(type, types);
        return getParticipationsSet().stream().filter(p -> values.contains(p.getType())).collect(Collectors.toList());
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        setRootDomainObject(null);

        for (; !getParticipationsSet().isEmpty(); getParticipationsSet().iterator().next().delete()) {
            ;
        }

        // Unnecessary, the student could not submit files while in draft
        // setDissertation(null);
        // setExtendedAbstract(null);

        setDegree(null);
        removeEnrolment();

        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (getState() != ThesisState.DRAFT) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "thesis.delete.notDraft"));
        }
        if (getReaders() != null) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "thesis.delete.notDraft"));
        }
    }

    public boolean isDeletable() {
        return getDeletionBlockers().isEmpty();
    }

    protected static Collection<Thesis> getThesisInState(Degree degree, ExecutionYear year, ThesisState state) {
        List<Thesis> theses = new ArrayList<>();

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
        return new ArrayList<>(getThesisInState(degree, executionYear, ThesisState.APPROVED));
    }

    public static Collection<Thesis> getRevisionThesis() {
        return getRevisionThesis(null);
    }

    public static Collection<Thesis> getRevisionThesis(Degree degree) {
        return getRevisionThesis(degree, null);
    }

    public static Collection<Thesis> getRevisionThesis(Degree degree, ExecutionYear executionYear) {
        return new ArrayList<>(getThesisInState(degree, executionYear, ThesisState.REVISION));
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

    public boolean hasCredits() {
        return isEvaluated() && hasFinalEnrolmentEvaluation();
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

        CurricularCourse curricularCourse = enrolment.getCurricularCourse();
        if (!curricularCourse.isDissertation()) {
            throw new DomainException("thesis.enrolment.notDissertationEnrolment");
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
        if (!competenceCourse.getDepartmentsSet().isEmpty()) {
            return competenceCourse.getDepartmentsSet().iterator().next().getRealName();
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
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.submit.notDraft");
        }

        if (!isValid()) {
            throw new DomainException("thesis.submit.hasConditions");
        }

        Person person = AccessControl.getPerson();

        if (person.getTeacher() == null) {
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
    @Atomic
    public void approveProposal() {
        check(this, ThesisPredicates.isScientificCouncilOrCoordinatorAndNotOrientatorOrCoorientator);
        if (getState() != ThesisState.APPROVED) {
            if (getState() != ThesisState.SUBMITTED) {
                throw new DomainException("thesis.approve.notSubmitted");
            }

            setApproval(new DateTime());
            setProposalApprover(AccessControl.getPerson());

            setState(ThesisState.APPROVED);

            Signal.emit(PROPOSAL_APPROVED_SIGNAL, new DomainObjectEvent<>(this));
        }
    }

    // (SUBMITTED | APPROVED) -> DRAFT
    @Atomic
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

        final Collection<Person> persons = new HashSet<>();
        final ExecutionYear executionYear = getEnrolment().getExecutionYear();
        for (ScientificCommission member : getDegree().getScientificCommissionMembers(executionYear)) {
            if (member.isContact()) {
                persons.add(member.getPerson());
            }
        }

        Set<Person> orientationPersons = getOrientationPersons();
        persons.addAll(orientationPersons);

        final String studentNumber = getStudent().getNumber().toString();
        final String title = getFinalFullTitle().getContent();
        final String subject = getMessage("message.thesis.reject.submission.email.subject", studentNumber);
        final String body = getMessage("message.thesis.reject.submission.email.body", studentNumber, title, rejectionComment);

        //

        Message.from(ScientificCouncilUnit.getScientificCouncilUnit().getSender())
                .replyToSender()
                .to(Person.convertToUserGroup(persons))
                .subject(subject)
                .textBody(body)
                .send();
    }

    protected String getMessage(final String key, final Object... args) {
        return MessageFormat.format(BundleUtil.getString(Bundle.SCIENTIFIC, key), args);
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

        if (getExtendedAbstract() == null) {
            throw new DomainException("thesis.confirm.noExtendedAbstract");
        }

        if (getDissertation() == null) {
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

        if (getExtendedAbstract() == null) {
            throw new DomainException("thesis.confirm.noExtendedAbstract");
        }

        if (getDissertation() == null) {
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

            setConfirmation(null);
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

            setDocumentsAvailableAfter(new DateTime().plusMonths(9));
        } else {
            throw new DomainException("thesis.already.evaluated");
        }
    }

    public ThesisLibraryState getLibraryState() {
        if (getLastLibraryOperation() != null) {
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
        if (getLastLibraryOperation() != null) {
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
        if (getLastLibraryOperation() != null) {
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
        return enrolment.getThesesSet().size() != 1 || !getEvaluationMark().getValue().equals(GradeScale.RE);
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
        for (EnrolmentEvaluation evaluation : getEnrolment().getEvaluationsSet()) {
            if (!evaluation.getEvaluationSeason().isNormal()) {
                continue;
            }

            if (evaluation.getMarkSheet() == null) {
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

        for (MarkSheet markSheet : curricularCourse.getMarkSheetsSet()) {
            if (getEnrolment().getExecutionPeriod() != markSheet.getExecutionPeriod()) {
                continue;
            }

            if (markSheet.isConfirmed()) {
                continue;
            }

            if (!markSheet.getEvaluationSeason().isSpecialAuthorization()) {
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
        if (responsible == null) {
            responsible = AccessControl.getPerson().getTeacher();
        }

        List<MarkSheetEnrolmentEvaluationBean> evaluations = getStudentEvalutionBean();

        return curricularCourse.createNormalMarkSheet(executionSemester, responsible, evaluationDate,
                EvaluationSeason.readSpecialAuthorization(), true, evaluations, responsible.getPerson());
    }

    private List<MarkSheetEnrolmentEvaluationBean> getStudentEvalutionBean() {
        return Arrays.asList(new MarkSheetEnrolmentEvaluationBean(getEnrolment(), getDiscussed().toDate(), getEvaluationMark()));
    }

    private Teacher getExecutionCourseTeacher() {
        final List<Teacher> teachers = new ArrayList<>();

        final ExecutionCourse executionCourse = getExecutionCourse();
        if (executionCourse != null) {
            for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
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

        if (getDissertation() != null) {
            getDissertation().delete();
        }

        if (getExtendedAbstract() != null) {
            getExtendedAbstract().delete();
        }
    }

    @Atomic
    public void swapFilesVisibility() {
        ThesisVisibilityType visibility = getVisibility();

        if (visibility == null) {
            throw new DomainException("thesis.acceptDeclaration.visibility.required");
        }

        if (visibility.equals(ThesisVisibilityType.INTRANET)) {
            setVisibility(ThesisVisibilityType.PUBLIC);
        } else {
            setVisibility(ThesisVisibilityType.INTRANET);
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
                    && person == thesisEvaluationParticipant.getPerson()) {
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
        List<ThesisCondition> conditions = new ArrayList<>();

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
        List<ThesisCondition> conditions = new ArrayList<>();

        conditions.addAll(getGeneralConditions());
        conditions.addAll(getOrientationConditions());
        conditions.addAll(getJuryConditions());

        return conditions;
    }

    public Collection<ThesisCondition> getGeneralConditions() {
        List<ThesisCondition> conditions = new ArrayList<>();

        // check too few persons
        int count = getJuryParticipantCount();
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
        List<ThesisCondition> conditions = new ArrayList<>();

        List<ThesisEvaluationParticipant> orientation = getOrientation();
        if (orientation.isEmpty()) {
            conditions.add(new ThesisCondition("thesis.condition.orientator.required"));
        } else {
            if (orientation.stream().anyMatch(o -> o.getPercentageDistribution() < 20)) {
                conditions.add(new ThesisCondition("thesis.condition.orientation.credits.low"));
            }

            if (orientation.stream().mapToInt(o -> o.getPercentageDistribution()).sum() > 100) {
                conditions.add(new ThesisCondition("thesis.condition.orientation.credits.overflow"));
            }
            
            if (orientation.size()>2) {
                conditions.add(new ThesisCondition("thesis.condition.orientation.limited.to.two"));
            }
            // check for duplicated persons
            if (orientation.stream().map(ThesisEvaluationParticipant::getPerson).filter(Objects::nonNull).count() != orientation
                    .stream().map(ThesisEvaluationParticipant::getPerson).filter(Objects::nonNull).distinct().count()) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.orientation"));
            }
        }

        return conditions;
    }

    public List<ThesisCondition> getJuryConditions() {
        List<ThesisCondition> conditions = new ArrayList<>();

        Person president = getParticipationPerson(getPresident());

        if (president == null) {
            conditions.add(new ThesisCondition("thesis.condition.president.required"));
        } else {
            if (president.getTeacher() == null || !president.getTeacher().isActiveContractedTeacher()) {
                conditions.add(new ThesisCondition("thesis.condition.president.notInternal"));
            } else {
                boolean isMember = false;

                final Enrolment enrolment = getEnrolment();
                final DegreeCurricularPlan degreeCurricularPlan = enrolment.getDegreeCurricularPlanOfDegreeModule();
                final ExecutionYear executionYear = enrolment.getExecutionYear();
                final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
                if (executionDegree != null) {
                    for (ScientificCommission member : executionDegree.getScientificCommissionMembersSet()) {
                        isMember = president == member.getPerson();

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
            // check duplicated person within jury
            Set<Person> juryPersons = new HashSet<>();
            //only vowels to separate the president's case
            for (ThesisEvaluationParticipant vowel : getVowels()) {
                if (vowel.getPerson() != null) {
                    juryPersons.add(vowel.getPerson());
                }
            }

            if (getVowels().stream().filter(v -> !v.isExternal()).count() != juryPersons.size()) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.vowels"));
            }

            if (president != null && juryPersons.contains(president)) {
                conditions.add(new ThesisCondition("thesis.condition.people.repeated.vowels.president"));
            }

            juryPersons.add(president);  // necessary since there is no express prohibition to the president being an orientation member
            // check that one and only one member of the orientation is in the jury

            //FIXME: Removed while it's not possible to check external advisors
//            if (getOrientation().stream().filter(p -> juryPersons.contains(p.getPerson())).count() != 1) {
//                conditions.add(new ThesisCondition("thesis.condition.people.jury.orientation.members"));
//            }

        }

        return conditions;
    }

    private int getJuryParticipantCount() {
        return getVowels().size() + (getPresident() != null ? 1 : 0);
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
        LocalizedString thesisAbstract = getThesisAbstract();

        if (thesisAbstract == null) {
            return null;
        } else {
            Locale realLanguage = new Locale.Builder().setLanguageTag(language).build();
            String value = thesisAbstract.getContent(realLanguage);

            if (value == null || value.length() == 0) {
                return null;
            } else {
                return value;
            }
        }
    }

    public void setThesisAbstractLanguage(String language, String text) {
        LocalizedString thesisAbstract = getThesisAbstract();
        Locale realLanguage = new Locale.Builder().setLanguageTag(language).build();

        if (thesisAbstract == null) {
            setThesisAbstract(new LocalizedString(realLanguage, text));
        } else {
            thesisAbstract = thesisAbstract.with(realLanguage, text);
            setThesisAbstract(thesisAbstract);
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
        LocalizedString thesisAbstract = getKeywords();

        if (thesisAbstract == null) {
            return null;
        } else {
            Locale realLanguage = new Locale.Builder().setLanguageTag(language).build();
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

        LocalizedString keywords = getKeywords();
        Locale realLanguage = new Locale.Builder().setLanguageTag(language).build();

        if (keywords == null) {
            setKeywords(new LocalizedString(realLanguage, text));
        } else {
            keywords = keywords.with(realLanguage, text);
            setKeywords(keywords);
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

    public void setParticipation(Person person, ThesisParticipationType type) {
        if (person == null) {
            removeParticipation(getParticipant(type));
        } else {
            new ThesisEvaluationParticipant(this, person, type);
        }
    }

    public void addParticipant(Person person, ThesisParticipationType type) {
        new ThesisEvaluationParticipant(this, person, type);
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
        final Set<Thesis> theses = new HashSet<>();
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

    public List<Locale> getLanguages() {
        final List<Locale> result = new ArrayList<>();

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

    private void add(final List<Locale> result, final LocalizedString mls) {
        if (mls != null) {
            for (final Locale language : mls.getLocales()) {
                add(result, language);
            }
        }
    }

    private void add(final List<Locale> result, final Locale language) {
        if (language != null && !result.contains(language)) {
            if (language == org.fenixedu.academic.util.LocaleUtils.PT) {
                result.add(0, language);
            } else if (language == org.fenixedu.academic.util.LocaleUtils.EN) {
                if (result.size() > 0 && result.iterator().next() == org.fenixedu.academic.util.LocaleUtils.PT) {
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
        return getDissertation() != null && getVisibility() != null;
    }

    public Set<Person> getOrientationPersons() {
        return getOrientation().stream().filter(p -> p.getPerson() != null).map(p -> p.getPerson()).collect(Collectors.toSet());
    }

    public void addExternal(ThesisParticipationType type, String name, String email) {
        new ThesisEvaluationParticipant(this, name, email, type);
    }

}
