package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IEnrollment;
import Dominio.IExecutionYear;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author David Santos in Mar 19, 2004
 */

public class PrepareStudentEnrolmentContextForOptionalCoursesEnrolment
        implements IService {

    public PrepareStudentEnrolmentContextForOptionalCoursesEnrolment() {
    }

    public Object run(InfoStudent infoStudent, TipoCurso degreeType, String year)
            throws FenixServiceException {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IStudentCurricularPlanPersistente studentCurricularPlanDAO = sp
                    .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = null;
            if (infoStudent != null && infoStudent.getNumber() != null) {
                studentCurricularPlan = studentCurricularPlanDAO
                        .readActiveByStudentNumberAndDegreeType(infoStudent
                                .getNumber(), degreeType);
            }

            if (studentCurricularPlan == null) {
                throw new FenixServiceException(
                        "error.student.curriculum.noCurricularPlans");
            }

            IPersistentExecutionYear executionYearDAO = sp
                    .getIPersistentExecutionYear();
            IExecutionYear executionYear = executionYearDAO
                    .readExecutionYearByName(year);

            if (isStudentCurricularPlanFromChosenExecutionYear(
                    studentCurricularPlan, executionYear)) {
                IPersistentEnrolment enrolmentDAO = sp
                        .getIPersistentEnrolment();
                IPersistentCurricularCourse curricularCourseDAO = sp
                        .getIPersistentCurricularCourse();

                List enrollmentsEnrolled = enrolmentDAO
                        .readEnrolmentsByStudentCurricularPlanAndEnrolmentStateAndCurricularCourseType(
                                studentCurricularPlan, EnrolmentState.ENROLLED,
                                CurricularCourseType.OPTIONAL_COURSE_OBJ);

                List enrollments = studentCurricularPlan.getEnrolments();

                List coursesFromEnrollments = (List) CollectionUtils.collect(
                        enrollments, new Transformer() {
                            public Object transform(Object input) {
                                IEnrollment enrolment = (IEnrollment) input;
                                return enrolment.getCurricularCourse();
                            }
                        });

                List coursesFromStudentCurricularPlan = curricularCourseDAO
                        .readAllByDegreeCurricularPlanAndType(
                                studentCurricularPlan.getDegreeCurricularPlan(),
                                CurricularCourseType.OPTIONAL_COURSE_OBJ);

                List courses = (List) CollectionUtils.subtract(
                        coursesFromStudentCurricularPlan,
                        coursesFromEnrollments);

                infoStudentEnrolmentContext = buildResult(
                        studentCurricularPlan, enrollmentsEnrolled, courses,
                        executionYear);
            } else {
                throw new FenixServiceException(
                        "error.student.curriculum.not.from.chosen.execution.year");
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentCurricularPlan
     * @param year
     * @return true/false
     * @throws ExcepcaoPersistencia
     */
    private boolean isStudentCurricularPlanFromChosenExecutionYear(
            IStudentCurricularPlan studentCurricularPlan,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        ICursoExecucaoPersistente executionDegreeDAO = sp
                .getICursoExecucaoPersistente();

        if (executionYear != null) {
            ICursoExecucao executionDegree = executionDegreeDAO
                    .readByDegreeCurricularPlanAndExecutionYear(
                            studentCurricularPlan.getDegreeCurricularPlan(),
                            executionYear);
            return executionDegree != null;
        }
        return false;

    }

    /**
     * @param studentCurricularPlan
     * @param enrollments
     * @param courses
     * @return InfoStudentEnrolmentContext
     */
    private InfoStudentEnrollmentContext buildResult(
            IStudentCurricularPlan studentCurricularPlan, List enrollments,
            List courses, IExecutionYear executionYear) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = Cloner
                .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);

        List infoEnrollments = new ArrayList();
        if (enrollments != null && enrollments.size() > 0) {
            infoEnrollments = (List) CollectionUtils.collect(enrollments,
                    new Transformer() {
                        public Object transform(Object input) {
                            IEnrollment enrolment = (IEnrollment) input;
                            return Cloner
                                    .copyIEnrolment2InfoEnrolment(enrolment);
                        }
                    });
            Collections.sort(infoEnrollments, new BeanComparator(
                    ("infoCurricularCourse.name")));
        }

        List infoCurricularCourses = new ArrayList();
        if (courses != null && courses.size() > 0) {
            infoCurricularCourses = (List) CollectionUtils.collect(courses,
                    new Transformer() {
                        public Object transform(Object input) {
                            ICurricularCourse curricularCourse = (ICurricularCourse) input;
                            return Cloner
                                    .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                        }
                    });
            Collections.sort(infoCurricularCourses,
                    new BeanComparator(("name")));
        }

        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) Cloner
                .get(executionYear);
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        infoStudentEnrolmentContext
                .setInfoStudentCurricularPlan(infoStudentCurricularPlan);
        infoStudentEnrolmentContext
                .setStudentInfoEnrollmentsWithStateEnrolled(infoEnrollments);
        infoStudentEnrolmentContext
                .setFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(infoCurricularCourses);
        infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoStudentEnrolmentContext;
    }
}