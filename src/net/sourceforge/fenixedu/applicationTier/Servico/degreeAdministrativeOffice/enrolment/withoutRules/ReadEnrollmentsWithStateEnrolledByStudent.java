/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudent;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadEnrollmentsWithStateEnrolledByStudent implements IService {

    public ReadEnrollmentsWithStateEnrolledByStudent() {
    }

    public Object run(InfoStudent infoStudent, TipoCurso degreeType, Integer executionPeriodID)
            throws FenixServiceException {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(ExecutionPeriod.class, executionPeriodID);

            IStudentCurricularPlan studentCurricularPlan = null;
            if (infoStudent != null && infoStudent.getNumber() != null) {
                studentCurricularPlan = persistentStudentCurricularPlan
                        .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(), degreeType);
            }
            if (studentCurricularPlan == null) {
                throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
            }

            if (isStudentCurricularPlanFromChosenExecutionYear(studentCurricularPlan, executionPeriod.getExecutionYear().getYear())) {
                IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
                List enrollments = persistentEnrolment
                        .readEnrolmentsByStudentCurricularPlanAndEnrolmentState(studentCurricularPlan,
                                EnrollmentState.ENROLLED);

                infoStudentEnrolmentContext = buildResult(studentCurricularPlan, enrollments, executionPeriod);

                if (infoStudentEnrolmentContext == null) {
                    throw new FenixServiceException();
                }
            } else {
                throw new FenixServiceException(
                        "error.student.curriculum.not.from.chosen.execution.year");
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }

        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentCurricularPlan
     * @param enrollments
     * @param executionPeriod
     * @return
     */
    private InfoStudentEnrollmentContext buildResult(IStudentCurricularPlan studentCurricularPlan,
            List enrollments, IExecutionPeriod executionPeriod) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlanWithInfoStudent
                .newInfoFromDomain(studentCurricularPlan);
        
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod);

        List infoEnrollments = new ArrayList();
        if (enrollments != null && enrollments.size() > 0) {
            infoEnrollments = (List) CollectionUtils.collect(enrollments, new Transformer() {
                public Object transform(Object input) {
                    IEnrollment enrolment = (IEnrollment) input;
                    return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                            .newInfoFromDomain(enrolment);
                }
            });
            Collections.sort(infoEnrollments, new BeanComparator(("infoCurricularCourse.name")));
        }

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
        infoStudentEnrolmentContext.setStudentInfoEnrollmentsWithStateEnrolled(infoEnrollments);
        infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentCurricularPlan
     * @param year
     * @return true/false
     * @throws ExcepcaoPersistencia
     */
    private boolean isStudentCurricularPlanFromChosenExecutionYear(
            IStudentCurricularPlan studentCurricularPlan, String year) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();
        IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

        IExecutionYear executionYear = executionYearDAO.readExecutionYearByName(year);
        if (executionYear != null) {
            IExecutionDegree executionDegree = executionDegreeDAO
                    .readByDegreeCurricularPlanAndExecutionYear(studentCurricularPlan
                            .getDegreeCurricularPlan(), executionYear);

            return executionDegree != null;
        }
        return false;

    }
}