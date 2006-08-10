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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsByIdArray extends Service {

    public List<InfoStudent> run(Integer executionCourseId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException, ExcepcaoPersistencia {

        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        if (selected != null && selected.length != 0) {
            if (insertByShifts.booleanValue())
                studentList = returnStudentsFromShiftsArray(selected);
            else
                studentList = returnStudentsFromStudentsArray(selected, executionCourseId);
        }
        return studentList;
    }

    public List<InfoStudent> run(Integer executionCourseId, ArrayList lavelValueBeanList)
            throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        for (LabelValueBean lvb : (ArrayList<LabelValueBean>) lavelValueBeanList) {
            if (!lvb.getLabel().equals(" (Ficha Fechada)")) {
                Integer number = new Integer(lvb.getValue());
                studentList.add(InfoStudent.newInfoFromDomain(Registration.readAllStudentsBetweenNumbers(
                        number, number).get(0)));
            }
        }

        return studentList;
    }

    private List<InfoStudent> returnStudentsFromShiftsArray(String[] shifts)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources");
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (int i = 0; i < shifts.length; i++) {
            if (shifts[i].equals(bundle.getString("label.allShifts"))) {
                continue;
            }
            Shift shift = rootDomainObject.readShiftByOID(new Integer(shifts[i]));
            List<Registration> studentList = shift.getStudents();
            for (Registration student : studentList) {
                InfoStudent infoStudent = InfoStudent.newInfoFromDomain(student);
                if (!infoStudentList.contains(infoStudent))
                    infoStudentList.add(infoStudent);
            }

        }
        return infoStudentList;
    }

    private List<InfoStudent> returnStudentsFromStudentsArray(String[] students,
            Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources");
        List<InfoStudent> studentsList = new ArrayList<InfoStudent>();
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        for (int i = 0; i < students.length; i++) {
            if (students[i].equals(bundle.getString("label.allStudents"))) {
                List<Attends> attendList = executionCourse.getAttends();
                for (Attends attend : attendList) {
                    studentsList.add(InfoStudent.newInfoFromDomain(attend.getAluno()));
                }
                break;
            }
            InfoStudent infoStudent = InfoStudent.newInfoFromDomain(rootDomainObject
                    .readRegistrationByOID(new Integer(students[i])));
            if (!studentsList.contains(infoStudent))
                studentsList.add(infoStudent);

        }
        return studentsList;
    }
}