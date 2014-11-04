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
 * Created on 16/Set/2003
 */
package org.fenixedu.academic.service.services.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.service.filter.ManagerAuthorizationFilter;
import org.fenixedu.academic.service.filter.ScientificCouncilAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTeachers {

    /**
     * Executes the service. Returns the current collection of infoTeachers.
     * 
     * @throws ExcepcaoPersistencia
     */

    protected List<InfoTeacher> run(String executionCourseId) throws FenixServiceException {

        Collection professorShips = null;
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        professorShips = executionCourse.getProfessorshipsSet();

        if (professorShips == null || professorShips.isEmpty()) {
            return null;
        }

        List<InfoTeacher> infoTeachers = new ArrayList<InfoTeacher>();
        Iterator iter = professorShips.iterator();
        Teacher teacher = null;

        while (iter.hasNext()) {
            teacher = ((Professorship) iter.next()).getTeacher();
            infoTeachers.add(InfoTeacher.newInfoFromDomain(teacher));
        }

        return infoTeachers;
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionCourseTeachers serviceInstance = new ReadExecutionCourseTeachers();

    @Atomic
    public static List<InfoTeacher> runReadExecutionCourseTeachers(String executionCourseId) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionCourseId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}