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
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * Base class for authorization issues on credits information edition done by
 * department members.
 * 
 * @author jpvl
 */
public abstract class AbstractTeacherDepartmentAuthorization<T> extends Filtro {

    public void execute(T object) throws FenixServiceException {
        User requester = Authenticate.getUser();
        if ((requester == null) || !requester.getPerson().hasRole(RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            throw new NotAuthorizedException();
        }

        String teacherId = getTeacherId(object);
        if (teacherId != null) {

            final Person requesterPerson = requester.getPerson();

            Teacher teacher = FenixFramework.getDomainObject(teacherId);

            Department teacherDepartment = teacher.getCurrentWorkingDepartment();

            Collection departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();

            if (!departmentsWithAccessGranted.contains(teacherDepartment)) {
                throw new NotAuthorizedException();
            }
        }

    }

    protected abstract String getTeacherId(T object) throws FenixServiceException;
}