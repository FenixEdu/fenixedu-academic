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
package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class RoleOperationLog extends RoleOperationLog_Base {

    public RoleOperationLog() {
        super();
    }

    public RoleOperationLog(Role role, Person person, Person whoGranted, RoleOperationType operationType) {
        setOperationType(operationType);
        setLogDate(new DateTime());
        setRole(role);
        setIstUsername(person.getIstUsername());
        setPerson(person);
        if (whoGranted != null) {
            setWhoGrantedIstUsername(whoGranted.getIstUsername());
            setPersonWhoGranted(whoGranted);
        } else {
            setWhoGrantedIstUsername("No user");
        }
    }

    protected Bennu getRootDomainObject() {
        return getRole() != null ? getRole().getRootDomainObject() : null;
    }

}
