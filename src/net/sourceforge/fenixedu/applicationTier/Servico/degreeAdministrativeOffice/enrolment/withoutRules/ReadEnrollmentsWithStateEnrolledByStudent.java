/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndDegree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadEnrollmentsWithStateEnrolledByStudent extends Service {

    public Object run(InfoStudent infoStudent, DegreeType degreeType, Integer executionPeriodID)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;

        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, executionPeriodID);

        StudentCurricularPlan studentCurricularPlan = null;
        if (infoStudent != null && infoStudent.getNumber() != null) {
            studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(), degreeType);
        }
        if (studentCurricularPlan == null) {
            throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
        }

        if (isStudentCurricularPlanFromChosenExecutionYear(studentCurricularPlan, executionPeriod
                .getExecutionYear().getYear())) {
            List enrolmentsInExecutionPeriod = studentCurricularPlan
                    .getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod);

            infoStudentEnrolmentContext = buildResult(studentCurricularPlan,
                    enrolmentsInExecutionPeriod, executionPeriod);

            if (infoStudentEnrolmentContext == null) {
                throw new FenixServiceException();
            }
        } else {
            throw new FenixServiceException("error.student.curriculum.not.from.chosen.execution.year");
        }

        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentCurricularPlan
     * @param enrollments
     * @param executionPeriod
     * @return
     */
    private InfoStudentEnrollmentContext buildResult(StudentCurricularPlan studentCurricularPlan,
            List enrollments, ExecutionPeriod executionPeriod) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlanWithInfoStudentAndDegree
                .newInfoFromDomain(studentCurricularPlan);

        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                .newInfoFromDomain(executionPeriod);

        List infoEnrollments = new ArrayList();
        if (enrollments != null && enrollments.size() > 0) {
            infoEnrollments = (List) CollectionUtils.collect(enrollments, new Transformer() {
                public Object transform(Object input) {
                    Enrolment enrolment = (Enrolment) input;
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
            StudentCurricularPlan studentCurricularPlan, String year) throws ExcepcaoPersistencia {
        IPersistentExecutionDegree executionDegreeDAO = persistentSupport.getIPersistentExecutionDegree();
        IPersistentExecutionYear executionYearDAO = persistentSupport.getIPersistentExecutionYear();

        ExecutionYear executionYear = executionYearDAO.readExecutionYearByName(year);
        if (executionYear != null) {
            ExecutionDegree executionDegree = executionDegreeDAO
                    .readByDegreeCurricularPlanAndExecutionYear(studentCurricularPlan
                            .getDegreeCurricularPlan().getName(), studentCurricularPlan
                            .getDegreeCurricularPlan().getDegree().getSigla(), executionYear.getYear());

            return executionDegree != null;
        }
        return false;

    }
}