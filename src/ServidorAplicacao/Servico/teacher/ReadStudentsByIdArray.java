/*
 * Created on 19/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.Student;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
                    studentList.add(InfoStudent.newInfoFromDomain((IStudent) ((List) persistentSuport
                            .getIPersistentStudent().readAllBetweenNumbers(number, number)).get(0)));
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
                ITurno shift = (ITurno) persistentShift.readByOID(Turno.class, new Integer(shifts[i]));
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
                        IFrequenta attend = (Frequenta) iterStudent.next();
                        InfoStudent infoStudent = InfoStudent.newInfoFromDomain((Student) attend
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