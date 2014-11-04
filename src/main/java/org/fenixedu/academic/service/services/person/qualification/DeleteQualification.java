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
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package org.fenixedu.academic.service.services.person.qualification;

import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteQualification {

    @Atomic
    public static void run(String qualificationId) {
        Qualification qualification = FenixFramework.getDomainObject(qualificationId);
        qualification.delete();
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static void runDeleteQualification(String qualificationId) throws NotAuthorizedException {
        run(qualificationId);
    }

}