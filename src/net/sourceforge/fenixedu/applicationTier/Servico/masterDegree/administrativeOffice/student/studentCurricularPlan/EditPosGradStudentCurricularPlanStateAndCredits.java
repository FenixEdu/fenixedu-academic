package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteEnrollment;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota 15/Out/2003
 */

public class EditPosGradStudentCurricularPlanStateAndCredits implements IService {

    public void run(IUserView userView, Integer studentCurricularPlanId, String currentState,
                    Double credits, String startDate, List extraCurricularCourses, String observations,
                    Integer branchId, String specialization) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId, true);
            if (studentCurricularPlan == null) {
                throw new InvalidArgumentsServiceException();
            }

            StudentCurricularPlanState newState = StudentCurricularPlanState.valueOf(currentState);

            IEmployee employee = null;
            IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
            IPersistentBranch persistentBranch = sp.getIPersistentBranch();

            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            if (person == null) {
                throw new InvalidArgumentsServiceException();
            }

            employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
            if (employee == null) {
                throw new InvalidArgumentsServiceException();
            }

            IBranch branch = (IBranch) persistentBranch.readByOID(Branch.class, branchId);
            if (branch == null) {
                throw new InvalidArgumentsServiceException();
            }

            studentCurricularPlan.setStartDate(stringDateToCalendar(startDate).getTime());
            studentCurricularPlan.setCurrentState(newState);
            studentCurricularPlan.setEmployee(employee);
            studentCurricularPlan.setGivenCredits(credits);
            studentCurricularPlan.setObservations(observations);
            studentCurricularPlan.setBranch(branch);
            studentCurricularPlan.setSpecialization(Specialization.valueOf(specialization));

            List enrollments = studentCurricularPlan.getEnrolments();
            Iterator iterator = enrollments.iterator();
            if (newState.equals(StudentCurricularPlanState.INACTIVE)) {
                while (iterator.hasNext()) {
                    IEnrolment enrolment = (IEnrolment) iterator.next();
                    if (enrolment.getEnrollmentState() == EnrollmentState.ENROLLED
                        || enrolment.getEnrollmentState() == EnrollmentState.TEMPORARILY_ENROLLED) {
                        persistentEnrolment.simpleLockWrite(enrolment);
                        enrolment.setEnrollmentState(EnrollmentState.ANNULED);
                    }
                }
            } else {

                while (iterator.hasNext()) {
                    IEnrolment enrolment = (IEnrolment) iterator.next();

                    if (extraCurricularCourses.contains(enrolment.getIdInternal())) {
                        if (!(enrolment instanceof EnrolmentInExtraCurricularCourse)) {


                            IEnrolment auxEnrolment = new EnrolmentInExtraCurricularCourse();
                            persistentEnrolment.simpleLockWrite(auxEnrolment);

                            copyEnrollment(enrolment, auxEnrolment);
                            setEnrolmentCreationInformation(userView, auxEnrolment);
                            
                            changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment,
                                                              auxEnrolment);
							
                            DeleteEnrollment deleteEnrolmentService = new DeleteEnrollment();
                            deleteEnrolmentService.run(enrolment.getIdInternal());
                        } else {
                            changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, enrolment);
                        }
                    } else {
                        if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
                            
                            IEnrolment auxEnrolment = new Enrolment();
                            persistentEnrolment.simpleLockWrite(auxEnrolment);

                            copyEnrollment(enrolment, auxEnrolment);
                            setEnrolmentCreationInformation(userView, auxEnrolment);
                            
                            changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment,
                                                              auxEnrolment);
							
                            DeleteEnrollment deleteEnrolmentService = new DeleteEnrollment();
                            deleteEnrolmentService.run(enrolment.getIdInternal());
                        } else {
                            changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, enrolment);
                        }
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

    private void setEnrolmentCreationInformation(IUserView userView, IEnrolment auxEnrolment) {
        auxEnrolment.setCreationDate(Calendar.getInstance().getTime());
        auxEnrolment.setCreatedBy(userView.getUtilizador());
    }

    /**
     * @param enrolment
     * @param auxEnrolment
     * @throws FenixServiceException
     */
    private void copyEnrollment(IEnrolment enrolment, IEnrolment auxEnrolment)
        throws FenixServiceException {

        auxEnrolment.setCreatedBy(enrolment.getCreatedBy());
        auxEnrolment.setCreationDate(enrolment.getCreationDate());
        auxEnrolment.setAccumulatedWeight(enrolment.getAccumulatedWeight());
        auxEnrolment.setCurricularCourse(enrolment.getCurricularCourse());
        auxEnrolment.setExecutionPeriod(enrolment.getExecutionPeriod());
        auxEnrolment.setStudentCurricularPlan(enrolment.getStudentCurricularPlan());     
        auxEnrolment.setCondition(enrolment.getCondition());
        auxEnrolment.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
        auxEnrolment.setEnrollmentState(enrolment.getEnrollmentState());
		
        auxEnrolment.getEvaluations().addAll(enrolment.getEvaluations());
        auxEnrolment.getEnrolmentEquivalences().addAll(enrolment.getEnrolmentEquivalences());
        auxEnrolment.getAttends().addAll(enrolment.getAttends());
        auxEnrolment.getEquivalentEnrolmentForEnrolmentEquivalences().addAll(enrolment.getEquivalentEnrolmentForEnrolmentEquivalences());
        auxEnrolment.getCreditsInAnySecundaryAreas().addAll(enrolment.getCreditsInAnySecundaryAreas());
        auxEnrolment.getCreditsInScientificAreas().addAll(enrolment.getCreditsInScientificAreas());
    }

    /**
     * @param newState
     * @param persistentEnrolment
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    private void changeAnnulled2ActiveIfActivePlan(StudentCurricularPlanState newState,
                                                   IPersistentEnrollment persistentEnrolment, IEnrolment enrolment)
        throws ExcepcaoPersistencia {
        if (newState.equals(StudentCurricularPlanState.ACTIVE)) {
            if (enrolment.getEnrollmentState() == EnrollmentState.ANNULED) {
                persistentEnrolment.simpleLockWrite(enrolment);
                enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
            }
        }
    }

    private Calendar stringDateToCalendar(String startDate) throws NumberFormatException {
        Calendar calendar = Calendar.getInstance();
        String[] aux = startDate.split("/");
        calendar.set(Calendar.DAY_OF_MONTH, (new Integer(aux[0])).intValue());
        calendar.set(Calendar.MONTH, (new Integer(aux[1])).intValue() - 1);
        calendar.set(Calendar.YEAR, (new Integer(aux[2])).intValue());

        return calendar;
    }

}
