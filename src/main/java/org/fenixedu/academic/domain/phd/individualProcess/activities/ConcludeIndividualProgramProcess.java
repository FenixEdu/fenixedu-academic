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

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.PhdProgramProcessState;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class ConcludeIndividualProgramProcess extends PhdIndividualProgramProcessActivity {

    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, User userView) {
    }

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcessState(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        if (process.getThesisProcess() == null) {
            throw new PhdDomainOperationException("error.PhdIndividualProgramProcess.thesis.process.inexistent");
        }

        if (!process.getThesisProcess().isConcluded()) {
            throw new PhdDomainOperationException("error.PhdIndividualProgramProcess.thesis.process.is.not.concluded");
        }

        PhdProgramProcessState.createWithGivenStateDate(process, PhdIndividualProgramProcessState.CONCLUDED,
                userView.getPerson(), "", new DateTime());

        return process;
    }

}
