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
package org.fenixedu.academic.domain.phd.individualProcess.activities;

import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.log.PhdLog;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.messaging.core.domain.Message;

public abstract class PhdIndividualProgramProcessActivity extends Activity<PhdIndividualProgramProcess> {

    @Override
    final public void checkPreConditions(final PhdIndividualProgramProcess process, final User userView) {
        processPreConditions(process, userView);
        activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdIndividualProgramProcess process, final User userView) {
        if (process != null && !process.getActiveState().isActive()) {
            throw new PreConditionNotValidException();
        }
    }

    protected void email(String email, String subject, String body) {
        Message.fromSystem()
                .singleBcc(email)
                .subject(subject)
                .textBody(body)
                .send();
    }

    abstract protected void activityPreConditions(final PhdIndividualProgramProcess process, final User userView);

    @Override
    protected void log(PhdIndividualProgramProcess process, User userView, Object object) {
        PhdLog.logActivity(this, process, userView, object);
    }

}
