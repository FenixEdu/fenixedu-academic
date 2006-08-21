/*
 * Created on Nov 18, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoImprovmentEnrolmentContext;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author nmgo
 */
public class ReadImprovmentsToEnroll extends Service {

    public Object run(Integer studentNumber, Integer executionPeriodID) throws FenixServiceException,
            ExcepcaoPersistencia {
        List previousExecPeriodAprovedEnrol = new ArrayList();
        List beforePreviousExecPeriodAprovedEnrol = new ArrayList();
        List beforeBeforePreviousExecPeriodAprovedEnrol = new ArrayList();

        // Read Execution Periods
        ExecutionPeriod actualExecPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        if (actualExecPeriod == null) {
            throw new InvalidArgumentsServiceException("error.executionPeriod.notExist");
        }

        ExecutionPeriod previousExecPeriod = actualExecPeriod.getPreviousExecutionPeriod();
        ExecutionPeriod beforePreviousExecPeriod = previousExecPeriod.getPreviousExecutionPeriod();
        ExecutionPeriod beforeBeforePreviousExecPeriod = beforePreviousExecPeriod
                .getPreviousExecutionPeriod();

        // Read Registration
        Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);

        if (registration == null) {
            throw new InvalidArgumentsServiceException("error.student.notExist");
        }

        // Read Aproved Enrolments by Execution OccupationPeriod
        List studentCurricularPlans = registration.getStudentCurricularPlans();

        Iterator<StudentCurricularPlan> iterator = studentCurricularPlans.iterator();
        while (iterator.hasNext()) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();

            if (previousExecPeriod != null) {
                previousExecPeriodAprovedEnrol.addAll(studentCurricularPlan
                        .getAprovedEnrolmentsInExecutionPeriod(previousExecPeriod));
            }

            if (beforePreviousExecPeriod != null) {
                beforePreviousExecPeriodAprovedEnrol.addAll(studentCurricularPlan
                        .getAprovedEnrolmentsInExecutionPeriod(beforePreviousExecPeriod));
            }

            if (beforeBeforePreviousExecPeriod != null) {
                beforeBeforePreviousExecPeriodAprovedEnrol.addAll(studentCurricularPlan
                        .getAprovedEnrolmentsInExecutionPeriod(beforeBeforePreviousExecPeriod));
            }
        }

        // Remove Enrolments From Equivalences
        removeEquivalenceEnrolment(previousExecPeriodAprovedEnrol);
        removeEquivalenceEnrolment(beforePreviousExecPeriodAprovedEnrol);
        removeEquivalenceEnrolment(beforeBeforePreviousExecPeriodAprovedEnrol);

        // Remove Enrolments Already Improved and get Improvment Enrolments of
        // this Execution OccupationPeriod
        List alreadyImprovedEnrolmentsInCurrentExecutionPeriod = new ArrayList();
        alreadyImprovedEnrolmentsInCurrentExecutionPeriod
                .addAll(removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(actualExecPeriod,
                        beforePreviousExecPeriodAprovedEnrol));
        alreadyImprovedEnrolmentsInCurrentExecutionPeriod
                .addAll(removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(actualExecPeriod,
                        previousExecPeriodAprovedEnrol));
        alreadyImprovedEnrolmentsInCurrentExecutionPeriod
                .addAll(removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(actualExecPeriod,
                        beforeBeforePreviousExecPeriodAprovedEnrol));

        // From Before Before Previous OccupationPeriod remove the ones with
        // scope in
        // Previous OccupationPeriod
        removeFromBeforeBeforePrevioupersistentSupporteriod(beforeBeforePreviousExecPeriodAprovedEnrol,
                previousExecPeriod);

        // From previous OccupationPeriod remove the ones that not take place in
        // the
        // Current OccupationPeriod
        previousExecPeriodAprovedEnrol = removeNotInCurrentExecutionPeriod(
                previousExecPeriodAprovedEnrol, actualExecPeriod);

        List res = (List) CollectionUtils.union(beforePreviousExecPeriodAprovedEnrol,
                previousExecPeriodAprovedEnrol);

        res = (List) CollectionUtils.union(beforeBeforePreviousExecPeriodAprovedEnrol, res);

        return buildResult(registration, actualExecPeriod, res,
                alreadyImprovedEnrolmentsInCurrentExecutionPeriod);
    }

    private void removeFromBeforeBeforePrevioupersistentSupporteriod(
            List beforeBeforePreviousExecPeriodAprovedEnrol, final ExecutionPeriod previousExecPeriod) {
        CollectionUtils.filter(beforeBeforePreviousExecPeriodAprovedEnrol, new Predicate() {

            public boolean evaluate(Object arg0) {
                Enrolment enrolment = (Enrolment) arg0;
                List executionCourses = enrolment.getCurricularCourse().getAssociatedExecutionCourses();
                for (Iterator iterator = executionCourses.iterator(); iterator.hasNext();) {
                    ExecutionCourse executionCourse = (ExecutionCourse) iterator.next();
                    if (executionCourse.getExecutionPeriod().equals(previousExecPeriod)) {
                        return false;
                    }
                }
                return true;
            }

        });

    }

    /**
     * @param actualExecPeriod
     * @param previousExecPeriodAprovedEnrol
     * @return
     */
    private List removeImprovedEnrolmentAndGetImprovmentsOfCurrentPeriod(
            ExecutionPeriod actualExecPeriod, List enrolments) {
        List improvments = (List) CollectionUtils.select(enrolments, new Predicate() {

            public boolean evaluate(Object arg0) {
                Enrolment enrollment = (Enrolment) arg0;
                if (CollectionUtils.find(enrollment.getEvaluations(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) arg0;
                        if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(
                                EnrolmentEvaluationType.IMPROVEMENT))
                            return true;
                        return false;
                    }

                }) != null)
                    return true;
                return false;
            }
        });
        enrolments.removeAll(improvments);

        return (List) CollectionUtils.select(improvments, new Predicate() {

            public boolean evaluate(Object arg0) {
                Enrolment enrollment = (Enrolment) arg0;
                EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) CollectionUtils.find(
                        enrollment.getEvaluations(), new Predicate() {

                            public boolean evaluate(Object arg0) {
                                EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) arg0;
                                if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(
                                        EnrolmentEvaluationType.IMPROVEMENT))
                                    return true;
                                return false;
                            }

                        });

                if (enrolmentEvaluation.getEnrolmentEvaluationState().equals(
                        EnrolmentEvaluationState.TEMPORARY_OBJ))
                    return true;
                return false;
            }

        });
    }

    /**
     * @param student
     * @param actualExecPeriod
     * @param res
     * @param alreadyImprovedEnrolmentsInCurrentExecutionPeriod
     * @return
     */
    private InfoImprovmentEnrolmentContext buildResult(Registration student,
            ExecutionPeriod actualExecPeriod, List res,
            List alreadyImprovedEnrolmentsInCurrentExecutionPeriod) {
        InfoImprovmentEnrolmentContext improvmentEnrolmentContext = new InfoImprovmentEnrolmentContext();
        improvmentEnrolmentContext.setInfoStudent(InfoStudent.newInfoFromDomain(student));
        improvmentEnrolmentContext.setInfoExecutionPeriod(InfoExecutionPeriod
                .newInfoFromDomain(actualExecPeriod));

        improvmentEnrolmentContext.setImprovmentsToEnroll((List) CollectionUtils.collect(res,
                new Transformer() {

                    public Object transform(Object arg0) {
                        Enrolment enrollment = (Enrolment) arg0;
                        return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                                .newInfoFromDomain(enrollment);
                    }

                }));

        improvmentEnrolmentContext.setAlreadyEnrolled((List) CollectionUtils.collect(
                alreadyImprovedEnrolmentsInCurrentExecutionPeriod, new Transformer() {

                    public Object transform(Object arg0) {
                        Enrolment enrollment = (Enrolment) arg0;
                        return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                                .newInfoFromDomain(enrollment);
                    }

                }));

        return improvmentEnrolmentContext;
    }

    private void removeEquivalenceEnrolment(List enrolments) {
        CollectionUtils.filter(enrolments, new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                if (enrollment.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.EQUIVALENCE))
                    return false;
                return true;
            }
        });
    }

    private List<Enrolment> removeNotInCurrentExecutionPeriod(List<Enrolment> enrolments,
            final ExecutionPeriod currentExecutionPeriod) throws ExcepcaoPersistencia {
        final List<Enrolment> res = new ArrayList<Enrolment>();
        for (final Enrolment enrolment : enrolments) {
            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            Set<CurricularCourseScope> scopes = curricularCourse.findCurricularCourseScopesIntersectingPeriod(
                    currentExecutionPeriod.getBeginDate(), currentExecutionPeriod.getEndDate());
            if (scopes != null && !scopes.isEmpty()) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) CollectionUtils
                        .find(scopes, new Predicate() {

                            public boolean evaluate(Object arg0) {
                                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                                if (curricularCourseScope.getCurricularSemester().getSemester().equals(
                                        currentExecutionPeriod.getSemester())
                                        && (curricularCourseScope.getEndDate() == null || (curricularCourseScope
                                                .getEnd().compareTo(new Date())) >= 0))
                                    return true;
                                return false;
                            }
                        });

                if (curricularCourseScope != null)
                    res.add(enrolment);
            }

        }
        return res;
    }
}
