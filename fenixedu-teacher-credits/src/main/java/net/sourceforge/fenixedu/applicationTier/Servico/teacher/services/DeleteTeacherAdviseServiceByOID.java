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
/**
 * Nov 29, 2005
 */
package org.fenixedu.academic.service.services.teacher.services;

import org.fenixedu.academic.service.filter.DepartmentAdministrativeOfficeAuthorizationFilter;
import org.fenixedu.academic.service.filter.DepartmentMemberAuthorizationFilter;
import org.fenixedu.academic.service.filter.ScientificCouncilAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.teacher.TeacherAdviseService;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteTeacherAdviseServiceByOID {

    protected void run(String teacherAdviseServiceID, RoleType roleType) {
        TeacherAdviseService teacherAdviseService = (TeacherAdviseService) FenixFramework.getDomainObject(teacherAdviseServiceID);
        teacherAdviseService.delete(roleType);
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTeacherAdviseServiceByOID serviceInstance = new DeleteTeacherAdviseServiceByOID();

    @Atomic
    public static void runDeleteTeacherAdviseServiceByOID(String teacherAdviseServiceID, RoleType roleType)
            throws NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(teacherAdviseServiceID, roleType);
        } catch (NotAuthorizedException ex1) {
            try {
                DepartmentMemberAuthorizationFilter.instance.execute();
                serviceInstance.run(teacherAdviseServiceID, roleType);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(teacherAdviseServiceID, roleType);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}