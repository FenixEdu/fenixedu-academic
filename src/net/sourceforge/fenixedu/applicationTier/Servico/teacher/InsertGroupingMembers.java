/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
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

public class InsertGroupingMembers extends Service {

    public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, List studentCodes)
            throws FenixServiceException, ExcepcaoPersistencia {

        List students = new ArrayList();

        Grouping groupProperties = rootDomainObject.readGroupingByOID(
                groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        Iterator iterator = studentCodes.iterator();

        while (iterator.hasNext()) {
            Registration student = rootDomainObject.readRegistrationByOID((Integer) iterator
                    .next());
            students.add(student);
        }

        Iterator iterAttends = groupProperties.getAttends().iterator();

        while (iterAttends.hasNext()) {
            Attends existingAttend = (Attends) iterAttends.next();
            Registration existingAttendStudent = existingAttend.getAluno();

            Iterator iteratorStudents = students.iterator();

            while (iteratorStudents.hasNext()) {

                Registration student = (Registration) iteratorStudents.next();
                if (student.equals(existingAttendStudent)) {
                    throw new InvalidSituationServiceException();
                }
            }
        }

        Iterator iterStudents1 = students.iterator();

        while (iterStudents1.hasNext()) {
            Attends attend = null;
            Registration student = (Registration) iterStudents1.next();

            List listaExecutionCourses = new ArrayList();
            listaExecutionCourses.addAll(groupProperties.getExecutionCourses());
            Iterator iterExecutionCourse = listaExecutionCourses.iterator();
            while (iterExecutionCourse.hasNext() && attend == null) {
                ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourse.next();
                attend = student.readAttendByExecutionCourse(executionCourse);
            }
            groupProperties.addAttends(attend);
        }

        return Boolean.TRUE;
    }
}