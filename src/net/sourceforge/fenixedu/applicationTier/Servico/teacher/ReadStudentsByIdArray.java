/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsByIdArray implements IService {

    public ReadStudentsByIdArray() {
    }

    public Object run(Integer executionCourseId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException {

        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            List studentList = new ArrayList();
            if (selected != null && selected.length != 0) {
                if (insertByShifts.booleanValue())
                    studentList = returnStudentsFromShiftsArray(persistentSuport, selected);
                else
                    studentList = returnStudentsFromStudentsArray(persistentSuport, selected,
                            executionCourse);
            }
            return studentList;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
    }

    public Object run(Integer executionCourseId, ArrayList lavelValueBeanList)
            throws FenixServiceException {

        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            List studentList = new ArrayList();
            for (int i = 0; i < lavelValueBeanList.size(); i++) {
                LabelValueBean lvb = (LabelValueBean) lavelValueBeanList.get(i);
                if (!lvb.getLabel().equals(" (Ficha Fechada)")) {
                    Integer number = new Integer(lvb.getValue());
                    studentList.add(InfoStudent.newInfoFromDomain((IStudent) persistentSuport
                            .getIPersistentStudent().readAllBetweenNumbers(number, number).get(0)));
                }
            }

            return studentList;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
    }

    private List returnStudentsFromShiftsArray(ISuportePersistente persistentSuport, String[] shifts)
            throws FenixServiceException {
        List studentsList = new ArrayList();
        try {

            ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
            for (int i = 0; i < shifts.length; i++) {
                if (shifts[i].equals("Todos os Turnos")) {
                    continue;
                }
                IShift shift = (IShift) persistentShift.readByOID(Shift.class, new Integer(shifts[i]));
                Iterator studentIt = persistentSuport.getITurnoAlunoPersistente().readByShift(shift)
                        .iterator();
                while (studentIt.hasNext()) {
                    InfoStudent infoStudent = InfoStudent.newInfoFromDomain((IStudent) studentIt.next());
                    if (!studentsList.contains(infoStudent))
                        studentsList.add(infoStudent);
                }

            }
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return studentsList;
    }

    private List returnStudentsFromStudentsArray(ISuportePersistente persistentSuport,
            String[] students, IExecutionCourse executionCourse) throws FenixServiceException {
        List studentsList = new ArrayList();
        try {

            for (int i = 0; i < students.length; i++) {
                if (students[i].equals("Todos os Alunos")) {
                    List attendList = persistentSuport.getIFrequentaPersistente().readByExecutionCourse(
                            executionCourse);

                    Iterator iterStudent = attendList.listIterator();
                    while (iterStudent.hasNext()) {
                        IAttends attend = (Attends) iterStudent.next();
                        InfoStudent infoStudent = InfoStudent.newInfoFromDomain(attend
                                .getAluno());
                        studentsList.add(infoStudent);
                    }
                    break;
                }
                InfoStudent infoStudent = InfoStudent.newInfoFromDomain((Student) persistentSuport
                        .getIPersistentStudent().readByOID(Student.class, new Integer(students[i])));
                if (!studentsList.contains(infoStudent))
                    studentsList.add(infoStudent);

            }
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return studentsList;
    }
}