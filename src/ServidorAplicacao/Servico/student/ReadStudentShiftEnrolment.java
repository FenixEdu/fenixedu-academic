/**
 * Project Fénix 
 * 
 * Package ServidorAplicacao.Servico.sop
 * 
 * Created on 18/Dez/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoClass;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoShiftStudentEnrolment;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.Pessoa;
import Dominio.ShiftStudent;
import Dominio.Student;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.utils.ClassesComparatorByNumberOfCourses;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * Describe class <code>ReadStudentShiftEnrolment</code> here.
 *
 * @author tdi-dev
 *
 *
 * @version 1.0
 */
public class ReadStudentShiftEnrolment implements IServico
{
    /**
     * <code>_service</code> is the instance of the service
     *
     */
    private static ReadStudentShiftEnrolment _service = new ReadStudentShiftEnrolment();

    /**
     * Creates a new <code>ReadStudentShiftEnrolment</code>.
     *
     */
    private ReadStudentShiftEnrolment()
    {
    }

    /**
     * Describe <code>getService</code> method here.
     *
     * @return a <code>ReadStudentShiftEnrolment</code> value
     */
    public static ReadStudentShiftEnrolment getService()
    {
        return _service;
    }
    /* (non-Javadoc)
     * @see ServidorAplicacao.IServico#getNome()
     */
    public final String getNome()
    {
        return "ReadStudentShiftEnrolment";
    }

    /**
     * Works with SHIFT_STUDENT table.
     * Inserts and updates table.
     * 
     *
     * @param infoShiftStudentEnrolment an <code>InfoShiftStudentEnrolment</code> value
     * @return an <code>InfoShiftStudentEnrolment</code> value
     * @exception FenixServiceException if an error occurs
     * @exception ExcepcaoPersistencia if an error occurs
     */
    public InfoShiftStudentEnrolment run(InfoShiftStudentEnrolment infoShiftStudentEnrolment)
        throws FenixServiceException, ExcepcaoPersistencia
    {
        if (infoShiftStudentEnrolment == null)
        {
            throw new IllegalArgumentException("InfoShiftStudentEnrolment must be not null!");
        }

        IStudent student = new Student();
        IPessoa person = new Pessoa();
        person.setUsername(infoShiftStudentEnrolment.getInfoStudent().getInfoPerson().getUsername());
        student.setPerson(person);

        //Get the OJB Persistent Support singleton
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        //TODO: (tdi-dev) -> edgar.goncalves - ask if the null in the
        //ServiceManager call is important...

        List courses =
            (List) ReadDisciplinesByStudent.getService().run(
                infoShiftStudentEnrolment.getInfoStudent().getNumber(),
                infoShiftStudentEnrolment.getInfoStudent().getDegreeType());

        infoShiftStudentEnrolment.setEnrolledExecutionCourses(courses);
        //*********************************************************************
        //Get the classes to wich the student is entitled to be in, 
        //  in the current execution year:
        List studentAllowedClasses = getStudentsAllowedClasses(student, sp, courses);

        //Copy these Classes (ITurma objects) to the action
        ClassTransformer classTransformer = new ClassTransformer();

        List allowedClasses = (List) CollectionUtils.collect(studentAllowedClasses, classTransformer);

        // And the result is: shifts with vacancies, of the courses the student is enrolled with
        infoShiftStudentEnrolment.setAllowedClasses(allowedClasses);

        //************************************************
        //Code for filling the currentEnrolment list:

        //Open a OJB entrypoint for the ShiftStudent table
        ITurnoAlunoPersistente shiftStudentDAO = sp.getITurnoAlunoPersistente();

        //read all the shiftStudent for that student
        ITurnoAluno shiftStudentExample = new ShiftStudent();

        shiftStudentExample.setStudent(student);
        List infoShiftStudentEnrolmentTmp;
        infoShiftStudentEnrolmentTmp = shiftStudentDAO.readByCriteria(shiftStudentExample);

        //Copy the Shifts (ITurno objects) associated with the course in which 
        // the student is enrolled to InfoShifts
        ShiftStudentTransformer shiftStudentTransformer = new ShiftStudentTransformer();
        List infoShiftEnrolment =
            (List) CollectionUtils.collect(infoShiftStudentEnrolmentTmp, shiftStudentTransformer);

        // And the result is: shifts with vacancies, of the courses the student is enrolled with
        infoShiftStudentEnrolment.setCurrentEnrolment(infoShiftEnrolment);

        //************************************************
        //Code for filling the availlableShifts list
        ITurno shiftExample = new Turno();

        //Open a OJB entrypoint for the Shift table
        ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

        //read all the shifts associated with the course in which the student is enrolled
        IExecutionCourse executionCourseExample = new ExecutionCourse();
        List associatedStudents = new ArrayList();
        associatedStudents.add(student);
        executionCourseExample.setAttendingStudents(associatedStudents);
        shiftExample.setDisciplinaExecucao(executionCourseExample);

        //TODO: tdi-dev -> The executionPeriod isn't handled yet!
        //IExecutionPeriod executionPeriod = null;
        //do something to the execution period, get the current period...
        //executionCourse.setExecutionPeriod(executionPeriod);

        //And the result is: shifts (ITurno's) associated with the course in which the student is enrolled
        List currentEnrolment = shiftDAO.readByCriteria(shiftExample);

        //Copy the Shifts associated with the course in which the student is enrolled
        ShiftTransformer shiftTransformer = new ShiftTransformer();
        List infoShiftEnrolmentTmp = (List) CollectionUtils.collect(currentEnrolment, shiftTransformer);

        //Remove the full shifts and the shifts that aren't theo. or prat.  
        List availableShifts = new ArrayList();
        CollectionUtils.select(infoShiftEnrolmentTmp, new Predicate()
        {
            public boolean evaluate(Object arg0)
            {
                InfoShift shift = (InfoShift) arg0;
                return (
                    shift.getAvailabilityFinal().intValue() > 0
                        && (shift.getTipo().getTipo().intValue() == TipoAula.TEORICA
                            || shift.getTipo().getTipo().intValue() == TipoAula.TEORICO_PRATICA
                            || shift.getTipo().getTipo().intValue() == TipoAula.PRATICA));
            }
        }, availableShifts);

        // Remove the shifts in which the student is already enrolled
        List infoAvailableShifts =
            (List) CollectionUtils.subtract(
                availableShifts,
                infoShiftStudentEnrolment.getCurrentEnrolment());

        //Get the classes for each shift that is in  infoAvailableShifts,
        // using the list currentEnrollment to search in OJB
        List infoAvailableShiftsFiltered = new ArrayList();
        for (int i = 0; i < infoAvailableShifts.size(); i++)
        {
            InfoShift thisInfoShift = (InfoShift) infoAvailableShifts.get(i);

            //Get the appropriate OJB Shift from the currentEnrolment,
            // and get the classes associated to that shift
            List classList = getClassesAssociatedWithShift(thisInfoShift, currentEnrolment, sp);

            if (theClassIsAllowed(classList, infoShiftStudentEnrolment.getAllowedClasses()))
            {
                //the shift is OK to proceed:
                //Replace the arrayList element with an array
                //[InfoShift, ArrayListWithAssociatedClasses]  
                //	Object obj[] = new Object[2];
                //	obj[0] = (Object) thisInfoShift;
                //	obj[1] = (Object) classList;	
                InfoShiftWithAssociatedInfoClassesAndInfoLessons composedShift =
                    new InfoShiftWithAssociatedInfoClassesAndInfoLessons();

                composedShift.setInfoClasses(
                    (List) CollectionUtils.intersection(allowedClasses, classList));
                composedShift.setInfoShift(thisInfoShift);
                composedShift.setInfoLessons(
                    sp.getITurnoAulaPersistente().readByShift(
                        (ITurno) (sp
                            .getITurnoPersistente()
                            .readByOId(Cloner.copyInfoShift2IShift(thisInfoShift), false))));
                //Copy the Shifts associated with the course in which the student is enrolled
                composedShift.setInfoLessons(
                    (List) CollectionUtils.collect(
                        composedShift.getInfoLessons(),
                        new LessonTransformer()));

                infoAvailableShiftsFiltered.add(composedShift);
            }

        }

        //Shifts with vacancies of the courses the student is enrolled with 
        infoShiftStudentEnrolment.setAvailableShift(infoAvailableShiftsFiltered);

        //adds the student lessons to the structure for timetable display
        infoShiftStudentEnrolment.setLessons(getStudentLessons(infoShiftStudentEnrolment));
        infoShiftStudentEnrolment.setCurrentEnrolment(
            setLessons2InfoShifts(infoShiftStudentEnrolment.getCurrentEnrolment()));
        return infoShiftStudentEnrolment;
    }

    /**
    	 * @param list
    	 * @return
    	 */
    private List setLessons2InfoShifts(List list) throws ExcepcaoPersistencia
    {
        if (list == null)
        {
            return null;
        } else
        {
            List infoShiftsWithLessons = new ArrayList();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            ITurnoAulaPersistente persistentShiftLesson = sp.getITurnoAulaPersistente();
            Iterator iter = list.iterator();
            while (iter.hasNext())
            {
                InfoShift infoShift = (InfoShift) iter.next();
                ITurno shift = Cloner.copyInfoShift2IShift(infoShift);
                shift = (ITurno) persistentShift.readByOId(shift, false);
                List lessons = persistentShiftLesson.readByShift(shift);
                Iterator iter1 = lessons.iterator();
                List infoLessons = new ArrayList();
                while (iter1.hasNext())
                {
                    IAula lesson = (IAula) iter1.next();
                    InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
                    infoLessons.add(infoLesson);
                }
                infoShift.setInfoLessons(infoLessons);
                infoShiftsWithLessons.add(infoShift);
            }

            return infoShiftsWithLessons;
        }

    }

    /**
     * @return
     */
    private List getStudentLessons(InfoShiftStudentEnrolment infoShiftStudentEnrolment)
        throws ExcepcaoPersistencia
    {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        ITurnoAulaPersistente persistentShiftLesson = sp.getITurnoAulaPersistente();
        List currentEnrollment = infoShiftStudentEnrolment.getCurrentEnrolment();
        Iterator iter = currentEnrollment.iterator();
        List lessons = new ArrayList();
        while (iter.hasNext())
        {
            InfoShift infoShift = (InfoShift) iter.next();
            ITurno shift = Cloner.copyInfoShift2IShift(infoShift);
            shift = (ITurno) persistentShift.readByOId(shift, false);
            List shiftLessons = persistentShiftLesson.readByShift(shift);
            List infoLessons = new ArrayList();
            Iterator iter1 = shiftLessons.iterator();
            while (iter1.hasNext())
            {
                IAula lesson = (IAula) iter1.next();
                infoLessons.add(Cloner.copyILesson2InfoLesson(lesson));
            }
            lessons.addAll(infoLessons);
        }
        if (lessons.isEmpty())
        {
            lessons = null;
        }
        return lessons;
    }

    /**
     * getStudentsAllowedClasses
     * @param student
     * @param sp Persistent Suport
     * @param courses a <code>List</code> with the attending courses
     * @return list of classes (ITurma) with the allowed classes for the student
     * @exception ExcepcaoPersistencia
     */
    private List getStudentsAllowedClasses(IStudent student, ISuportePersistente sp, List courses)
        throws ExcepcaoPersistencia
    {

        ITurno shiftExample = new Turno();
        IExecutionCourse executionCourseExample = new ExecutionCourse();

        List associatedStudents = new ArrayList();
        associatedStudents.add(student);
        executionCourseExample.setAttendingStudents(associatedStudents);
        shiftExample.setDisciplinaExecucao(executionCourseExample);

        //ITurma classExample = new Turma();

        ITurmaTurno classShiftExample = new TurmaTurno();
        //classShiftExample.setTurma(classExample);
        classShiftExample.setTurno(shiftExample);

        List classShifts = sp.getITurmaTurnoPersistente().readByCriteria(classShiftExample);

        classShifts = removeFullShiftsFromListOfClassShifts(classShifts);

        List executionCourses = new ArrayList();
        Iterator iter = courses.iterator();
        while (iter.hasNext())
        {
            executionCourses.add(
                Cloner.copyInfoExecutionCourse2ExecutionCourse((InfoExecutionCourse) iter.next()));
        }

        Comparator classesComparator = new ClassesComparatorByNumberOfCourses(executionCourses);
        List classes = getClassesFromShifts(classShifts);

        Collections.sort(classes, classesComparator);

        //		List classes =
        //			sortClassesByNumberOfCoursesSatisfied(classShifts, courses);

        return getDistinctClassesFromListOfClasses(classes);

    }

    /**
     * @param classShifts
     * @return
     */
    private List getClassesFromShifts(List classShifts)
    {
        if (classShifts == null)
        {
            return new ArrayList();
        } else
        {
            Iterator iter = classShifts.iterator();
            List classes = new ArrayList();
            while (iter.hasNext())
            {
                ITurmaTurno classShift = (ITurmaTurno) iter.next();
                classes.add(classShift.getTurma());
            }
            return classes;
        }

    }

    /**
     * @param classShifts
     * @return List with distinct classes (IClass)
     */
    private List getDistinctClassesFromListOfClasses(List classes)
    {
        Iterator i = classes.iterator();
        List result = new ArrayList();
        while (i.hasNext())
        {
            ITurma thisClass = (ITurma) i.next();
            Iterator resultIterator = result.iterator();
            boolean containsOneOfThese = false;
            while (resultIterator.hasNext())
            {
                ITurma resultClass = (ITurma) resultIterator.next();
                if (thisClass.equals(resultClass))
                {
                    containsOneOfThese = true;
                    break;
                }
            }
            if (!containsOneOfThese)
            {
                result.add(thisClass);
            }
        }
        return result;
    }

    /**
     * Creates a new <code>removeFullShiftsFromListOfClassShifts</code> instance.
     *
     * @param classShifts a <code>List</code> value
     */
    private List removeFullShiftsFromListOfClassShifts(List classShifts)
    {
        Iterator i = classShifts.iterator();
        while (i.hasNext())
        {
            ITurno shift = ((ITurmaTurno) i.next()).getTurno();
            if (shift.getAvailabilityFinal().intValue() < 1)
            {
                i.remove();
                continue;
            } // end of if ()

        }
        return classShifts;
    }

    /**
     * theClassIsAllowed returns true if the fist list (with
     * the classes of one shift) intersects the second list
     * (with the classes the student is allowed to be in).
     *  
     * @param list1 of classes
     * @param list2 of classes
     * @return true if list1 intersected with list2 is not null
     */
    private boolean theClassIsAllowed(List list1, List list2)
    {
        Iterator iterator = list1.iterator();
        while (iterator.hasNext())
        {
            InfoClass thisClass = (InfoClass) iterator.next();
            Iterator iterator2 = list2.iterator();
            while (iterator2.hasNext())
            {
                InfoClass element = (InfoClass) iterator2.next();
                if ((element.getNome().equals(thisClass.getNome()))
                    && (element.getInfoExecutionDegree().equals(thisClass.getInfoExecutionDegree())))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param thisInfoShift - Shift to be used in OJB usage
     * @param currentEnrolment - list of shifts (ITurno)
     * @return
     */
    private List getClassesAssociatedWithShift(
        InfoShift thisInfoShift,
        List currentEnrolment,
        ISuportePersistente sp)
    {
        ITurmaTurnoPersistente shiftClassDAO = sp.getITurmaTurnoPersistente();
        Iterator iter = currentEnrolment.iterator();
        ITurno shift = null;
        while (iter.hasNext())
        {
            shift = (ITurno) iter.next();
            if (shift.getIdInternal().equals(thisInfoShift.getIdInternal()))
            {
                break;
            }
            continue;
        }
        if (shift != null)
        {
            List classes;
            try
            {
                classes = shiftClassDAO.readClassesWithShift(shift);
            } catch (ExcepcaoPersistencia e)
            {
                e.printStackTrace();
                return null;
            }

            //Return a list of InfoClass objects
            ShiftClassTransformer shiftClassTransformer = new ShiftClassTransformer();
            return (List) CollectionUtils.collect(classes, shiftClassTransformer);
        }
        return null;
    }

    /**
     * @author tdi-dev
     *
     */
    private class ShiftClassTransformer implements Transformer
    {

        public Object transform(Object arg0)
        {
            ITurma thisClass = ((ITurmaTurno) arg0).getTurma();

            return Cloner.copyClass2InfoClass(thisClass);
        }
    }

    /**
     * ReadStudentShiftEnrolment.java
     *
     *
     * Created: Tue Jul 22 01:05:42 2003
     *
     * @author <a href="mailto:tfi-dev@tagus.ist.utl.pt">tdi-dev</a>
     * @author <a href="mailto:edgar.gonçalves@tagus.ist.utl.pt">Edgar Gonçalves</a>
     * @version 1.0
     */
    public class LessonTransformer implements Transformer
    {
        public LessonTransformer()
        {

        } // LessonTransformer constructor

        // Implementation of org.apache.commons.collections.Transformer

        /**
         * <code>transform</code> takes an Dominio.IAula and
         * returns an InfoLesson
         *
         * @param object an <code>Object</code> value
         * @return an <code>Object</code> value representing an InfoLesson
         */
        public Object transform(Object object)
        {
            return Cloner.copyILesson2InfoLesson((Dominio.IAula) object);
        }

    } // LessonTransformer

    /**
     * @author tdi-dev
     *
     */
    private class ClassTransformer implements Transformer
    {

        public Object transform(Object arg0)
        {

            ITurma thisClass = (ITurma) arg0;

            return Cloner.copyClass2InfoClass(thisClass);
        }
    }

    /**
     * @author tdi-dev
     *
     */
    private class ShiftStudentTransformer implements Transformer
    {

        public Object transform(Object arg0)
        {
            ITurnoAluno shiftStudent = (ITurnoAluno) arg0;
            InfoShift shift = new InfoShift();
            shift.setAvailabilityFinal(shiftStudent.getShift().getAvailabilityFinal());
            InfoExecutionCourse infoExecutionCourse =
                Cloner.copyIExecutionCourse2InfoExecutionCourse(
                    shiftStudent.getShift().getDisciplinaExecucao());
            shift.setInfoDisciplinaExecucao(infoExecutionCourse);

            shift.setLotacao(shiftStudent.getShift().getLotacao());
            shift.setNome(shiftStudent.getShift().getNome());
            shift.setTipo(shiftStudent.getShift().getTipo());
            shift.setIdInternal(shiftStudent.getShift().getIdInternal());
            return shift;
        }
    }

    /**
     * @author tdi-dev
     *
     */
    private class ShiftTransformer implements Transformer
    {

        public Object transform(Object arg0)
        {
            ITurno shift = (ITurno) arg0;
            return Cloner.copyIShift2InfoShift(shift);
        }
    }

}
