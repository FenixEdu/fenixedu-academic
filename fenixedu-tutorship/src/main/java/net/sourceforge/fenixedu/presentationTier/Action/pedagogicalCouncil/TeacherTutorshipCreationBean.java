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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.TutorshipIntention;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * Class TeacherTutorshipCreationBean.java
 * 
 * @author jaime created on Aug 26, 2010
 */

public class TeacherTutorshipCreationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Person teacher;
    private ExecutionDegree executionDegree;

    public TeacherTutorshipCreationBean(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public static class TutorsProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        /**
         * provide all teachers in the departments that teach that course
         */
        @Override
        public Object provide(Object source, Object current) {
            TeacherTutorshipCreationBean bean = (TeacherTutorshipCreationBean) source;
            List<Person> teachers = new ArrayList<Person>();
            if (bean.getExecutionDegree() != null) {
                ExecutionDegree executionDegree = bean.getExecutionDegree();
                for (final Teacher teacher : Tutorship.getPossibleTutorsFromExecutionDegreeDepartments(executionDegree)) {
                    if (hasTutorshipIntentionFor(teacher, bean.getExecutionDegree())) {
                        teachers.add(teacher.getPerson());
                    }
                }
            }
            return teachers;
        }

        private static boolean hasTutorshipIntentionFor(Teacher teacher, ExecutionDegree executionDegree) {
            for (TutorshipIntention intention : teacher.getTutorshipIntentionSet()) {
                if (intention.getAcademicInterval().equals(executionDegree.getAcademicInterval())
                        && intention.getDegreeCurricularPlan().equals(executionDegree.getDegreeCurricularPlan())) {
                    return true;
                }
            }
            return false;
        }
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

}
