/*
 * Created on 12/Fev/2004
 */
package ServidorAplicacao.Servico.enrolment.shift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.enrollment.shift.ShiftEnrollmentErrorReport;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ShiftStudent;
import Dominio.Student;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 */
public class EnrollStudentInShifts implements IService
{
    
    public class StudentNotFoundServiceException extends FenixServiceException
    {

    }

    /**
     * 
     */
    public EnrollStudentInShifts()
    {
    }

    /**
     * @param studentId
     * @param shiftIdsToEnroll
     * @return @throws
     *         StudentNotFoundServiceException
     * @throws FenixServiceException
     */
    public ShiftEnrollmentErrorReport run(Integer studentId,
            final List shiftIdsToEnroll)
            throws StudentNotFoundServiceException, FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            ITurnoAlunoPersistente persistentShiftStudent = sp
                    .getITurnoAlunoPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp
                    .getIPersistentExecutionPeriod();
            ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

            if (shiftIdsToEnroll != null && !shiftIdsToEnroll.isEmpty())
            {
                IStudent student = (IStudent) persistentStudent.readByOId(
                        new Student(studentId), false);
                if (student == null) { throw new StudentNotFoundServiceException(); }

                IExecutionPeriod executionPeriod = persistentExecutionPeriod
                        .readActualExecutionPeriod();
                List shiftEnrollments = persistentShiftStudent
                        .readByStudentAndExecutionPeriod(student,
                                executionPeriod);

                List shiftsToUnEnroll = new ArrayList();
                List filteredShiftsIdsToEnroll = new ArrayList();
                if (shiftEnrollments == null || shiftEnrollments.isEmpty())
                {
                    filteredShiftsIdsToEnroll = shiftIdsToEnroll;
                }
                else
                {
                    calculateShiftsToUnEnroll(shiftIdsToEnroll,
                            shiftEnrollments, shiftsToUnEnroll);
                    calculateShiftsToEnroll(shiftIdsToEnroll, shiftEnrollments,
                            filteredShiftsIdsToEnroll);
                }
                enrollStudentsInShifts(student, filteredShiftsIdsToEnroll,
                        errorReport, persistentShift, persistentShiftStudent);
                unEnrollStudentsInShifts(shiftsToUnEnroll, errorReport,
                        persistentShiftStudent);

            }

            return errorReport;
        }
        catch (ExcepcaoPersistencia e)
        {
        	e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param shiftsToUnEnroll
     * @param errors
     * @param persistentShiftStudent
     */
    private void unEnrollStudentsInShifts(List shiftsToUnEnroll,
            ShiftEnrollmentErrorReport errorReport,
            ITurnoAlunoPersistente persistentShiftStudent)
            throws ExcepcaoPersistencia
    {
        Iterator iter = shiftsToUnEnroll.iterator();
        while (iter.hasNext())
        {
            ITurnoAluno shiftStudent = (ITurnoAluno) iter.next();
            persistentShiftStudent.delete(shiftStudent);
        }

    }

    /**
     * @param student
     * @param filteredShiftsIdsToEnroll
     * @param errors
     * @param persistentShift
     * @param persistentShiftStudent
     * @throws ExcepcaoPersistencia
     */
    private void enrollStudentsInShifts(IStudent student,
            List filteredShiftsIdsToEnroll,
            ShiftEnrollmentErrorReport errorReport,
            ITurnoPersistente persistentShift,
            ITurnoAlunoPersistente persistentShiftStudent)
            throws ExcepcaoPersistencia
    {
        Iterator iter = filteredShiftsIdsToEnroll.iterator();
        while (iter.hasNext())
        {
            Integer shiftId = (Integer) iter.next();
            ITurno shift = (ITurno) persistentShift.readByOId(
                    new Turno(shiftId), false);
            if (shift == null)
            {
                errorReport.getUnExistingShifts().add(shiftId);
            }
            else
            {
                if (shift.getLotacao().intValue() > persistentShiftStudent
                        .readNumberOfStudentsByShift(shift))
                {
                    ITurnoAluno shiftStudentToWrite = new ShiftStudent();
                    persistentShiftStudent.simpleLockWrite(shiftStudentToWrite);
                    shiftStudentToWrite.setShift(shift);
                    shiftStudentToWrite.setStudent(student);
                }
                else
                {
                    errorReport.getUnAvailableShifts().add(Cloner.get(shift));
                }

            }

        }
    }

    /**
     * @param shiftIdsToEnroll
     * @param shiftEnrollments
     * @param filteredShiftsIdsToEnroll
     */
    private void calculateShiftsToEnroll(List shiftIdsToEnroll,
            final List shiftEnrollments, List filteredShiftsIdsToEnroll)
    {
        filteredShiftsIdsToEnroll = (List) CollectionUtils.select(
                shiftIdsToEnroll, new Predicate()
                {

                    public boolean evaluate(Object arg0)
                    {
                        final Integer shiftId = (Integer) arg0;

                        if (CollectionUtils.exists(shiftEnrollments,
                                new Predicate()
                                {

                                    public boolean evaluate(Object arg0)
                                    {
                                        ITurnoAluno shiftStudent = (ITurnoAluno) arg0;
                                        return shiftStudent.getShift()
                                                .getIdInternal()
                                                .equals(shiftId);
                                    }
                                }))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }

                    }
                });

    }

    /**
     * @param shiftIdsToEnroll
     * @param shiftEnrollments
     */
    private void calculateShiftsToUnEnroll(final List shiftIdsToEnroll,
            List shiftEnrollments, List shiftsToUnEnroll)
    {

        shiftsToUnEnroll = (List) CollectionUtils.select(shiftEnrollments,
                new Predicate()
                {

                    public boolean evaluate(Object arg0)
                    {
                        ITurnoAluno shiftStudent = (ITurnoAluno) arg0;
                        final Integer idToCompare = shiftStudent.getShift()
                                .getIdInternal();
                        if (CollectionUtils.exists(shiftIdsToEnroll,
                                new Predicate()
                                {

                                    public boolean evaluate(Object arg0)
                                    {
                                        return idToCompare.equals(arg0);
                                    }
                                }))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }

                    }
                });

    }

}
