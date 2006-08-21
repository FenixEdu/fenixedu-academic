/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.EnrollmentPredicates;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.InfoStudentCurricularPlansWithSelectedEnrollments;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EnrollmentStateSelectionType;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Andr� Fernandes / Jo�o Brito
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ReadStudentCurricularPlansByPersonAndCriteria extends Service {

    /*
     * devolve InfoXxxx com: - StudentCurricularPlan's do aluno - Enrolments do
     * aluno (filtrados por um criterio)
     */
    public InfoStudentCurricularPlansWithSelectedEnrollments run(String username,
            StudentCurricularPlanIDDomainType curricularPlanID, EnrollmentStateSelectionType criterio)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {
        // curricularPlanID pode ser: ID do CP do aluno, 'all ou 'newest
        // criterio define que IEnrolments vamos ver: pode ser 'aprovado ou null
        // (selecciona todos)

        List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();

        Predicate predicado = null;

        if (criterio.equals(EnrollmentStateSelectionType.APPROVED)) {
            predicado = EnrollmentPredicates.getApprovedPredicate();
        } else if (criterio.equals(EnrollmentStateSelectionType.NONE)) {
            predicado = EnrollmentPredicates.getNonePredicate(); // nenhum
        } else {
            predicado = EnrollmentPredicates.getAllPredicate(); // todos
        }

        if (curricularPlanID.isAll() || curricularPlanID.isNewest()) {
            Person person = Person.readPersonByUsername(username);
            List students = person.getStudents();
            List<StudentCurricularPlan> studentCPsTemp = null;

            Iterator studentsIterator = students.iterator();

            // para cada Registration que esta Person �
            // juntar todos os SCP
            while (studentsIterator.hasNext()) {
                Registration registration = (Registration) studentsIterator.next();

                // seleccionar todos os planos do aluno
                studentCPsTemp = registration.getStudentCurricularPlans();

                studentCurricularPlans.addAll(studentCPsTemp);
            }

            if (curricularPlanID.isNewest()) {
                // seleccionar o mais recente

                StudentCurricularPlan planoRecente = null;
                StudentCurricularPlan planoTemp = null;
                Iterator iterator = studentCurricularPlans.iterator();

                while (iterator.hasNext()) {
                    planoTemp = (StudentCurricularPlan) iterator.next();

                    if (planoRecente == null
                            || planoRecente.getStartDate().before(planoTemp.getStartDate())) {
                        planoRecente = planoTemp;
                    }
                }

                studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
                studentCurricularPlans.add(planoRecente);
            }
        } else // um SCP em particular
        {
            // obter o CP especificado como curricularPlanID
            studentCurricularPlans.add(rootDomainObject.readStudentCurricularPlanByOID(curricularPlanID.getId()));
        }

        InfoStudentCurricularPlansWithSelectedEnrollments currPlanEnrol = new InfoStudentCurricularPlansWithSelectedEnrollments();

        Iterator iteratorInfo = studentCurricularPlans.iterator();
        StudentCurricularPlan studentCurricularPlan = null;

        while (iteratorInfo.hasNext()) {
            // criacao da info a retornar a partir dos objectos de dominio
            // pretendidos

            studentCurricularPlan = (StudentCurricularPlan) iteratorInfo.next();

            List enrollments = studentCurricularPlan.getEnrolments();// lista
                                                                        // de
                                                                        // IEnrollment's
            List selectedEnrollments = ((List) CollectionUtils.select(enrollments, predicado));

            List<InfoEnrolment> infoSelectedEnrollments = new ArrayList<InfoEnrolment>();
            Iterator selectedEnrollmentsIterator = selectedEnrollments.iterator();

            GetEnrolmentGrade getEnrolmentGrade = new GetEnrolmentGrade();

            while (selectedEnrollmentsIterator.hasNext()) {
                Enrolment enrollment = (Enrolment) selectedEnrollmentsIterator.next();
                InfoEnrolment infoEnrollment = InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
                        .newInfoFromDomain(enrollment);

                InfoEnrolmentEvaluation infoEnrolmentEvaluation = getEnrolmentGrade.run(enrollment);
                infoEnrollment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

                infoSelectedEnrollments.add(infoEnrollment);
            }

            currPlanEnrol.addInfoStudentCurricularPlan(
                    InfoStudentCurricularPlan
                            .newInfoFromDomain(studentCurricularPlan), infoSelectedEnrollments);
        }

        return currPlanEnrol;
    }

}
