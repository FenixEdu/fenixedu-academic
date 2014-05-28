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
package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni.AlumniNotificationService;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class ValidateAlumniIdentity extends AlumniNotificationService {

    protected void run(AlumniIdentityCheckRequest identityRequest, Boolean approval, Person operator) {
        identityRequest.validate(approval, operator);
        sendIdentityCheckEmail(identityRequest, approval);
    }

    protected void run(AlumniIdentityCheckRequest identityRequest, Person alumniPerson) {
        alumniPerson.setSocialSecurityNumber(identityRequest.getSocialSecurityNumber());
    }

    // Service Invokers migrated from Berserk

    private static final ValidateAlumniIdentity serviceInstance = new ValidateAlumniIdentity();

    @Atomic
    public static void runValidateAlumniIdentity(AlumniIdentityCheckRequest identityRequest, Boolean approval, Person operator) {
        serviceInstance.run(identityRequest, approval, operator);
    }

    @Atomic
    public static void runValidateAlumniIdentity(AlumniIdentityCheckRequest identityRequest, Person alumniPerson) {
        serviceInstance.run(identityRequest, alumniPerson);
    }

}