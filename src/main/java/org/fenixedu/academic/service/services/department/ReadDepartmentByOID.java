/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 *  
 */
package org.fenixedu.academic.service.services.department;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.dto.InfoDepartment;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Fialho & Rita Ferreira
 */
public class ReadDepartmentByOID {

    @Atomic
    public static InfoDepartment run(String oid) throws FenixServiceException {
        Department department = FenixFramework.getDomainObject(oid);
        return InfoDepartment.newInfoFromDomain(department);
    }
}