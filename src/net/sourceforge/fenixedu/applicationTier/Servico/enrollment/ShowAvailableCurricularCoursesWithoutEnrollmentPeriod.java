package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in Jan 27, 2004
 */

public class ShowAvailableCurricularCoursesWithoutEnrollmentPeriod implements IService {
    public ShowAvailableCurricularCoursesWithoutEnrollmentPeriod() {
    }

    // some of these arguments may be null. they are only needed for filter
    public InfoStudentEnrollmentContext run(Integer executionDegreeId, Integer studentCurricularPlanId,
            Integer studentNumber) throws Exception {
        try {
            IStudent student = getStudent(studentNumber);

            if (student != null) {
                IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

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

        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
    }

    /**
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     * @throws EnrolmentRuleServiceException 
     */
    protected InfoStudentEnrollmentContext getInfoStudentEnrollmentContext(
            final IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia, EnrolmentRuleServiceException {

        final IExecutionPeriod executionPeriod = getExecutionPeriod(null);

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();

        List curricularCourses2Enroll;
        try {
            curricularCourses2Enroll = studentCurricularPlan.getCurricularCoursesToEnroll(executionPeriod);
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
            final IStudentCurricularPlan studentCurricularPlan, final IExecutionPeriod executionPeriod,
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
            final IStudentCurricularPlan studentCurricularPlan, final IExecutionPeriod executionPeriod) {
        return (List) CollectionUtils.collect(studentCurricularPlan
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod), new Transformer() {

            public Object transform(Object arg0) {

                return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                        .newInfoFromDomain((IEnrolment) arg0);
            }
        });
    }

    /**
     * @param studentActiveCurricularPlan
     * @return IEnrollmentPeriodInCurricularCourses
     * @throws ExcepcaoPersistencia
     * @throws OutOfCurricularCourseEnrolmentPeriod
     */
    public static IEnrolmentPeriodInCurricularCourses getEnrolmentPeriod(
            IStudentCurricularPlan studentActiveCurricularPlan) throws ExcepcaoPersistencia,
            OutOfCurricularCourseEnrolmentPeriod {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSuport.getIPersistentEnrolmentPeriod();
        IEnrolmentPeriodInCurricularCourses enrolmentPeriod = enrolmentPeriodDAO
                .readActualEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
                        .getDegreeCurricularPlan().getIdInternal());
        if (enrolmentPeriod == null) {
            IEnrolmentPeriodInCurricularCourses nextEnrolmentPeriod = enrolmentPeriodDAO
                    .readNextEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
                            .getDegreeCurricularPlan().getIdInternal());
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

    /**
     * @param studentNumber
     * @return IStudent
     * @throws ExcepcaoPersistencia
     */
    protected IStudent getStudent(Integer studentNumber) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent studentDAO = persistentSuport.getIPersistentStudent();

        return studentDAO.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
    }

    /**
     * @param student
     * @return IStudentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    protected IStudentCurricularPlan getStudentCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSuport
                .getIStudentCurricularPlanPersistente();

        return studentCurricularPlanDAO.readActiveStudentCurricularPlan(student.getNumber(), student
                .getDegreeType());
    }

    protected IExecutionPeriod getExecutionPeriod(IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod2Return = executionPeriod;

        if (executionPeriod == null) {
            ISuportePersistente daoFactory = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod executionPeriodDAO = daoFactory.getIPersistentExecutionPeriod();
            executionPeriod2Return = executionPeriodDAO.readActualExecutionPeriod();
        }

        return executionPeriod2Return;
    }
}