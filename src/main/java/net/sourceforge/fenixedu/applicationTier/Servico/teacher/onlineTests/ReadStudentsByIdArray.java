/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsByIdArray {

    public List<InfoStudent> run(Integer executionCourseId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException {

        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        if (selected != null && selected.length != 0) {
            if (insertByShifts.booleanValue()) {
                studentList = returnStudentsFromShiftsArray(null, selected);
            } else {
                studentList = returnStudentsFromStudentsArray(null, selected, executionCourseId);
            }
        }
        return studentList;
    }

    public List<InfoStudent> run(Integer executionCourseId, Integer distributedTestId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException {

        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        DistributedTest distributedTest = RootDomainObject.getInstance().readDistributedTestByOID(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (selected != null && selected.length != 0) {
            if (insertByShifts.booleanValue()) {
                studentList = returnStudentsFromShiftsArray(distributedTest, selected);
            } else {
                studentList = returnStudentsFromStudentsArray(distributedTest, selected, executionCourseId);
            }
        }
        return studentList;
    }

    public List<InfoStudent> run(Integer executionCourseId, ArrayList lavelValueBeanList) throws FenixServiceException {
        List<InfoStudent> studentList = new ArrayList<InfoStudent>();
        for (LabelValueBean lvb : (ArrayList<LabelValueBean>) lavelValueBeanList) {
            if (!lvb.getLabel().equals(" (Ficha Fechada)")) {
                Integer number = Integer.parseInt(lvb.getValue());
                studentList.add(InfoStudent.newInfoFromDomain(Registration.readAllStudentsBetweenNumbers(number, number).get(0)));
            }
        }

        return studentList;
    }

    private List<InfoStudent> returnStudentsFromShiftsArray(DistributedTest distributedTest, String[] shifts)
            throws FenixServiceException {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (String shift2 : shifts) {
            if (shift2.equals(bundle.getString("label.allShifts"))) {
                continue;
            }
            Shift shift = RootDomainObject.getInstance().readShiftByOID(new Integer(shift2));
            List<Registration> studentList = shift.getStudents();
            for (Registration registration : studentList) {
                InfoStudent infoStudent = InfoStudent.newInfoFromDomain(registration);
                if (!infoStudentList.contains(infoStudent)
                        && (distributedTest == null || !StudentTestQuestion
                                .hasStudentTestQuestions(registration, distributedTest))) {
                    infoStudentList.add(infoStudent);
                }
            }

        }
        return infoStudentList;
    }

    private List<InfoStudent> returnStudentsFromStudentsArray(DistributedTest distributedTest, String[] students,
            Integer executionCourseId) throws FenixServiceException {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
        List<InfoStudent> studentsList = new ArrayList<InfoStudent>();
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);

        for (String student : students) {
            if (student.equals(bundle.getString("label.allStudents"))) {
                List<Attends> attendList = executionCourse.getAttends();
                for (Attends attend : attendList) {
                    InfoStudent infoStudent = InfoStudent.newInfoFromDomain(attend.getRegistration());
                    if (!studentsList.contains(infoStudent)
                            && (distributedTest == null || !StudentTestQuestion.hasStudentTestQuestions(attend.getRegistration(),
                                    distributedTest))) {
                        studentsList.add(infoStudent);
                    }
                }
                break;
            }
            Registration registration = RootDomainObject.getInstance().readRegistrationByOID(new Integer(student));
            InfoStudent infoStudent = InfoStudent.newInfoFromDomain(registration);
            if (!studentsList.contains(infoStudent)) {
                if (!studentsList.contains(infoStudent)
                        && (distributedTest == null || !StudentTestQuestion
                                .hasStudentTestQuestions(registration, distributedTest))) {
                    studentsList.add(infoStudent);
                }
            }

        }
        return studentsList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsByIdArray serviceInstance = new ReadStudentsByIdArray();

    @Service
    public static List<InfoStudent> runReadStudentsByIdArray(Integer executionCourseId, String[] selected, Boolean insertByShifts)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, selected, insertByShifts);
    }

    @Service
    public static List<InfoStudent> runReadStudentsByIdArray(Integer executionCourseId, Integer distributedTestId,
            String[] selected, Boolean insertByShifts) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId, selected, insertByShifts);
    }

}