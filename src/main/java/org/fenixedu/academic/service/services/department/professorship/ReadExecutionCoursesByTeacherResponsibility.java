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
/*
 * Created on Aug 26, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 * 
 */
public class ReadExecutionCoursesByTeacherResponsibility {

    @Atomic
    public static List run(String id) throws FenixServiceException {

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Person person = Person.readPersonByUsername(id);
        if (person.getTeacher() != null) {
            Teacher teacher = person.getTeacher();

            final List<Professorship> responsibilities = teacher.responsibleFors();

            if (responsibilities != null) {
                for (final Professorship professorship : responsibilities) {
                    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(professorship.getExecutionCourse()));
                }
            }
            return infoExecutionCourses;
        } else {
            return new ArrayList<Professorship>();
        }
    }
}