/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.UnableToPrintServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentNotPayedException;
import net.sourceforge.fenixedu.domain.exceptions.InDebtEnrolmentsException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.predicates.MarkSheetPredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class MarkSheet extends MarkSheet_Base {

    static final private Comparator<MarkSheet> COMPARATOR_BY_EVALUATION_DATE = new Comparator<MarkSheet>() {
        @Override
        public int compare(MarkSheet o1, MarkSheet o2) {
            if (o1.getEvaluationDateDateTime() == null && o2.getEvaluationDateDateTime() == null) {
                return 0;
            }
            if (o1.getEvaluationDateDateTime() == null) {
                return -1;
            }
            if (o2.getEvaluationDateDateTime() == null) {
                return 1;
            }

            return o1.getEvaluationDateDateTime().compareTo(o2.getEvaluationDateDateTime());
        }
    };

    static final private Comparator<MarkSheet> COMPARATOR_BY_CREATION_DATE = new Comparator<MarkSheet>() {
        @Override
        public int compare(MarkSheet o1, MarkSheet o2) {
            if (o1.getCreationDateDateTime() == null && o2.getCreationDateDateTime() == null) {
                return 0;
            }
            if (o1.getCreationDateDateTime() == null) {
                return -1;
            }
            if (o2.getCreationDateDateTime() == null) {
                return 1;
            }

            return o1.getCreationDateDateTime().compareTo(o2.getCreationDateDateTime());
        }
    };

    static final public Comparator<MarkSheet> COMPARATOR_BY_EVALUATION_DATE_AND_ID = new Comparator<MarkSheet>() {
        @Override
        final public int compare(MarkSheet o1, MarkSheet o2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_EVALUATION_DATE);
            comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

            return comparatorChain.compare(o1, o2);
        }
    };

    static final public Comparator<MarkSheet> COMPARATOR_BY_EVALUATION_DATE_AND_CREATION_DATE_AND_ID =
            new Comparator<MarkSheet>() {
                @Override
                final public int compare(MarkSheet o1, MarkSheet o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_EVALUATION_DATE);
                    comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_CREATION_DATE);
                    comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

                    return comparatorChain.compare(o1, o2);
                }
            };

    protected MarkSheet() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCreationDateDateTime(new DateTime());
        setPrinted(Boolean.FALSE);
    }

    private MarkSheet(CurricularCourse curricularCourse, ExecutionSemester executionSemester, Teacher responsibleTeacher,
            Date evaluationDate, MarkSheetType markSheetType, MarkSheetState markSheetState, Boolean submittedByTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Person creator) {

        this();
        checkParameters(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType, markSheetState,
                evaluationBeans, creator);
        init(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType, markSheetState,
                submittedByTeacher, creator);

        if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            addEnrolmentEvaluationsWithoutResctrictions(responsibleTeacher, evaluationBeans,
                    EnrolmentEvaluationState.TEMPORARY_OBJ);
        } else {
            addEnrolmentEvaluationsWithResctrictions(responsibleTeacher, evaluationBeans, EnrolmentEvaluationState.TEMPORARY_OBJ);
        }
        generateCheckSum();
    }

    public static MarkSheet createNormal(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, Boolean submittedByTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Person creator) {

        return new MarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
                MarkSheetState.NOT_CONFIRMED, submittedByTeacher, evaluationBeans, creator);
    }

    public static MarkSheet createOldNormal(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Person creator) {

        return new OldMarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
                MarkSheetState.NOT_CONFIRMED, evaluationBeans, creator);
    }

    public static MarkSheet createRectification(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, String reason,
            MarkSheetEnrolmentEvaluationBean evaluationBean, Person creator) {

        MarkSheet markSheet =
                new MarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
                        MarkSheetState.RECTIFICATION_NOT_CONFIRMED, Boolean.FALSE,
                        (evaluationBean != null) ? Collections.singletonList(evaluationBean) : null, creator);
        markSheet.setReason(reason);
        return markSheet;
    }

    public static MarkSheet createOldRectification(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, String reason,
            MarkSheetEnrolmentEvaluationBean evaluationBean, Person creator) {

        Collection<MarkSheetEnrolmentEvaluationBean> beans =
                (evaluationBean != null) ? Collections.singletonList(evaluationBean) : null;

        MarkSheet markSheet =
                new OldMarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
                        MarkSheetState.RECTIFICATION_NOT_CONFIRMED, beans, creator);
        markSheet.setReason(reason);
        return markSheet;
    }

    private void checkParameters(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, MarkSheetState markSheetState,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Person creator) {

        if (curricularCourse == null || executionSemester == null || responsibleTeacher == null || evaluationDate == null
                || markSheetType == null || markSheetState == null || creator == null) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }
        if (evaluationBeans == null || evaluationBeans.size() == 0) {
            throw new DomainException("error.markSheet.create.with.invalid.enrolmentEvaluations.number");
        }
        checkIfTeacherIsResponsibleOrCoordinator(curricularCourse, executionSemester, responsibleTeacher, markSheetType);
        checkIfEvaluationDateIsInExamsPeriod(curricularCourse, getExecutionDegree(curricularCourse, executionSemester),
                executionSemester, evaluationDate, markSheetType);
    }

    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, Teacher responsibleTeacher, MarkSheetType markSheetType) throws DomainException {

        if (curricularCourse.isDissertation()) {
            if (responsibleTeacher.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                return;
            }
            for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                if (executionCourse.getExecutionPeriod().getExecutionYear() == executionSemester.getExecutionYear()) {
                    for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                        if (professorship.isResponsibleFor() && professorship.getTeacher() == responsibleTeacher) {
                            return;
                        }
                    }
                }
            }
        }

        if (markSheetType == MarkSheetType.IMPROVEMENT
                && curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester).isEmpty()) {

            if (!responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
                    executionSemester.getPreviousExecutionPeriod())
                    && !responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
                            executionSemester.getPreviousExecutionPeriod().getPreviousExecutionPeriod())
                    && !responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(
                            curricularCourse,
                            executionSemester.getPreviousExecutionPeriod().getPreviousExecutionPeriod()
                                    .getPreviousExecutionPeriod())) {
                throw new DomainException("error.teacherNotResponsibleOrNotCoordinator");
            }

        } else if (!responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse, executionSemester)) {
            throw new DomainException("error.teacherNotResponsibleOrNotCoordinator");
        }

    }

    protected void checkIfEvaluationDateIsInExamsPeriod(CurricularCourse curricularCourse, ExecutionDegree executionDegree,
            ExecutionSemester executionSemester, Date evaluationDate, MarkSheetType markSheetType) throws DomainException {

        if (executionDegree == null) {
            if (!markSheetType.equals(MarkSheetType.IMPROVEMENT)
                    || !curricularCourse.getDegreeCurricularPlan().canSubmitImprovementMarkSheets(
                            executionSemester.getExecutionYear())) {
                throw new DomainException("error.evaluationDateNotInExamsPeriod");
            }

        } else if (!executionDegree.isEvaluationDateInExamPeriod(evaluationDate, executionSemester, markSheetType)) {

            OccupationPeriod occupationPeriod = executionDegree.getOccupationPeriodFor(executionSemester, markSheetType);
            if (occupationPeriod == null) {
                throw new DomainException("error.evaluationDateNotInExamsPeriod");
            } else {
                throw new DomainException("error.evaluationDateNotInExamsPeriod.withEvaluationDateAndPeriodDates",
                        DateFormatUtil.format("dd/MM/yyyy", evaluationDate), occupationPeriod.getStartYearMonthDay().toString(
                                "dd/MM/yyyy"), occupationPeriod.getEndYearMonthDay().toString("dd/MM/yyyy"));
            }
        }
    }

    private ExecutionDegree getExecutionDegree(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
        return curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionSemester.getExecutionYear());
    }

    protected void init(CurricularCourse curricularCourse, ExecutionSemester executionSemester, Teacher responsibleTeacher,
            Date evaluationDate, MarkSheetType markSheetType, MarkSheetState markSheetState, Boolean submittedByTeacher,
            Person creator) {

        setMarkSheetState(markSheetState);
        setCurricularCourse(curricularCourse);
        setExecutionPeriod(executionSemester);
        setResponsibleTeacher(responsibleTeacher);
        setEvaluationDate(evaluationDate);
        setMarkSheetType(markSheetType);
        setSubmittedByTeacher(submittedByTeacher);
        setCreator(creator);
    }

    private void addEnrolmentEvaluationsWithoutResctrictions(Teacher responsibleTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, EnrolmentEvaluationState enrolmentEvaluationState) {
        check(this, MarkSheetPredicates.editPredicate);

        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());

        for (final MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {

            checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
                    evaluationBean.getEvaluationDate(), getMarkSheetType());

            final EnrolmentEvaluation enrolmentEvaluation =
                    evaluationBean.getEnrolment().addNewEnrolmentEvaluation(enrolmentEvaluationState,
                            getMarkSheetType().getEnrolmentEvaluationType(), responsibleTeacher.getPerson(),
                            evaluationBean.getGradeValue(), getCreationDate(), evaluationBean.getEvaluationDate(),
                            getExecutionPeriod(), null);

            addEnrolmentEvaluations(enrolmentEvaluation);
        }
    }

    private void addEnrolmentEvaluationsWithResctrictions(Teacher responsibleTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, EnrolmentEvaluationState enrolmentEvaluationState) {
        check(this, MarkSheetPredicates.editPredicate);

        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());
        final Set<Enrolment> enrolmentsNotInAnyMarkSheet =
                getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(getMarkSheetType(), getExecutionPeriod());

        Set<Enrolment> notPayedEnrolments = new HashSet<Enrolment>();
        for (final MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {

            if (enrolmentsNotInAnyMarkSheet.contains(evaluationBean.getEnrolment())) {
                try {
                    addEnrolmentEvaluationToMarkSheet(responsibleTeacher, enrolmentEvaluationState, evaluationBean,
                            executionDegree);
                } catch (EnrolmentNotPayedException e) {
                    notPayedEnrolments.add(e.getEnrolment());
                }
            } else {
                // TODO:
                throw new DomainException("error.markSheet");
            }
        }
    }

    private void addEnrolmentEvaluationToMarkSheet(Teacher responsibleTeacher, EnrolmentEvaluationState enrolmentEvaluationState,
            final MarkSheetEnrolmentEvaluationBean evaluationBean, ExecutionDegree executionDegree) {
        check(this, MarkSheetPredicates.editPredicate);

        checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
                evaluationBean.getEvaluationDate(), getMarkSheetType());

        EnrolmentEvaluation enrolmentEvaluation =
                evaluationBean.getEnrolment().getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(enrolmentEvaluationState,
                        getMarkSheetType().getEnrolmentEvaluationType());

        if (enrolmentEvaluation == null) {
            enrolmentEvaluation =
                    evaluationBean.getEnrolment().addNewEnrolmentEvaluation(enrolmentEvaluationState,
                            getMarkSheetType().getEnrolmentEvaluationType(), responsibleTeacher.getPerson(),
                            evaluationBean.getGradeValue(), getCreationDate(), evaluationBean.getEvaluationDate(),
                            getExecutionPeriod(), null);
        } else {
            enrolmentEvaluation.edit(responsibleTeacher.getPerson(), evaluationBean.getGradeValue(), getCreationDate(),
                    evaluationBean.getEvaluationDate());
        }
        addEnrolmentEvaluations(enrolmentEvaluation);
    }

    protected boolean hasMarkSheetState(MarkSheetState markSheetState) {
        return (getMarkSheetState() == markSheetState);
    }

    public boolean isNotConfirmed() {
        return hasMarkSheetState(MarkSheetState.NOT_CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    public boolean isConfirmed() {
        return hasMarkSheetState(MarkSheetState.CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION);
    }

    public boolean isRectification() {
        return hasMarkSheetState(MarkSheetState.RECTIFICATION) || hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    public void editNormal(Teacher responsibleTeacher, Date newEvaluationDate) {
        check(this, MarkSheetPredicates.editPredicate);

        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");

        } else if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            throw new DomainException("error.markSheet.wrong.edit.method");

        } else {
            checkIfTeacherIsResponsibleOrCoordinator(getCurricularCourse(), getExecutionPeriod(), responsibleTeacher,
                    getMarkSheetType());
            checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(),
                    getExecutionDegree(getCurricularCourse(), getExecutionPeriod()), getExecutionPeriod(), newEvaluationDate,
                    getMarkSheetType());

            Date oldEvaluationDate = getEvaluationDateDateTime().toDate();
            setResponsibleTeacher(responsibleTeacher);
            setEvaluationDate(newEvaluationDate);

            editMarkSheetEnrolmentEvaluationsWithSameEvaluationDate(responsibleTeacher, oldEvaluationDate, newEvaluationDate,
                    getEnrolmentEvaluationsSet());

            generateCheckSum();
        }
    }

    public void editRectification(MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean) {
        check(this, MarkSheetPredicates.editPredicate);

        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");

        } else if (hasMarkSheetState(MarkSheetState.NOT_CONFIRMED)) {
            throw new DomainException("error.markSheet.wrong.edit.method");

        } else if (enrolmentEvaluationBean == null) {
            throw new DomainException("error.markSheet.edit.with.invalid.enrolmentEvaluations.number");

        } else {
            editEnrolmentEvaluations(Collections.singletonList(enrolmentEvaluationBean));
            generateCheckSum();
        }
    }

    private void editMarkSheetEnrolmentEvaluationsWithSameEvaluationDate(Teacher responsibleTeacher, Date oldEvaluationDate,
            Date newEvaluationDate, Set<EnrolmentEvaluation> enrolmentEvaluationsToEdit) {
        check(this, MarkSheetPredicates.editPredicate);

        String dateFormat = "dd/MM/yyyy";
        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());
        for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluationsToEdit) {
            if (DateFormatUtil.compareDates(dateFormat, enrolmentEvaluation.getExamDate(), oldEvaluationDate) == 0) {
                checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
                        newEvaluationDate, getMarkSheetType());
                enrolmentEvaluation.edit(responsibleTeacher.getPerson(), newEvaluationDate);
            }
        }
    }

    public void editNormal(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToRemove) {
        check(this, MarkSheetPredicates.editPredicate);

        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");

        } else if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            throw new DomainException("error.markSheet.wrong.edit.method");

        } else {
            checkIfEnrolmentEvaluationsNumberIsValid(evaluationBeansToAppend, evaluationBeansToRemove);

            editEnrolmentEvaluations(evaluationBeansToEdit);
            removeEnrolmentEvaluations(evaluationBeansToRemove);
            appendEnrolmentEvaluations(evaluationBeansToAppend);

            generateCheckSum();
        }
    }

    private void checkIfEnrolmentEvaluationsNumberIsValid(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend,
            Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToRemove) {

        if (evaluationBeansToAppend.size() == 0 && evaluationBeansToRemove.size() == getEnrolmentEvaluationsSet().size()) {
            throw new DomainException("error.markSheet.edit.with.invalid.enrolmentEvaluations.number");
        }
    }

    protected void editEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit) {
        check(this, MarkSheetPredicates.editPredicate);

        final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());

        for (final MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : evaluationBeansToEdit) {

            if (this.getEnrolmentEvaluationsSet().contains(enrolmentEvaluationBean.getEnrolmentEvaluation())) {

                checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
                        enrolmentEvaluationBean.getEvaluationDate(), getMarkSheetType());

                final EnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationBean.getEnrolmentEvaluation();
                enrolmentEvaluation.edit(getResponsibleTeacher().getPerson(), enrolmentEvaluationBean.getGradeValue(),
                        new Date(), enrolmentEvaluationBean.getEvaluationDate());
            } else {
                // TODO:
                throw new DomainException("error.markSheet");
            }
        }
    }

    private void removeEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemove) {
        check(this, MarkSheetPredicates.editPredicate);
        for (MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : enrolmentEvaluationBeansToRemove) {
            enrolmentEvaluationBean.getEnrolmentEvaluation().removeFromMarkSheet();
            enrolmentEvaluationBean.getEnrolmentEvaluation().setGrade(Grade.createEmptyGrade());
        }
    }

    protected void appendEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend) {
        addEnrolmentEvaluationsWithResctrictions(getResponsibleTeacher(), evaluationBeansToAppend,
                EnrolmentEvaluationState.TEMPORARY_OBJ);
    }

    public void confirm(Person validator) {
        check(this, MarkSheetPredicates.confirmPredicate);
        if (validator == null) {
            throw new DomainException("error.markSheet.invalid.arguments");
        }
        if (isNotConfirmed()) {
            setValidator(validator);

            Set<Enrolment> inDebtEnrolments = new HashSet<Enrolment>();
            for (final EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
                try {
                    enrolmentEvaluation.confirmSubmission(getEnrolmentEvaluationStateToConfirm(), validator, "");
                } catch (EnrolmentNotPayedException e) {
                    inDebtEnrolments.add(e.getEnrolment());
                }
            }

            if (!inDebtEnrolments.isEmpty()) {
                throw new InDebtEnrolmentsException("EnrolmentEvaluation.cannot.set.grade.on.not.payed.enrolment.evaluation",
                        inDebtEnrolments);
            }

            setConfirmationDateDateTime(new DateTime());
            setMarkSheetState(getMarkSheetStateToConfirm());

        } else {
            throw new DomainException("error.markSheet.already.confirmed");
        }
    }

    protected MarkSheetState getMarkSheetStateToConfirm() {
        if (this.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
            return MarkSheetState.CONFIRMED;
        } else {
            return MarkSheetState.RECTIFICATION;
        }
    }

    protected EnrolmentEvaluationState getEnrolmentEvaluationStateToConfirm() {
        if (this.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
            return EnrolmentEvaluationState.FINAL_OBJ;
        } else {
            return EnrolmentEvaluationState.RECTIFICATION_OBJ;
        }
    }

    public boolean getCanBeDeleted() {
        return isNotConfirmed();
    }

    public void delete() {
        if (!getCanBeDeleted()) {
            throw new DomainException("error.markSheet.cannot.be.deleted");
        }

        setExecutionPeriod(null);
        setCurricularCourse(null);
        setResponsibleTeacher(null);
        setValidator(null);
        setCreator(null);

        if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
            changeRectifiedEnrolmentEvaluationToPreviowsState();
            for (; !getEnrolmentEvaluations().isEmpty(); getEnrolmentEvaluations().iterator().next().delete()) {
                ;
            }
        } else {
            for (; !getEnrolmentEvaluations().isEmpty(); getEnrolmentEvaluations().iterator().next().removeFromMarkSheet()) {
                ;
            }
        }

        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void changeRectifiedEnrolmentEvaluationToPreviowsState() {

        /*
         * This is not common, but if by any reason the rectified marksheet was removed from system and rectified enrolment evaluation
         * was removed too, no enrolment evaluation will be available
         */
        if (!hasAnyEnrolmentEvaluations()) {
            return;
        }

        EnrolmentEvaluation enrolmentEvaluation = this.getEnrolmentEvaluations().iterator().next().getRectified();
        enrolmentEvaluation
                .setEnrolmentEvaluationState((enrolmentEvaluation.getMarkSheet().getMarkSheetState() == MarkSheetState.RECTIFICATION) ? EnrolmentEvaluationState.RECTIFICATION_OBJ : EnrolmentEvaluationState.FINAL_OBJ);
    }

    protected void generateCheckSum() {
        if (isNotConfirmed()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getExecutionPeriod().getExecutionYear().getYear()).append(getExecutionPeriod().getSemester());
            stringBuilder.append(getResponsibleTeacher().getPerson().getIstUsername()).append(
                    getEvaluationDateDateTime().toString("yyyy/MM/dd"));
            stringBuilder.append(getMarkSheetType().getName());
            for (EnrolmentEvaluation enrolmentEvaluation : getEnrolmentEvaluationsSortedByStudentNumber()) {
                stringBuilder.append(enrolmentEvaluation.getCheckSum());
            }
            setCheckSum(FenixDigestUtils.createDigest(stringBuilder.toString()));
        }
    }

    public Set<EnrolmentEvaluation> getEnrolmentEvaluationsSortedByStudentNumber() {
        final Set<EnrolmentEvaluation> enrolmentEvaluations =
                new TreeSet<EnrolmentEvaluation>(EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
        enrolmentEvaluations.addAll(getEnrolmentEvaluationsSet());
        return enrolmentEvaluations;
    }

    public EnrolmentEvaluation getEnrolmentEvaluationByStudent(Student student) {
        for (EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
            if (enrolmentEvaluation.getEnrolment().getStudentCurricularPlan().getRegistration().getStudent().equals(student)) {
                return enrolmentEvaluation;
            }
        }
        return null;
    }

    @Atomic
    static public void printMarksheet(MarkSheet markSheet, String printerName) throws FenixServiceException {
        if (markSheet == null) {
            throw new InvalidArgumentsServiceException("mark sheet cannot be null");
        }
        if (!markSheet.isRectification()) {
            printMarkSheet(markSheet, printerName);
        } else {
            printRectificationMarkSheet(markSheet, printerName);
        }
        if (!markSheet.getPrinted()) {
            markSheet.setPrinted(Boolean.TRUE);
        }
    }

    @Atomic
    static public void printMarksheets(final Collection<MarkSheet> markSheets, final Person person, final String printerName)
            throws FenixServiceException {
        for (final MarkSheet markSheet : markSheets) {
            if (markSheet.canManage(person)) {
                try {
                    printMarksheet(markSheet, printerName);
                } catch (InvalidArgumentsServiceException e) {

                }
            }
        }
    }

    static private void printRectificationMarkSheet(MarkSheet markSheet, String printerName) throws UnableToPrintServiceException {
        final EnrolmentEvaluation rectification = markSheet.getEnrolmentEvaluationsSet().iterator().next();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("markSheet", markSheet);
        parameters.put("checkSum", FenixDigestUtils.getPrettyCheckSum(markSheet.getCheckSum()));
        parameters.put("rectification", rectification);
        parameters.put("rectified", rectification.getRectified());

        boolean result =
                ReportsUtils.printReport("markSheetRectification", parameters, Collections.emptyList(), printerName);
        if (!result) {
            throw new UnableToPrintServiceException("error.print.failed");
        }
    }

    static private void printMarkSheet(MarkSheet markSheet, String printerName) throws UnableToPrintServiceException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("markSheet", markSheet);
        parameters.put("checkSum", FenixDigestUtils.getPrettyCheckSum(markSheet.getCheckSum()));
        List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluations());
        Collections.sort(evaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);

        boolean result = ReportsUtils.printReport("markSheet", parameters, evaluations, printerName);
        if (!result) {
            throw new UnableToPrintServiceException("error.print.failed");
        }
    }

    /*
     * Override: Getters and Setters
     */

    @Override
    public void addEnrolmentEvaluations(EnrolmentEvaluation enrolmentEvaluations) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.addEnrolmentEvaluations(enrolmentEvaluations);
        }
    }

    public void removeCurricularCourse() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCurricularCourse(null);
        }
    }

    public void removeValidator() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setValidator(null);
        }
    }

    public void removeCreator() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCreator(null);
        }
    }

    @Override
    public void removeEnrolmentEvaluations(EnrolmentEvaluation enrolmentEvaluations) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeEnrolmentEvaluations(enrolmentEvaluations);
        }
    }

    public void removeExecutionPeriod() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setExecutionPeriod(null);
        }
    }

    public void removeResponsibleTeacher() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setResponsibleTeacher(null);
        }
    }

    public void removeBennu() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setRootDomainObject(null);
        }
    }

    @Override
    public void setCheckSum(String checkSum) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCheckSum(checkSum);
        }
    }

    public void setConfirmationDate(Date confirmationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            if (confirmationDate == null) {
                setConfirmationDateDateTime(null);
            } else {
                setConfirmationDateDateTime(new org.joda.time.DateTime(confirmationDate.getTime()));
            }
        }
    }

    public void setCreationDate(Date creationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            if (creationDate == null) {
                setCreationDateDateTime(null);
            } else {
                setCreationDateDateTime(new org.joda.time.DateTime(creationDate.getTime()));
            }
        }
    }

    @Override
    public void setCurricularCourse(CurricularCourse curricularCourse) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCurricularCourse(curricularCourse);
        }
    }

    @Override
    public void setValidator(Person validator) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setValidator(validator);
        }
    }

    @Override
    public void setCreator(Person creator) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCreator(creator);
        }
    }

    public void setEvaluationDate(Date evaluationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            if (evaluationDate == null) {
                setEvaluationDateDateTime(null);
            } else {
                setEvaluationDateDateTime(new org.joda.time.DateTime(evaluationDate.getTime()));
            }
        }
    }

    @Override
    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setExecutionPeriod(executionSemester);
        }
    }

    @Override
    public void setMarkSheetState(MarkSheetState markSheetState) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setMarkSheetState(markSheetState);
        }
    }

    @Override
    public void setMarkSheetType(MarkSheetType markSheetType) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setMarkSheetType(markSheetType);
        }
    }

    @Override
    public void setResponsibleTeacher(Teacher responsibleTeacher) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setResponsibleTeacher(responsibleTeacher);
        }
    }

    @Override
    public void setConfirmationDateDateTime(DateTime confirmationDateDateTime) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setConfirmationDateDateTime(confirmationDateDateTime);
        }
    }

    @Override
    public void setCreationDateDateTime(DateTime creationDateDateTime) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCreationDateDateTime(creationDateDateTime);
        }
    }

    @Override
    public void setEvaluationDateDateTime(DateTime evaluationDateDateTime) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setEvaluationDateDateTime(evaluationDateDateTime);
        }
    }

    @Override
    public void setReason(String reason) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setReason(reason);
        }
    }

    @Override
    public void setSubmittedByTeacher(Boolean submittedByTeacher) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setSubmittedByTeacher(submittedByTeacher);
        }
    }

    public String getPrettyCheckSum() {
        return FenixDigestUtils.getPrettyCheckSum(getCheckSum());
    }

    public boolean canManage(final Person person) {
        Set<Degree> degreesForOperation =
                AcademicAuthorizationGroup.getDegreesForOperation(person, AcademicOperationType.MANAGE_MARKSHEETS);
        return degreesForOperation.contains(getCurricularCourse().getDegreeCurricularPlan().getDegree());
    }

    public void removeGrades(Collection<EnrolmentEvaluation> enrolmentEvaluations) {
        if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
            if (getMarkSheetState() == MarkSheetState.CONFIRMED) {
                super.setMarkSheetState(MarkSheetState.NOT_CONFIRMED);
            }
            if (getMarkSheetState() == MarkSheetState.RECTIFICATION) {
                super.setMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
            }

            setValidator(null);
            setConfirmationDateDateTime(null);

            for (EnrolmentEvaluation enrolmentEvaluation : getEnrolmentEvaluationsSet()) {
                if (enrolmentEvaluations.contains(enrolmentEvaluation)) {
                    if (enrolmentEvaluation.hasRectification()) {
                        throw new DomainException("error.enrolment.evaluation.has.rectification");
                    }
                    removeEvaluationFromMarkSheet(enrolmentEvaluation);
                } else {
                    changeEvaluationStateToTemporaryState(enrolmentEvaluation);
                }
            }
            generateCheckSum();
        }
    }

    private void removeEvaluationFromMarkSheet(EnrolmentEvaluation enrolmentEvaluation) {
        changeEvaluationStateToTemporaryState(enrolmentEvaluation);
        enrolmentEvaluation.setMarkSheet(null);
        enrolmentEvaluation.setGrade(Grade.createEmptyGrade());
        enrolmentEvaluation.setCheckSum(null);
        enrolmentEvaluation.setExamDateYearMonthDay(null);
        enrolmentEvaluation.setPerson(null);
        enrolmentEvaluation.setGradeAvailableDateYearMonthDay(null);
        enrolmentEvaluation.setPersonResponsibleForGrade(null);
        enrolmentEvaluation.setRectified(null);
    }

    private void changeEvaluationStateToTemporaryState(final EnrolmentEvaluation enrolmentEvaluation) {
        enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);

        Enrolment enrolment = enrolmentEvaluation.getEnrolment();

        if (enrolment.getAllFinalEnrolmentEvaluations().isEmpty()) {
            enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
        } else {
            enrolment.setEnrollmentState(enrolment.getLatestEnrolmentEvaluation().getEnrollmentStateByGrade());
        }
    }

    public boolean getCanRectify() {
        return isConfirmed() && MarkSheetPredicates.rectifyPredicate.evaluate(this);
    }

    public boolean getCanConfirm() {
        return isNotConfirmed() && !getEnrolmentEvaluationsSet().isEmpty() && MarkSheetPredicates.confirmPredicate.evaluate(this);
    }

    public boolean getCanEdit() {
        return isNotConfirmed() && MarkSheetPredicates.editPredicate.evaluate(this);
    }

    public boolean getCanRemoveGrades() {
        return isConfirmed() && MarkSheetPredicates.removeGradesPredicate.evaluate(this);
    }

    public boolean isDissertation() {
        return getCurricularCourse().isDissertation();
    }

    public String getStateDiscription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BundleUtil.getString(Bundle.ENUMERATION, getMarkSheetState().getName()).trim());
        if (getSubmittedByTeacher()) {
            stringBuilder.append(" (").append(BundleUtil.getString(Bundle.ACADEMIC, "label.markSheet.submittedByTeacher").trim())
                    .append(")");
        }
        return stringBuilder.toString();
    }

    public String getDegreeName() {
        return getCurricularCourse().getDegree().getNameFor(getExecutionPeriod()).getContent();
    }

    public String getDegreeCurricularPlanName() {
        return getCurricularCourse().getDegreeCurricularPlan().getName();
    }

    public String getCurricularCourseName() {
        return getCurricularCourse().getName(getExecutionPeriod());
    }

    public String getCurricularCourseAcronym() {
        return getCurricularCourse().getAcronym(getExecutionPeriod());
    }

    public boolean isFor(DegreeCurricularPlan dcp) {
        return getCurricularCourse().getDegreeCurricularPlan().equals(dcp);
    }

    @Deprecated
    public java.util.Date getConfirmationDate() {
        org.joda.time.DateTime dt = getConfirmationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public java.util.Date getCreationDate() {
        org.joda.time.DateTime dt = getCreationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public java.util.Date getEvaluationDate() {
        org.joda.time.DateTime dt = getEvaluationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EnrolmentEvaluation> getEnrolmentEvaluations() {
        return getEnrolmentEvaluationsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolmentEvaluations() {
        return !getEnrolmentEvaluationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasResponsibleTeacher() {
        return getResponsibleTeacher() != null;
    }

    @Deprecated
    public boolean hasMarkSheetType() {
        return getMarkSheetType() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreationDateDateTime() {
        return getCreationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasEvaluationDateDateTime() {
        return getEvaluationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasValidator() {
        return getValidator() != null;
    }

    @Deprecated
    public boolean hasConfirmationDateDateTime() {
        return getConfirmationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasArchiveInformation() {
        return getArchiveInformation() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasCheckSum() {
        return getCheckSum() != null;
    }

    @Deprecated
    public boolean hasPrinted() {
        return getPrinted() != null;
    }

    @Deprecated
    public boolean hasMarkSheetState() {
        return getMarkSheetState() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

    @Deprecated
    public boolean hasSubmittedByTeacher() {
        return getSubmittedByTeacher() != null;
    }

    @Deprecated
    public boolean hasReason() {
        return getReason() != null;
    }

}
