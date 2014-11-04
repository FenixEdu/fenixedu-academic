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

import org.fenixedu.academic.domain.candidacy.FirstTimeCandidacyStage;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class FirstTimeCandidacyLog extends FirstTimeCandidacyLog_Base {

    public FirstTimeCandidacyLog() {
        super();
    }

    public FirstTimeCandidacyLog(StudentCandidacy candidacy) {
        super();
        this.setStudentCandidacy(candidacy);
    }

    public void addEntry(FirstTimeCandidacyStage stage, DateTime timestamp) {
        this.addFirstTimeCandidacyLogEntry(new FirstTimeCandidacyLogEntry(stage, timestamp, this));
    }

    protected Bennu getRootDomainObject() {
        return Bennu.getInstance();
    }

}
