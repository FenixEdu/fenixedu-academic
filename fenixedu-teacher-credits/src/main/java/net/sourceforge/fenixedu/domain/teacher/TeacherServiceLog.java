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
package org.fenixedu.academic.domain.teacher;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.predicate.AccessControl;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class TeacherServiceLog extends TeacherServiceLog_Base implements Comparable<TeacherServiceLog> {

    public TeacherServiceLog(final TeacherService teacherService, final String description) {
        super();
        setModificationDate(new DateTime());
        setTeacherService(teacherService);
        final Person person = AccessControl.getPerson();
        final User user = person == null ? null : person.getUser();
        setUser(user);
        setDescription(description);
    }

    public void delete() {
        setTeacherService(null);
        setRootDomainObject(null);
        setUser(null);
        super.deleteDomainObject();
    }

    @Override
    public int compareTo(final TeacherServiceLog o) {
        if (o == null) {
            return -1;
        }
        int compareTo = getModificationDate().compareTo(o.getModificationDate());
        return compareTo == 0 ? getExternalId().compareTo(o.getExternalId()) : compareTo;
    }

}
