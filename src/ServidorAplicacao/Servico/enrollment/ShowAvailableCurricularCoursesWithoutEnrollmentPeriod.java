package ServidorAplicacao.Servico.enrollment;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import DataBeans.InfoExecutionPeriodWithInfoExecutionYear;
import DataBeans.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch;
import DataBeans.enrollment.InfoCurricularCourse2Enroll;
import DataBeans.enrollment.InfoCurricularCourse2EnrollWithInfoCurricularCourse;
import Dominio.IEnrollment;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.CurricularCourse2Enroll;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jan 27, 2004
 */

public class ShowAvailableCurricularCoursesWithoutEnrollmentPeriod implements
        IService {
    public ShowAvailableCurricularCoursesWithoutEnrollmentPeriod() {
    }

    // some of these arguments may be null. they are only needed for filter
    public InfoStudentEnrollmentContext run(Integer executionDegreeId,
            Integer studentCurricularPlanId, Integer studentNumber)
            throws FenixServiceException {
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
     */
    protected InfoStudentEnrollmentContext getInfoStudentEnrollmentContext(
            final IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        List curricularCourses2Enroll = studentCurricularPlan
                .getCurricularCoursesToEnroll(null, EnrollmentRuleType.TOTAL);

        infoStudentEnrolmentContext
                .setCurricularCourses2Enroll((List) CollectionUtils.collect(
                        curricularCourses2Enroll, new Transformer() {

                            public Object transform(Object arg0) {
                                InfoCurricularCourse2Enroll infoCurricularCourse = InfoCurricularCourse2EnrollWithInfoCurricularCourse
                                        .newInfoFromDomain((CurricularCourse2Enroll) arg0);
                                try {
                                    infoCurricularCourse
                                            .setCurricularYear(InfoCurricularYear
                                                    .newInfoFromDomain(((CurricularCourse2Enroll) arg0)
                                                            .getCurricularCourse()
                                                            .getCurricularYearByBranchAndSemester(
                                                                    studentCurricularPlan
                                                                            .getBranch(),
                                                                    getCurrentExecutionPeriod()
                                                                            .getSemester())));
                                } catch (ExcepcaoPersistencia e) {
                                    infoCurricularCourse
                                            .setCurricularYear(InfoCurricularYear
                                                    .newInfoFromDomain(((CurricularCourse2Enroll) arg0)
                                                            .getCurricularCourse()
                                                            .getCurricularYearByBranchAndSemester(
                                                                    studentCurricularPlan
                                                                            .getBranch(),
                                                                    null)));
                                }
                                return infoCurricularCourse;
                            }
                        }));
        Collections.sort(infoStudentEnrolmentContext
                .getCurricularCourses2Enroll(), new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoCurricularCourse2Enroll obj1 = (InfoCurricularCourse2Enroll) o1;
                InfoCurricularCourse2Enroll obj2 = (InfoCurricularCourse2Enroll) o2;
                return obj1.getCurricularYear().getYear().compareTo(
                        obj2.getCurricularYear().getYear());
            }
        });
        infoStudentEnrolmentContext
                .setStudentCurrentSemesterInfoEnrollments((List) CollectionUtils
                        .collect(
                                studentCurricularPlan
                                        .getAllStudentEnrolledEnrollmentsInExecutionPeriod(null),
                                new Transformer() {

                                    public Object transform(Object arg0) {

                                        return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                                                .newInfoFromDomain((IEnrollment) arg0);
                                    }
                                }));
        infoStudentEnrolmentContext
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch
                        .newInfoFromDomain(studentCurricularPlan));
        infoStudentEnrolmentContext
                .setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                        .newInfoFromDomain(getCurrentExecutionPeriod()));
        infoStudentEnrolmentContext
                .setCreditsInSpecializationArea(studentCurricularPlan
                        .getCreditsInSpecializationArea());
        infoStudentEnrolmentContext
                .setCreditsInSecundaryArea(studentCurricularPlan
                        .getCreditsInSecundaryArea());
        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentActiveCurricularPlan
     * @return IEnrolmentPeriod
     * @throws ExcepcaoPersistencia
     * @throws OutOfCurricularCourseEnrolmentPeriod
     */
    public static IEnrolmentPeriod getEnrolmentPeriod(
            IStudentCurricularPlan studentActiveCurricularPlan)
            throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSuport
                .getIPersistentEnrolmentPeriod();
        IEnrolmentPeriod enrolmentPeriod = enrolmentPeriodDAO
                .readActualEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
                        .getDegreeCurricularPlan());
        if (enrolmentPeriod == null) {
            IEnrolmentPeriod nextEnrolmentPeriod = enrolmentPeriodDAO
                    .readNextEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
                            .getDegreeCurricularPlan());
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
    protected IStudent getStudent(Integer studentNumber)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentStudent studentDAO = persistentSuport
                .getIPersistentStudent();

        return studentDAO.readStudentByNumberAndDegreeType(studentNumber,
                TipoCurso.LICENCIATURA_OBJ);
    }

    /**
     * @param student
     * @return IStudentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    protected IStudentCurricularPlan getStudentCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport
                .getIStudentCurricularPlanPersistente();

        return studentCurricularPlanDAO.readActiveStudentCurricularPlan(student
                .getNumber(), student.getDegreeType());
    }

    /**
     * @return IExecutionPeriod
     * @throws ExcepcaoPersistencia
     */
    protected IExecutionPeriod getCurrentExecutionPeriod()
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB
                .getInstance();
        IPersistentExecutionPeriod executionPeriodDAO = persistentSuport
                .getIPersistentExecutionPeriod();

        return executionPeriodDAO.readActualExecutionPeriod();
    }

}