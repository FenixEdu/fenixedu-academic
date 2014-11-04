/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil;

import org.fenixedu.academic.dto.coordinator.tutor.StudentsByEntryYearBean;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.util.Month;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

/**
 * Class for initializing several types of beans
 * 
 * Dependency Injection
 * 
 * @author jaime
 */
public class BeanInitializer {

    /**
     * Method for StudentsByEntryYearBean
     * 
     * @param studentsByEntryYearBean
     * @param tutorBean
     * @param contextBean
     * @param selectedPersons
     * @param tutorshipDuration
     */
    public static void initializeBean(StudentsByEntryYearBean studentsByEntryYearBean, TeacherTutorshipCreationBean tutorBean,
            ContextTutorshipCreationBean contextBean, String[] selectedPersons, int tutorshipDuration) {
        ExecutionDegree executionDegree = contextBean.getExecutionDegree();
        studentsByEntryYearBean.receiveStudentsToCreateTutorshipList(selectedPersons, executionDegree);
        studentsByEntryYearBean.setTeacher(tutorBean.getTeacher().getTeacher());
        DateTime today = new DateTime();
        studentsByEntryYearBean.setTutorshipEndMonth(Month.fromDateTime(today));
        studentsByEntryYearBean.setTutorshipEndYear(today.getYear() + tutorshipDuration);
    }

    /**
     * Method for different parameters
     * 
     * @param studentsByEntryYearBean
     * @param teacher
     * @param executionDegree
     * @param personStudent
     * @param endDate
     */
    public static void initializeBean(StudentsByEntryYearBean studentsByEntryYearBean, Teacher teacher,
            ExecutionDegree executionDegree, Person personStudent, Partial endDate) {
        // TODO Auto-generated method stub
        String[] persons = { personStudent.getExternalId().toString() };
        studentsByEntryYearBean.receiveStudentsToCreateTutorshipList(persons, executionDegree);
        studentsByEntryYearBean.setTeacher(teacher);
        int month = endDate.get(DateTimeFieldType.monthOfYear());
        int year = endDate.get(DateTimeFieldType.year());
        studentsByEntryYearBean.setTutorshipEndMonth(Month.fromInt(month));
        studentsByEntryYearBean.setTutorshipEndYear(year);
    }

}
