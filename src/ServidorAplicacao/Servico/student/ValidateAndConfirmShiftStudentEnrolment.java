/**
 * Project Sop 
 * 
 * Package ServidorAplicacao.Servico.student
 * 
 * Created on 18/Dez/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoClass;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoShiftStudentEnrolment;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ShiftStudent;
import Dominio.Student;
import Dominio.Turma;
import Dominio.TurmaTurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author tdi-dev
 * 
 *  
 */
public class ValidateAndConfirmShiftStudentEnrolment implements IServico
{
    /**
	 * @author stocker
	 * 
	 * Transforms a shift of the type IShift in an object of the type InfoShift
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
    private static ValidateAndConfirmShiftStudentEnrolment _service =
        new ValidateAndConfirmShiftStudentEnrolment();

    private ValidateAndConfirmShiftStudentEnrolment()
    {
    }

    public static ValidateAndConfirmShiftStudentEnrolment getService()
    {
        return _service;
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public final String getNome()
    {
        return "ValidateAndConfirmShiftStudentEnrolment";
    }

    /**
	 * Works with SHIFT_STUDENT table. Inserts and updates table.
	 *  
	 */
    public InfoShiftStudentEnrolment run(InfoShiftStudentEnrolment infoShiftStudentEnrolment)
        throws FenixServiceException, ExcepcaoPersistencia
    {
        if (infoShiftStudentEnrolment == null)
        {
            throw new IllegalArgumentException("InfoShiftStudentEnrolment must be not null!");
        }
        /**
		 * Steps to validate the user's choices:
		 * 
		 * It can deal with shift changes (from the same course). It cannot
		 * deal with shifts removal.
		 * 
		 * Obtain from the action: actualShifts - actual shifts for the student
		 * (currentEnrolment); wantedShifts - wanted shifts; Create:
		 * filledShifts - table for shifts without vacancies; newShifts - table
		 * for pairs of shifts (old and new, after validation) Algorithm: 1)
		 * Pick a shift from the wantedShifts 1.1) Check if it there is any
		 * shift of the same kind, of the same course in the wantedShifts
		 * (throws an error!); 1.2) Check if it there is any shift of the same
		 * kind, of the same class in the wantedShifts (throws an error!); 1.3)
		 * Filter entries that already exists in actualShifts: - insert the
		 * pair in newShifts in the form (actualShift . NULL); - remove the
		 * shifts from actualShifts; 1.4) Filter shifts that are not associated
		 * with the classes the user is allowed to be in. 1.5) (IF (the wanted
		 * shift has FNL vacancies?) (THEN - Decrement the FNL vacancies for
		 * the shift; (IF the shift already exists in actualShifts (type,
		 * executionCourse)) (THEN - Insert in newShifts a new pair with
		 * (old-shift . new-shift) -> changed shift - Increment the FNL
		 * vacancies for the old shift; - remove the old-shift from the
		 * actualShifts) (ELSE Insert in newShifts a new pair with (NULL .
		 * new-shift) -> new shift )) (ELSE insert the shift in the
		 * filledShifts)) 2) If the actualShifts is not null, throw an
		 * exception; 3) Return the lists: - newShifts - filledShifts - shifts
		 * that could not be booked
		 */

        IStudent student = new Student();
        student.setDegreeType(infoShiftStudentEnrolment.getInfoStudent().getDegreeType());
        student.setNumber(infoShiftStudentEnrolment.getInfoStudent().getNumber());

        List filledShifts = new ArrayList();
        List newShifts = new ArrayList();

        List errors = new ArrayList();

        //1

        ShiftTransformerInverse shiftTransformerInverse = new ShiftTransformerInverse();
        //		Copy of the wanted Shifts, in ITurno format
        List wantedShifts =
            (List) CollectionUtils.collect(
                infoShiftStudentEnrolment.getWantedShifts(),
                shiftTransformerInverse);

        //make the shifts persistent
        wantedShifts = getPersistentShifts(wantedShifts);
        //		Copy of the current Enrolment (actual Shifts), in ITurno format

        //		List actualShifts =
        //			(List) CollectionUtils.collect(
        //				infoShiftStudentEnrolment.getCurrentEnrolment(),
        //				shiftTransformerInverse);

        //Get the persistentSupport instance
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        //Get the actual shifts updated from the database:
        //		Open a OJB entrypoint for the ShiftStudent table
        ITurnoAlunoPersistente shiftStudentDAO = sp.getITurnoAlunoPersistente();

        //read all the shiftStudent for that student
        ITurnoAluno shiftStudentExample = new ShiftStudent();
        shiftStudentExample.setStudent(student);
        List actualShifts = shiftStudentDAO.readByCriteria(shiftStudentExample);
        //actualShifts are ITurno objects, from now on, and are read from the
        // database
        for (int i = 0; i < actualShifts.size(); i++)
        {
            actualShifts.set(i, ((ITurnoAluno) actualShifts.get(i)).getShift());
        }

        //Old shift configuration to be presented as extra info to the user
        ShiftTransformer shiftTransformer = new ShiftTransformer();
        infoShiftStudentEnrolment.setCurrentEnrolment(
            (List) CollectionUtils.collect(actualShifts, shiftTransformer));

        //get the students allowed classes, to check each shift against it
        List studentsAllowedClasses = getStudentsAllowedClasses(student, sp);

        List pretendedNewShifts = getNewShiftsOnly(wantedShifts, actualShifts);

        Iterator i = pretendedNewShifts.iterator();
        while (i.hasNext())
        {
            ITurno shift = (ITurno) i.next();

            ArrayList listTmp;

            //1.0 - check if the shift was already reported as erroneous
            if (alreadyReportedAsBogusShift(errors, shift))
            {
                i.remove();
                continue;
            }

            //1.1 - Produce an error if the shift is of the same type and same
            // course than another pretended one.
            if ((listTmp = obtainShiftsOfSameTypeAndCourse(pretendedNewShifts, shift)).size() > 1)
            {

                treatShiftInErrors(
                    errors,
                    actualShifts,
                    listTmp,
                    "ValidadeShiftStudentEnrolment - Não se pode escolher mais que um turno para a mesma cadeira que sejam do mesmo tipo!");

                //don't remove the shift, to make sure the other shift is
                // reported as a bogus one.
                continue;
            }

            //1.2 - Produce an error if the shift is of the same course than
            // another pretended one but of different classes.
            //checks in all the shifts (both pretended and actual)
            if ((listTmp = obtainShiftsOfDifferentClassesAndSameCourse(wantedShifts, shift)).size() > 1)
            {

                treatShiftInErrors(
                    errors,
                    actualShifts,
                    listTmp,
                    "ValidadeShiftStudentEnrolment - Não se pode escolher mais que um turno para a mesma cadeira em turmas diferentes!");
                //don't remove the shift, to make sure the other shift is
                // reported as a bogus one.
                continue;
            }

            //1.3
            if (actualShifts.contains(shift))
            {
                /*
				 * Filter entries that already exists in actualShifts: - insert
				 * the pair in newShifts in the form (actualShift . NULL); -
				 * remove the shifts from actualShifts;
				 */

                InfoShift pair[] = new InfoShift[2];
                pair[0] = Cloner.copyIShift2InfoShift(shift);
                pair[1] = null;

                newShifts.add(pair);

                actualShifts.remove(shift);
                i.remove();
                continue;
            }

            //1.4 Filter shifts that are not associated with the classes the
            // user is allowed to be in.
            if (!theClassIsAllowed(getClassesAssociatedWithShift(shift, sp), studentsAllowedClasses))
            {
                ShiftConflict error =
                    new ShiftConflict(
                        shift,
                        "ValidateAndConfirmShiftStudentEnrolment - you are not allowed to be in this shift!!");
                errors.add(error);
                i.remove();
                continue;
            }

            //1.5
            ITurno shiftExample = new Turno();
            //Added support to idInternal -> it should be the only thing
            // necessary
            shiftExample.setIdInternal(shift.getIdInternal());

            ITurnoPersistente shiftDAO = sp.getITurnoPersistente();
            ITurno obtainedShift = (ITurno) shiftDAO.readDomainObjectByCriteria(shiftExample);
            if (obtainedShift == null)
            {
                ShiftConflict error =
                    new ShiftConflict(
                        shift,
                        "ValidateAndConfirmShiftStudentEnrolment - turno não existe!");
                errors.add(error);
                actualShifts.remove(shift);
                i.remove();
                continue;
                //throw new
                // FenixServiceException("ValidateAndConfirmShiftStudentEnrolment
                // - non existent shift!");
            }

            int availabilityFinal = obtainedShift.getAvailabilityFinal().intValue();

            if (availabilityFinal > 0)
            {
                //decrement the value of FNL
                //TODO: tdi-dev (edgar.goncalves) -> make sure the database is
                // updated successfully
                obtainedShift.setAvailabilityFinal(new Integer(availabilityFinal - 1));

                ITurno oldShift = obtainShiftOfSameTypeAndCourse(actualShifts, shift);

                if (oldShift != null)
                {
                    /*
					 * the shift already exixts in actual shifts (type,
					 * executionCourse)
					 */
                    /*
					 * - Insert in newShifts a new pair with (old-shift .
					 * new-shift) -> changed shift - Increment the FNL
					 * vacancies for the old shift;
					 */

                    InfoShift pair[] = new InfoShift[2];
                    pair[0] = Cloner.copyIShift2InfoShift(oldShift);
                    pair[1] = Cloner.copyIShift2InfoShift(shift);
                    newShifts.add(pair);

                    //Removing the old shift
                    ITurnoAluno thisShiftStudent = new ShiftStudent();
                    ITurno thisShift = new Turno();
                    thisShift.setIdInternal(oldShift.getIdInternal());

                    thisShiftStudent.setShift(thisShift);
                    thisShiftStudent.setStudent(student);

                    List oldShiftStudents = shiftStudentDAO.readByCriteria(thisShiftStudent);

                    shiftStudentDAO.delete((ITurnoAluno) oldShiftStudents.get(0));

                    //Writing to the database the newshift
                    IStudent studentTmp =
                        sp.getIPersistentStudent().readStudentByNumberAndDegreeType(
                            student.getNumber(),
                            student.getDegreeType());
                    ITurno shiftTmp =
                        sp.getITurnoPersistente().readByNameAndExecutionCourse(
                            shift.getNome(),
                            shift.getDisciplinaExecucao());
                    ITurnoAluno newShiftStudent = new ShiftStudent(shiftTmp, studentTmp);
                    ((ServidorPersistente.OJB.TurnoAlunoOJB) shiftStudentDAO).simpleLockWrite(
                        newShiftStudent);

                    obtainedShift.setAvailabilityFinal(new Integer(availabilityFinal - 1));
                    oldShift.setAvailabilityFinal(
                        new Integer(oldShift.getAvailabilityFinal().intValue() + 1));

                    Iterator i2 = actualShifts.iterator();
                    while (i2.hasNext())
                    {
                        ITurno element = (ITurno) i2.next();
                        if ((element.getNome().equals(oldShift.getNome()))
                            && (element
                                .getDisciplinaExecucao()
                                .getNome()
                                .equals(oldShift.getDisciplinaExecucao().getNome())))
                        {
                            i2.remove();
                            break;
                        }
                    }
                    //Updates the availability on the database
                    shiftDAO.lockWrite(obtainedShift);
                    shiftDAO.lockWrite(oldShift);

                    i.remove();
                }
                else
                {
                    /*
					 * Insert in newShifts a new pair with (NULL . new-shift) ->
					 * new shift
					 */

                    InfoShift pair[] = new InfoShift[2];
                    pair[0] = null;
                    pair[1] = Cloner.copyIShift2InfoShift(shift);
                    newShifts.add(pair);

                    //Updates the availability on the database
                    obtainedShift.setAvailabilityFinal(new Integer(availabilityFinal - 1));
                    shiftDAO.lockWrite(obtainedShift);

                    //Writing to the database the newshift
                    IStudent studentTmp =
                        sp.getIPersistentStudent().readStudentByNumberAndDegreeType(
                            student.getNumber(),
                            student.getDegreeType());
                    ITurno shiftTmp =
                        sp.getITurnoPersistente().readByNameAndExecutionCourse(
                            shift.getNome(),
                            shift.getDisciplinaExecucao());
                    ITurnoAluno newShiftStudent = new ShiftStudent(shiftTmp, studentTmp);
                    ((ServidorPersistente.OJB.TurnoAlunoOJB) shiftStudentDAO).simpleLockWrite(
                        newShiftStudent);
                }
            }
            else
            {
                /*
				 * insert the shift in the filledShifts, for the shift is full!
				 */
                filledShifts.add(shift);

            }

        }

        /*
		 * 2) If the actualShifts is not null, throw an exception
		 */

        //		if (actualShifts.size() != 0) {
        //			//errors.add("error.wanted.shifts.do.not.contain.current.shifts");
        //			throw new
        // FenixServiceException("ValidateAndConfirmShiftStudentEnrolment -
        //  conflicts in the current enrolment: wanted shifts do not contain the
        // current shifts!");
        //		}

        /*
		 * 3) Return the lists: - newShifts - filledShifts - shifts that could
		 * not be booked - currentEnrollment - old shift configuration
		 */

        InfoShiftStudentEnrolment result = infoShiftStudentEnrolment;
        result.setFilledShifts((List) CollectionUtils.collect(filledShifts, shiftTransformerInverse));
        result.setNewShifts(newShifts);

        if (errors.size() == 0)
        {
            result.setErrors(null);
        }
        else
        {
            result.setErrors(errors);
        }

        result.setCurrentEnrolment(setLessons2InfoShifts(result.getCurrentEnrolment()));
        //			result.setCurrentEnrolment(
        //						(List) CollectionUtils.collect(actualShifts, shiftTransformer));
        return result;
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
        }
        else
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
	 * @param wantedShifts
	 * @return
	 */
    private List getPersistentShifts(List wantedShifts) throws ExcepcaoPersistencia
    {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        List shifts = new ArrayList();
        Iterator iter = wantedShifts.iterator();
        while (iter.hasNext())
        {
            ITurno shift = (ITurno) iter.next();
            shift = (ITurno) persistentShift.readByOId(shift, false);
            if (shift != null)
            {
                shifts.add(shift);
            }
        }
        return shifts;
    }

    /**
	 * @param wantedShifts -
	 *            list with all the shifts the student wants to be in
	 *            (ITurma's)
	 * @param actualShifts -
	 *            list with the actual shifts the student is enrolled with
	 *            (ITurma's)
	 * @return
	 */
    private List getNewShiftsOnly(List wantedShifts, List actualShifts)
    {
        List result = new ArrayList();

        Iterator i = wantedShifts.iterator();
        while (i.hasNext())
        {
            ITurno wantedShift = (ITurno) i.next();
            if (!(actualShifts.contains(wantedShift)))
            {
                result.add(wantedShift);
            }
        }
        return result;
    }

    /**
	 * getStudentsAllowedClasses
	 * 
	 * @param student
	 * @param sp
	 *            Persistent Suport
	 * @return list of classes (ITurma) with the allowed classes for the
	 *         student
	 * @throws ExcepcaoPersistencia
	 */
    private List getStudentsAllowedClasses(IStudent student, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        ITurno shiftExample = new Turno();
        IExecutionCourse executionCourseExample = new ExecutionCourse();
        List associatedStudents = new ArrayList();
        associatedStudents.add(student);
        executionCourseExample.setAttendingStudents(associatedStudents);
        shiftExample.setDisciplinaExecucao(executionCourseExample);

        ITurma classExample = new Turma();

        ITurmaTurno classShiftExample = new TurmaTurno();
        classShiftExample.setTurma(classExample);
        classShiftExample.setTurno(shiftExample);

        List classShifts = sp.getITurmaTurnoPersistente().readByCriteria(classShiftExample);

        List result = getDistinctClassesFromListOfClassShifts(classShifts);
        return result;
    }

    /**
	 * @param classShifts
	 * @return List with distinct classes (IClass)
	 */
    private List getDistinctClassesFromListOfClassShifts(List classShifts)
    {
        Iterator i = classShifts.iterator();
        List result = new ArrayList();
        while (i.hasNext())
        {
            ITurma clazz = ((ITurmaTurno) i.next()).getTurma();

            if (!result.contains(clazz))
            {
                result.add(clazz);
            }

            //			Iterator resultIterator = result.iterator();
            //			boolean containsOneOfThese = false;
            //			while (resultIterator.hasNext()) {
            //				ITurma resultShift = (ITurma) resultIterator.next();
            //				if (shift.equals(resultShift)) {
            //					containsOneOfThese = true;
            //					break;
            //				}
            //			}
            //			if (!containsOneOfThese) {
            //				result.add(shift);
            //			}
        }
        return result;
    }

    /**
	 * theClassIsAllowed returns true if the fist list (with the classes of one
	 * shift) intersects the second list (with the classes the student is
	 * allowed to be in).
	 * 
	 * @param list1
	 *            of classes
	 * @param list2
	 *            of classes
	 * @return true if list1 intersected with list2 is not null
	 */
    private boolean theClassIsAllowed(List shiftClasses, List studentAllowedClasses)
    {
        Iterator iterator = shiftClasses.iterator();

        while (iterator.hasNext())
        {
            InfoClass thisClass = (InfoClass) iterator.next();
            Iterator iterator2 = studentAllowedClasses.iterator();
            while (iterator2.hasNext())
            {
                ITurma element = (ITurma) iterator2.next();
                if (element.getNome().equals(thisClass.getNome()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * @param shift -
	 *            Shift to be used in OJB usage
	 * @return
	 */
    private List getClassesAssociatedWithShift(ITurno shift, ISuportePersistente sp)
        throws FenixServiceException
    {
        ITurmaTurnoPersistente shiftClassDAO = sp.getITurmaTurnoPersistente();
        if (shift != null)
        {
            List classes;
            try
            {
                classes = shiftClassDAO.readClassesWithShift(shift);
            }
            catch (ExcepcaoPersistencia e)
            {
                e.printStackTrace(System.out);
                throw new FenixServiceException(e);
            }

            //Return a list of InfoClass objects
            ShiftClassTransformer shiftClassTransformer = new ShiftClassTransformer();
            return (List) CollectionUtils.collect(classes, shiftClassTransformer);
        }
        return null;
    }

    /**
	 * @param errors
	 * @param actualShifts
	 * @param listTmp
	 * @param msg
	 */
    private void treatShiftInErrors(List errors, List actualShifts, ArrayList listTmp, String msg)
    {
        Iterator iteratorTmp;
        Iterator iteratorTmp2;
        //Remove from the bug list and from the actualShifts the fake
        // erroneous Shifts that are already enroled
        iteratorTmp = listTmp.iterator();
        boolean shiftIsInActualShifts = false;

        while (iteratorTmp.hasNext())
        {
            ITurno tempShift = (ITurno) (iteratorTmp.next());
            int idCode = tempShift.getIdInternal().intValue();

            iteratorTmp2 = actualShifts.iterator();
            while (iteratorTmp2.hasNext())
            {
                if (idCode == ((ITurno) (iteratorTmp2.next())).getIdInternal().intValue())
                {
                    shiftIsInActualShifts = true;
                    break;
                }
            }

            //Produce an error if it isn't already there
            if ((!shiftIsInActualShifts) && (!alreadyReportedAsBogusShift(errors, tempShift)))
            {
                ShiftConflict error = new ShiftConflict(tempShift, msg);
                errors.add(error);
            }

        }
    }

    /**
	 * alreadyReportedAsBogusShift
	 * 
	 * self-explaining...
	 * 
	 * @param errors -
	 *            List list of errors
	 * @param shift
	 * @return true if the shift is already in the error list, false otherwise
	 */
    private boolean alreadyReportedAsBogusShift(List errors, ITurno shift)
    {
        Iterator errorIterator = errors.iterator();
        while (errorIterator.hasNext())
        {
            if (shift.getIdInternal().intValue()
                == ((ShiftConflict) (errorIterator.next())).getShift().getIdInternal().intValue())
            {
                return true;
            }
        }
        return false;
    }

    /**
	 * obtainShiftsOfSameTypeAndCourse
	 * 
	 * self-explaining...
	 * 
	 * @param list
	 *            List with InfoShift objects
	 * @param shift
	 *            InfoShift object that is going to be used as a search
	 *            criteria @returnArrayList with the shift in the list that
	 *            satisfied this criteria
	 */
    private ArrayList obtainShiftsOfSameTypeAndCourse(List list, ITurno shift)
    {

        final TipoAula type = shift.getTipo();
        final IExecutionCourse course = shift.getDisciplinaExecucao();

        ArrayList resultList = (ArrayList) CollectionUtils.select(list, new Predicate()
        {

            public boolean evaluate(Object arg0)
            {
                ITurno infoShift = (ITurno) arg0;
                return infoShift.getTipo().equals(type)
                    && infoShift.getDisciplinaExecucao().equals(course);
            }
        });
        return resultList;
    }

    /**
	 * obtainShiftOfSameTypeAndCourse
	 * 
	 * self-explaining...
	 * 
	 * @param l
	 *            List with InfoShift objects
	 * @param shift
	 *            InfoShift object that is going to be used as a search
	 *            criteria
	 * @return match the shift in the list (the first one only!)
	 */
    private ITurno obtainShiftOfSameTypeAndCourse(List l, ITurno shift)
    {

        final TipoAula type = shift.getTipo();
        final IExecutionCourse course = shift.getDisciplinaExecucao();

        return (ITurno) CollectionUtils.find(l, new Predicate()
        {

            public boolean evaluate(Object arg0)
            {
                ITurno shift = (ITurno) arg0;
                return shift.getTipo().equals(type) && shift.getDisciplinaExecucao().equals(course);
            }
        });
    }

    /**
	 * obtainShiftsOfSameClassAndCourses
	 * 
	 * Checks wether there is not any more Shifts in the list l belonging to
	 * the same course that are not of the same class
	 * 
	 * @param l
	 *            List with InfoShift objects
	 * @param shift
	 *            InfoShift object that is going to be used as a search
	 *            criteria
	 * @return ArrayList: list of shifts tht should not be processed
	 */

    private ArrayList obtainShiftsOfDifferentClassesAndSameCourse(List l, ITurno shift)
        throws ExcepcaoPersistencia
    {
        ArrayList result = new ArrayList();

        final IExecutionCourse course = shift.getDisciplinaExecucao();

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        ITurmaTurnoPersistente classShiftDAO = sp.getITurmaTurnoPersistente();
        ArrayList obtainedClasses = (ArrayList) classShiftDAO.readByShift(shift);

        //Assuming there is two shifts (tops) of the same course:
        ITurno otherShift = (ITurno) CollectionUtils.find(l, new Predicate()
        {

            public boolean evaluate(Object arg0)
            {
                ITurno infoShift = (ITurno) arg0;
                return infoShift.getDisciplinaExecucao().equals(course);
            }
        });

        ArrayList obtainedClassesFromTheOtherShift = (ArrayList) classShiftDAO.readByShift(otherShift);

        ArrayList list =
            (ArrayList) CollectionUtils.intersection(obtainedClassesFromTheOtherShift, obtainedClasses);
        if (list.size() == 0)
        {
            result.add(shift);
            result.add(otherShift);
        }

        return result;
    }

    /**
	 * @author tdi-dev
	 * 
	 * Container (bean) for an error message and the erroneous shift.
	 * 
	 * TODO: tdi-dev -> get a proper place (package) for this subclass:
	 * ShiftConflict. (posted by edgar.goncalves)
	 *  
	 */
    public class ShiftConflict
    {

        private String msg;
        private InfoShift shift;

        public ShiftConflict()
        {
        }

        public ShiftConflict(ITurno s, String m)
        {
            this.setMsg(m);
            this.setShift(Cloner.copyIShift2InfoShift(s));
        }

        /**
		 * @return the error message
		 */
        public String getMsg()
        {
            return msg;
        }

        /**
		 * @return the shift with an error
		 */
        public InfoShift getShift()
        {
            return shift;
        }

        /**
		 * @param string:
		 *            an error message
		 */
        public void setMsg(String string)
        {
            msg = string;
        }

        /**
		 * @param turno
		 */
        public void setShift(InfoShift turno)
        {
            shift = turno;
        }

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
	 * @author tdi-dev
	 * 
	 * Transforms a shift of the type InfoShift in an object of the type IShift
	 *  
	 */
    private class ShiftTransformerInverse implements Transformer
    {

        public Object transform(Object arg0)
        {
            InfoShift shift = (InfoShift) arg0;
            return Cloner.copyInfoShift2IShift(shift);
        }
    }

    /**
	 * @author tdi-dev
	 * 
	 * Container (bean) for a shift and its associated classes.
	 * 
	 * TODO: tdi-dev -> get a proper place (package) for this subclass:
	 * ShiftWithAssociatedClasses (posted by edgar.goncalves).
	 *  
	 */
    public class ShiftWithAssociatedClasses
    {

        private ArrayList classes;
        private InfoShift shift;

        /**
		 *  
		 */
        public ShiftWithAssociatedClasses()
        {
            super();
        }

        /**
		 *  
		 */
        public ShiftWithAssociatedClasses(InfoShift shift, ArrayList classes)
        {
            this.setClasses(classes);
            this.setShift(shift);
        }

        /**
		 * @return
		 */
        public ArrayList getClasses()
        {
            return classes;
        }

        /**
		 * @return
		 */
        public InfoShift getShift()
        {
            return shift;
        }

        /**
		 * @param list
		 */
        public void setClasses(ArrayList list)
        {
            classes = list;
        }

        /**
		 * @param turno
		 */
        public void setShift(InfoShift turno)
        {
            shift = turno;
        }

    }
}
