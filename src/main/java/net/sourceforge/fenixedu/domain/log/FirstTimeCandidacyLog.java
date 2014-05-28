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
package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.candidacy.FirstTimeCandidacyStage;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;

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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.FirstTimeCandidacyLogEntry> getFirstTimeCandidacyLogEntry() {
        return getFirstTimeCandidacyLogEntrySet();
    }

    @Deprecated
    public boolean hasAnyFirstTimeCandidacyLogEntry() {
        return !getFirstTimeCandidacyLogEntrySet().isEmpty();
    }

    @Deprecated
    public boolean hasStudentCandidacy() {
        return getStudentCandidacy() != null;
    }

}
