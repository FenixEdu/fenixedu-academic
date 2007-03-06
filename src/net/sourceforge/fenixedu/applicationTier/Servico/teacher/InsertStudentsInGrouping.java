/*
 * Created on 08/Mars/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa & rmalo
 * 
 */

public class InsertStudentsInGrouping extends Service {

    public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, String[] selected)
            throws FenixServiceException, ExcepcaoPersistencia {

        List students = new ArrayList();

        Grouping groupProperties = rootDomainObject.readGroupingByOID(
                groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        if (selected == null)
            return Boolean.TRUE;

        List studentCodes = Arrays.asList(selected);

        Iterator iterator = studentCodes.iterator();

        while (iterator.hasNext()) {
            String number = (String) iterator.next();
            if (number.equals("Todos os Alunos")) {
            } else {
                Registration registration = rootDomainObject.readRegistrationByOID(
                        new Integer(number));
                students.add(registration);
            }
        }

        Iterator iterAttends = groupProperties.getAttends().iterator();

        while (iterAttends.hasNext()) {
            Attends existingAttend = (Attends) iterAttends.next();
            Registration existingAttendStudent = existingAttend.getRegistration();

            List studentsList = new ArrayList();
            studentsList.addAll(students);
            Iterator iteratorStudents = studentsList.iterator();

            while (iteratorStudents.hasNext()) {

                Registration registration = (Registration) iteratorStudents.next();
                if (registration.equals(existingAttendStudent)) {
                    throw new InvalidSituationServiceException();
                }
            }
        }

        List studentsList1 = new ArrayList();
        studentsList1.addAll(students);
        Iterator iterStudents1 = studentsList1.iterator();

        while (iterStudents1.hasNext()) {
            Attends attend = null;
            Registration registration = (Registration) iterStudents1.next();

            List listaExecutionCourses = new ArrayList();
            listaExecutionCourses.addAll(groupProperties.getExecutionCourses());
            Iterator iterExecutionCourse = listaExecutionCourses.iterator();
            while (iterExecutionCourse.hasNext() && attend == null) {

                ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourse.next();
                attend = registration.readAttendByExecutionCourse(executionCourse);
            }
            groupProperties.addAttends(attend);
        }

        return new Boolean(true);
    }
}