package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentRuleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoCurricularCourse2Enroll;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoCurricularCourse2EnrollWithInfoCurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author David Santos in Jan 27, 2004
 */

public class ShowAvailableCurricularCoursesWithoutEnrollmentPeriod extends Service {
    public ShowAvailableCurricularCoursesWithoutEnrollmentPeriod() {
    }

    // some of these arguments may be null. they are only needed for filter
    public InfoStudentEnrollmentContext run(Integer executionDegreeId, Integer studentCurricularPlanId,
            Integer studentNumber) throws Exception {
        Student student = getStudent(studentNumber);

        if (student != null) {
            StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();

            if (studentCurricularPlan != null) {
                //					
                try {

                    return getInfoStudentEnrollmentContext(studentCurricularPlan);
                } catch (IllegalArgumentException e) {
                    throw new FenixServiceException("degree");
                }

            }
            throw new ExistingServiceException("studentCurricularPlan");

        }
        throw new ExistingServiceException("student");
    }

    /**
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     * @throws EnrolmentRuleServiceException
     */
    protected InfoStudentEnrollmentContext getInfoStudentEnrollmentContext(
            final StudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia,
            EnrolmentRuleServiceException {

        final ExecutionPeriod executionPeriod = getExecutionPeriod(null);

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();

        List curricularCourses2Enroll;
        try {
            curricularCourses2Enroll = studentCurricularPlan
                    .getCurricularCoursesToEnroll(executionPeriod);
        } catch (FenixDomainException e) {
            throw new EnrolmentRuleServiceException(e.getErrorType());
        }

        infoStudentEnrolmentContext
                .setCurricularCourses2Enroll(getInfoCurricularCoursesToEnrollFromCurricularCourses(
                        studentCurricularPlan, executionPeriod, curricularCourses2Enroll));

        Collections.sort(infoStudentEnrolmentContext.getCurricularCourses2Enroll(), new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoCurricularCourse2Enroll obj1 = (InfoCurricularCourse2Enroll) o1;
                InfoCurricularCourse2Enroll obj2 = (InfoCurricularCourse2Enroll) o2;
                return obj1.getCurricularYear().getYear().compareTo(obj2.getCurricularYear().getYear());
            }
        });
        infoStudentEnrolmentContext
                .setStudentCurrentSemesterInfoEnrollments(getStudentEnrollmentsWithStateEnrolledInExecutionPeriod(
                        studentCurricularPlan, executionPeriod));
        infoStudentEnrolmentContext
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch
                        .newInfoFromDomain(studentCurricularPlan));
        infoStudentEnrolmentContext.setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                .newInfoFromDomain(executionPeriod));
        infoStudentEnrolmentContext.setCreditsInSpecializationArea(studentCurricularPlan
                .getCreditsInSpecializationArea());
        infoStudentEnrolmentContext.setCreditsInSecundaryArea(studentCurricularPlan
                .getCreditsInSecundaryArea());
        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentCurricularPlan
     * @param executionPeriod
     * @param curricularCourses2Enroll
     * @return
     */
    protected List getInfoCurricularCoursesToEnrollFromCurricularCourses(
            final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod,
            List curricularCourses2Enroll) {
        return (List) CollectionUtils.collect(curricularCourses2Enroll, new Transformer() {

            public Object transform(Object arg0) {
                InfoCurricularCourse2Enroll infoCurricularCourse = InfoCurricularCourse2EnrollWithInfoCurricularCourse
                        .newInfoFromDomain((CurricularCourse2Enroll) arg0);

                infoCurricularCourse.setCurricularYear(InfoCurricularYear
                        .newInfoFromDomain(((CurricularCourse2Enroll) arg0).getCurricularCourse()
                                .getCurricularYearByBranchAndSemester(studentCurricularPlan.getBranch(),
                                        executionPeriod.getSemester())));
                return infoCurricularCourse;
            }
        });
    }

    /**
     * @param studentCurricularPlan
     * @param executionPeriod
     * @return
     */
    protected List getStudentEnrollmentsWithStateEnrolledInExecutionPeriod(
            final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod) {
        return (List) CollectionUtils.collect(studentCurricularPlan
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod), new Transformer() {

            public Object transform(Object arg0) {

                return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                        .newInfoFromDomain((Enrolment) arg0);
            }
        });
    }

    /**
     * @param studentActiveCurricularPlan
     * @return IEnrollmentPeriodInCurricularCourses
     * @throws ExcepcaoPersistencia
     * @throws OutOfCurricularCourseEnrolmentPeriod
     */
    public static EnrolmentPeriodInCurricularCourses getEnrolmentPeriod(
            StudentCurricularPlan studentActiveCurricularPlan) throws ExcepcaoPersistencia,
            OutOfCurricularCourseEnrolmentPeriod {
        EnrolmentPeriodInCurricularCourses enrolmentPeriod = studentActiveCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriod();
        if (enrolmentPeriod == null) {
        	EnrolmentPeriodInCurricularCourses nextEnrolmentPeriod = studentActiveCurricularPlan.getDegreeCurricularPlan().getNextEnrolmentPeriod();
            Date startDate = null;
            Date endDate = null;
            if (nextEnrolmentPeriod != null) {
                startDate = nextEnrolmentPeriod.getStartDate();
                endDate = nextEnrolmentPeriod.getEndDate();
            }
            throw new OutOfCurricularCourseEnrolmentPeriod(startDate, endDate);
        }
        return enrolmentPeriod;
    }

    protected Student getStudent(Integer studentNumber) throws ExcepcaoPersistencia {
        return Student.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
    }

    /*protected StudentCurricularPlan getStudentCurricularPlan(Student student)
            throws ExcepcaoPersistencia {

        return stude
    }*/

    protected ExecutionPeriod getExecutionPeriod(ExecutionPeriod executionPeriod) {
        return (executionPeriod == null) ? ExecutionPeriod.readActualExecutionPeriod() : executionPeriod;
    }
}