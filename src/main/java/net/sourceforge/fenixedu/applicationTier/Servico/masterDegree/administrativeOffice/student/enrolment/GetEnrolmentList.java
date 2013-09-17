package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class GetEnrolmentList {

    @Atomic
    public static List<InfoEnrolment> run(String studentCurricularPlanID, EnrollmentState enrollmentState) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
        for (final Enrolment enrolment : getStudentCurricularPlan(studentCurricularPlanID).getEnrolmentsByState(enrollmentState)) {
            if (enrolment.isExtraCurricular()) {
                continue;
            }
            if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
                result.add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }
        return result;
    }

    @Atomic
    public static List<InfoEnrolment> run(String studentCurricularPlanID) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
        for (final Enrolment enrolment : getStudentCurricularPlan(studentCurricularPlanID).getEnrolmentsSet()) {
            if (enrolment.isExtraCurricular()) {
                continue;
            }
            if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
                result.add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }
        return result;
    }

    @Atomic
    public static List<InfoEnrolment> run(String studentCurricularPlanID, EnrollmentState enrollmentState, Boolean pTypeEnrolments) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        return run(studentCurricularPlanID, enrollmentState, pTypeEnrolments, Boolean.FALSE);
    }

    @Atomic
    public static List<InfoEnrolment> run(String studentCurricularPlanID, EnrollmentState enrollmentState,
            Boolean pTypeEnrolments, Boolean includeExtraCurricular) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        if (!pTypeEnrolments.booleanValue()) {
            return run(studentCurricularPlanID, enrollmentState);
        }

        final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();

        for (final Enrolment enrolment : getStudentCurricularPlan(studentCurricularPlanID).getEnrolmentsByState(enrollmentState)) {
            if (enrolment.isExtraCurricular() && !includeExtraCurricular) {
                continue;
            }
            result.add(InfoEnrolment.newInfoFromDomain(enrolment));
        }
        return result;
    }

    private static StudentCurricularPlan getStudentCurricularPlan(String studentCurricularPlanID) {
        return FenixFramework.getDomainObject(studentCurricularPlanID);
    }

}