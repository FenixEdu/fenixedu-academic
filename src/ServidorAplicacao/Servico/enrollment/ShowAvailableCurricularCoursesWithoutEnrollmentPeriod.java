package ServidorAplicacao.Servico.enrollment;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import DataBeans.InfoExecutionPeriodWithInfoExecutionYear;
import DataBeans.InfoStudentCurricularPlanWithInfoStudent;
import DataBeans.enrollment.InfoCurricularCourse2EnrollWithInfoCurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.enrollment.CurricularCourse2Enroll;
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
import Util.enrollment.CurricularCourseEnrollmentType;
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

                } else {
                    throw new ExistingServiceException("studentCurricularPlan");
                }
            } else {
                throw new ExistingServiceException("student");
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
    }

    /**
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    protected InfoStudentEnrollmentContext getInfoStudentEnrollmentContext(
            IStudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia {

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        List curricularCourses2Enroll = (List) CollectionUtils.collect(
                studentCurricularPlan.getCurricularCoursesToEnroll(null,
                        EnrollmentRuleType.TOTAL), new Transformer() {

                    public Object transform(Object arg0) {
                        if (arg0 instanceof ICurricularCourse) {
                            ICurricularCourse curricularCourse = (ICurricularCourse) arg0;

                            return new CurricularCourse2Enroll(
                                    curricularCourse,
                                    CurricularCourseEnrollmentType.TEMPORARY);
                        } else {
                            return arg0;
                        }
                    }
                });

        infoStudentEnrolmentContext
                .setCurricularCourses2Enroll((List) CollectionUtils.collect(
                        curricularCourses2Enroll, new Transformer() {

                            public Object transform(Object arg0) {

                                return InfoCurricularCourse2EnrollWithInfoCurricularCourse
                                        .newInfoFromDomain((CurricularCourse2Enroll) arg0);
                            }
                        }));
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
                .setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudent
                        .newInfoFromDomain(studentCurricularPlan));
        infoStudentEnrolmentContext
                .setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear
                        .newInfoFromDomain(getCurrentExecutionPeriod()));
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