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
package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;

import org.fenixedu.bennu.core.domain.User;

public abstract class PhdProgramCandidacyProcessActivity extends Activity<PhdProgramCandidacyProcess> {

    @Override
    final public void checkPreConditions(final PhdProgramCandidacyProcess process, final User userView) {
        processPreConditions(process, userView);
        activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdProgramCandidacyProcess process, final User userView) {
    }

    abstract protected void activityPreConditions(final PhdProgramCandidacyProcess process, final User userView);

    @Override
    protected void log(PhdProgramCandidacyProcess process, User userView, Object object) {
        PhdLog.logActivity(this, process, userView, object);
    }

}
