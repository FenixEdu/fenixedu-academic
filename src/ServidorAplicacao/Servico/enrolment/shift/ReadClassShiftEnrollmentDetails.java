/*
 * Created on 11/Fev/2004
 */
package ServidorAplicacao.Servico.enrolment.shift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import DataBeans.enrollment.shift.ExecutionCourseShiftEnrollmentDetails;
import DataBeans.enrollment.shift.InfoClassEnrollmentDetails;
import DataBeans.enrollment.shift.ShiftEnrollmentDetails;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @throws StudentNotFoundServiceException
 * @author jpvl
 */
public class ReadClassShiftEnrollmentDetails implements IService
{

    /**
     * @author jpvl
     */
    public class StudentNotFoundServiceException extends FenixServiceException
    {

    }
    /**
     *  
     */
    public ReadClassShiftEnrollmentDetails()
    {
        super();
    }

    public InfoClassEnrollmentDetails run(InfoStudent infoStudent) throws FenixServiceException
    {
        InfoClassEnrollmentDetails enrollmentDetails = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentStudent studentDAO = sp.getIPersistentStudent();
            ITurmaPersistente classDAO = sp.getITurmaPersistente();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            ITurnoAlunoPersistente shiftStudentDAO = sp.getITurnoAlunoPersistente();

            IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            IStudent student = readStudent(infoStudent, studentDAO);
            List classList =
                classDAO.readClassesThatContainsStudentAttendsOnExecutionPeriod(
                    student,
                    executionPeriod);

            List studentShifts =
                shiftStudentDAO.readByStudentAndExecutionPeriod(student, executionPeriod);
            List shifts = collectShifts(studentShifts);

            List infoClassList = new ArrayList();

            Map classExecutionCourseShiftEnrollmentDetailsMap =
                createMapAndPopulateInfoClassList(shiftStudentDAO, classList, shifts, infoClassList);

            collectInfoShifts(shifts);

            InfoStudent infoStudent2 = Cloner.copyIStudent2InfoStudent(student);
            enrollmentDetails.setInfoStudent(infoStudent2);
            enrollmentDetails.setInfoShiftEnrolledList(studentShifts);
            enrollmentDetails.setInfoClassList(infoClassList);
            enrollmentDetails.setClassExecutionCourseShiftEnrollmentDetailsMap(
                classExecutionCourseShiftEnrollmentDetailsMap);
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

        return enrollmentDetails;
    }

    /**
     * @param shifts
     */
    private void collectInfoShifts(List shifts)
    {
        /* Prepare return */
        List infoShifts = (List) CollectionUtils.collect(shifts, new Transformer()
        {

            public Object transform(Object input)
            {
                ITurno shift = (ITurno) input;
                InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                return infoShift;
            }
        });
    }

    /**
     * @param studentShifts
     * @return
     */
    private List collectShifts(List studentShifts)
    {
        List shifts = (List) CollectionUtils.collect(studentShifts, new Transformer()
        {

            public Object transform(Object input)
            {
                ITurnoAluno shiftStudent = (ITurnoAluno) input;
                return shiftStudent.getShift();
            }
        });
        return shifts;
    }

    /**
     * @param shiftStudentDAO
     * @param classList
     * @param shifts
     * @param infoClassList
     * @return
     */
    private Map createMapAndPopulateInfoClassList(
        ITurnoAlunoPersistente shiftStudentDAO,
        List classList,
        List shifts,
        List infoClassList)
    {
        Map classExecutionCourseShiftEnrollmentDetailsMap = new HashMap();
        /* shift id -> ShiftEnrollmentDetails */
        Map shiftsTreated = new HashMap();

        /* executionCourse id -> ExecutionCourseShiftEnrollmentDetails */
        Map executionCourseTreated = new HashMap();

        for (int i = 0; i < classList.size(); i++)
        {
            ITurma klass = (ITurma) classList.get(i);
            InfoClass infoClass = Cloner.copyClass2InfoClass(klass);
            infoClassList.add(infoClass);

            Integer klassId = klass.getIdInternal();

//            List associatedShifts = klass.getAssociatedShifts();

            for (int j = 0; j < shifts.size(); j++)
            {
                ITurno shift = (ITurno) shifts.get(i);

                ShiftEnrollmentDetails shiftEnrollmentDetails =
                    createShiftEnrollmentDetails(shiftStudentDAO, shiftsTreated, shift);

                ExecutionCourseShiftEnrollmentDetails executionCourseShiftEnrollmentDetails =
                    createExecutionCourseShiftEnrollmentDetails(executionCourseTreated, shift);

                executionCourseShiftEnrollmentDetails.addShiftEnrollmentDetails(shiftEnrollmentDetails);

                List executionCourseShiftEnrollmentDetailsList =
                    (List) classExecutionCourseShiftEnrollmentDetailsMap.get(klassId);
                if (executionCourseShiftEnrollmentDetailsList == null)
                {
                    executionCourseShiftEnrollmentDetailsList = new ArrayList();
                    classExecutionCourseShiftEnrollmentDetailsMap.put(
                        klassId,
                        executionCourseShiftEnrollmentDetailsList);
                }
                executionCourseShiftEnrollmentDetailsList.add(executionCourseShiftEnrollmentDetails);
            }
        }
        return classExecutionCourseShiftEnrollmentDetailsMap;
    }

    /**
     * @param executionCourseTreated
     * @param shift
     * @return
     */
    private ExecutionCourseShiftEnrollmentDetails createExecutionCourseShiftEnrollmentDetails(
        Map executionCourseTreated,
        ITurno shift)
    {
        IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
        ExecutionCourseShiftEnrollmentDetails executionCourseShiftEnrollmentDetails =
            (ExecutionCourseShiftEnrollmentDetails) executionCourseTreated.get(
                executionCourse.getIdInternal());

        if (executionCourseShiftEnrollmentDetails == null)
        {
            executionCourseShiftEnrollmentDetails = new ExecutionCourseShiftEnrollmentDetails();
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            executionCourseShiftEnrollmentDetails.setInfoExecutionCourse(infoExecutionCourse);
        }
        return executionCourseShiftEnrollmentDetails;
    }

    /**
     * @param shiftStudentDAO
     * @param shiftsTreated
     * @param shift
     * @return
     */
    private ShiftEnrollmentDetails createShiftEnrollmentDetails(
        ITurnoAlunoPersistente shiftStudentDAO,
        Map shiftsTreated,
        ITurno shift)
    {
        ShiftEnrollmentDetails shiftEnrollmentDetails =
            (ShiftEnrollmentDetails) shiftsTreated.get(shift.getIdInternal());

        if (shiftEnrollmentDetails == null)
        {
            InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
            shiftEnrollmentDetails = new ShiftEnrollmentDetails();
            int occupation = shiftStudentDAO.readNumberOfStudentsByShift(shift);
            shiftEnrollmentDetails.setInfoShift(infoShift);
            shiftEnrollmentDetails.setVacancies(new Integer(shift.getLotacao().intValue() - occupation));
            shiftsTreated.put(shift.getIdInternal(), shiftEnrollmentDetails);
        }
        return shiftEnrollmentDetails;
    }

    /**
     * @param associatedShifts
     * @return
     */
    private List collectExecutionCourses(List shifts)
    {
        List executionCoursesCollected = new ArrayList();
        for (int i = 0; i < shifts.size(); i++)
        {
            ITurno shift = (ITurno) shifts.get(i);
            IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
            if (!executionCoursesCollected.contains(executionCourse))
            {
                executionCoursesCollected.add(executionCourse);
            }
        }
        return executionCoursesCollected;
    }

    /**
     * @param infoStudent
     * @param studentDAO
     * @return @throws
     *         StudentNotFoundServiceException
     */
    private IStudent readStudent(InfoStudent infoStudent, IPersistentStudent studentDAO)
        throws StudentNotFoundServiceException
    {
        IStudent student =
            (IStudent) studentDAO.readByOId(new Student(infoStudent.getIdInternal()), false);
        if (student == null)
        {
            throw new StudentNotFoundServiceException();
        }
        return student;
    }
}
