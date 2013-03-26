package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class GetEnrolmentList extends FenixService {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List<InfoEnrolment> run(Integer studentCurricularPlanID, EnrollmentState enrollmentState) {

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

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List<InfoEnrolment> run(Integer studentCurricularPlanID) {
        final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
        for (final Enrolment enrolment : getStudentCurricularPlan(studentCurricularPlanID).getEnrolments()) {
            if (enrolment.isExtraCurricular()) {
                continue;
            }
            if (!enrolment.getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE)) {
                result.add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }
        return result;
    }

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List<InfoEnrolment> run(Integer studentCurricularPlanID, EnrollmentState enrollmentState,
            Boolean pTypeEnrolments) {
        return run(studentCurricularPlanID, enrollmentState, pTypeEnrolments, Boolean.FALSE);
    }

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List<InfoEnrolment> run(Integer studentCurricularPlanID, EnrollmentState enrollmentState,
            Boolean pTypeEnrolments, Boolean includeExtraCurricular) {

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

    private static StudentCurricularPlan getStudentCurricularPlan(Integer studentCurricularPlanID) {
        return rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);
    }

}