/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsByIdArray extends Service {

    public List<InfoStudent> run(Integer executionCourseId, String[] selected, Boolean insertByShifts) throws FenixServiceException,
            ExcepcaoPersistencia {

        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        if (selected != null && selected.length != 0) {
            if (insertByShifts.booleanValue())
                studentList = returnStudentsFromShiftsArray(persistentSupport, selected);
            else
                studentList = returnStudentsFromStudentsArray(persistentSupport, selected, executionCourseId);
        }
        return studentList;
    }

    public List<InfoStudent> run(Integer executionCourseId, ArrayList lavelValueBeanList) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        for (LabelValueBean lvb : (ArrayList<LabelValueBean>) lavelValueBeanList) {
            if (!lvb.getLabel().equals(" (Ficha Fechada)")) {
                Integer number = new Integer(lvb.getValue());
                studentList.add(InfoStudent.newInfoFromDomain((Student) persistentSupport.getIPersistentStudent().readAllBetweenNumbers(number,
                        number).get(0)));
            }
        }

        return studentList;
    }

    private List<InfoStudent> returnStudentsFromShiftsArray(ISuportePersistente persistentSupport, String[] shifts) throws FenixServiceException,
            ExcepcaoPersistencia {
        final ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (int i = 0; i < shifts.length; i++) {
            if (shifts[i].equals(bundle.getString("label.allShifts"))) {
                continue;
            }
            Shift shift = (Shift) persistentObject.readByOID(Shift.class, new Integer(shifts[i]));
            List<Student> studentList = shift.getStudents();
            for (Student student : studentList) {
                InfoStudent infoStudent = InfoStudent.newInfoFromDomain(student);
                if (!infoStudentList.contains(infoStudent))
                    infoStudentList.add(infoStudent);
            }

        }
        return infoStudentList;
    }

    private List<InfoStudent> returnStudentsFromStudentsArray(ISuportePersistente persistentSupport, String[] students, Integer executionCourseId)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
        List<InfoStudent> studentsList = new ArrayList<InfoStudent>();

        for (int i = 0; i < students.length; i++) {
            if (students[i].equals(bundle.getString("label.allStudents"))) {
                List<Attends> attendList = persistentSupport.getIFrequentaPersistente().readByExecutionCourse(executionCourseId);
                for (Attends attend : attendList) {
                    studentsList.add(InfoStudent.newInfoFromDomain(attend.getAluno()));
                }
                break;
            }
            InfoStudent infoStudent = InfoStudent.newInfoFromDomain((Student) persistentObject.readByOID(Student.class,
                    new Integer(students[i])));
            if (!studentsList.contains(infoStudent))
                studentsList.add(infoStudent);

        }
        return studentsList;
    }
}