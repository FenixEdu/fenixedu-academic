package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrollmentState;
import Util.MarkType;

/**
 * @author Fernanda Quitério
 * 10/07/2003
 *
 */
public class ConfirmStudentsFinalEvaluation implements IServico
{
    private static ConfirmStudentsFinalEvaluation _servico = new ConfirmStudentsFinalEvaluation();

    /**
    	* The actor of this class.
    	**/
    private ConfirmStudentsFinalEvaluation()
    {

    }

    /**
     * Returns Service Name
     */
    public String getNome()
    {
        return "ConfirmStudentsFinalEvaluation";
    }

    /**
     * Returns the _servico.
     * @return ReadExecutionCourse
     */
    public static ConfirmStudentsFinalEvaluation getService()
    {
        return _servico;
    }

    public Boolean run(Integer curricularCourseCode, String yearString, IUserView userView)
        throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation =
                sp.getIPersistentEnrolmentEvaluation();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

            //			employee
            IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = readEmployee(person);

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, curricularCourseCode, false);

            List enrolments = null;
            if (yearString != null)
            {
            	enrolments =
            	persistentEnrolment.readByCurricularCourseAndYear(curricularCourse, yearString);
            }
            else
            {
            	enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse);
            }
            List enrolmentEvaluations = new ArrayList();
            Iterator iterEnrolment = enrolments.listIterator();
            while (iterEnrolment.hasNext())
            {
                IEnrollment enrolment = (IEnrollment) iterEnrolment.next();
                List allEnrolmentEvaluations =
                    persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolment(enrolment);
                IEnrolmentEvaluation enrolmentEvaluation =
                    (IEnrolmentEvaluation) allEnrolmentEvaluations.get(
                        allEnrolmentEvaluations.size() - 1);
                enrolmentEvaluations.add(enrolmentEvaluation);
            }

            if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0)
            {
                ListIterator iterEnrolmentEvaluations = enrolmentEvaluations.listIterator();
                while (iterEnrolmentEvaluations.hasNext())
                {
                    IEnrolmentEvaluation enrolmentEvaluationElem =
                        (IEnrolmentEvaluation) iterEnrolmentEvaluations.next();
                    if (enrolmentEvaluationElem.getGrade() != null
                        && enrolmentEvaluationElem.getGrade().length() > 0
                        && enrolmentEvaluationElem.getEnrolmentEvaluationState().equals(
                            EnrolmentEvaluationState.TEMPORARY_OBJ))
                    {

                        updateEnrolmentEvaluation(
                            persistentEnrolmentEvaluation,
                            persistentEnrolment,
                            employee,
                            enrolmentEvaluationElem);
                    }
                }
            }
        } catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return Boolean.TRUE;
    }

    private void updateEnrolmentEvaluation(
        IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation,
        IPersistentEnrollment persistentEnrolment,
        IEmployee employee,
        IEnrolmentEvaluation enrolmentEvaluationElem)
        throws ExcepcaoPersistencia
    {
        persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluationElem);

        enrolmentEvaluationElem.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
        Calendar calendar = Calendar.getInstance();
        enrolmentEvaluationElem.setWhen(calendar.getTime());
        enrolmentEvaluationElem.setEmployee(employee);
        enrolmentEvaluationElem.setObservation("Lançamento de Notas na Secretaria");
        //TODO: checksum
        enrolmentEvaluationElem.setCheckSum("");

        // update state of enrolment: aproved, notAproved or notEvaluated
        updateEnrolmentState(persistentEnrolment, enrolmentEvaluationElem);
    }

    private void updateEnrolmentState(
        IPersistentEnrollment persistentEnrolment,
        IEnrolmentEvaluation enrolmentEvaluationElem)
        throws ExcepcaoPersistencia
    {
        IEnrollment enrolmentToEdit = enrolmentEvaluationElem.getEnrolment();
        persistentEnrolment.simpleLockWrite(enrolmentToEdit);

        EnrollmentState newEnrolmentState = EnrollmentState.APROVED;

        if (MarkType.getRepMarks().contains(enrolmentEvaluationElem.getGrade()))
        {
            newEnrolmentState = EnrollmentState.NOT_APROVED;
        } else if (MarkType.getNaMarks().contains(enrolmentEvaluationElem.getGrade()))
        {
            newEnrolmentState = EnrollmentState.NOT_EVALUATED;
        }
        enrolmentToEdit.setEnrollmentState(newEnrolmentState);
    }

    private IEmployee readEmployee(IPessoa person)
    {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try
        {
            persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
            employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
        }
        return employee;
    }
}