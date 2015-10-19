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
package org.fenixedu.academic.domain.log;

import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class StudentRegistrationTransferLog extends StudentRegistrationTransferLog_Base {
    public StudentRegistrationTransferLog(Student source, Student target) {
        super();
        setSource(source);
        setTarget(target);
        setWhen(DateTime.now());
        setWho(Authenticate.getUser().getUsername());
    }

    @Override
    public Student getSource() {
        //FIXME: remove when the framework enables read-only slots
        return super.getSource();
    }

    @Override
    public Student getTarget() {
        //FIXME: remove when the framework enables read-only slots
        return super.getTarget();
    }

    @Override
    public DateTime getWhen() {
        //FIXME: remove when the framework enables read-only slots
        return super.getWhen();
    }

    @Override
    public String getWho() {
        //FIXME: remove when the framework enables read-only slots
        return super.getWho();
    }
}
