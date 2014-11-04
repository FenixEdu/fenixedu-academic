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
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package org.fenixedu.academic.service.services.resourceAllocationManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.predicate.RolePredicates;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ApagarTurma {

    @Atomic
    public static Boolean run(InfoClass infoClass) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        FenixFramework.<SchoolClass> getDomainObject(infoClass.getExternalId()).delete();
        return Boolean.TRUE;
    }

}