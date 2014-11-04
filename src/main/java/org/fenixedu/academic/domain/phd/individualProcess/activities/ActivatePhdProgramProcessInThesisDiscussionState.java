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
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessBean;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.PhdProgramProcessState;
import org.fenixedu.bennu.core.domain.User;

public class ActivatePhdProgramProcessInThesisDiscussionState extends PhdIndividualProgramProcessActivity {
    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, User userView) {
        // remove restrictions
    }

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcessState(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) object;

        PhdProgramProcessState.createWithGivenStateDate(process, PhdIndividualProgramProcessState.THESIS_DISCUSSION,
                userView.getPerson(), "", bean.getStateDate().toDateTimeAtStartOfDay());

        /*
         * If the program is associated to a registration we check if it is
         * concluded
         */

        if (process.getRegistration() == null) {
            return process;
        }

        if (!process.getRegistration().isConcluded() && !process.getRegistration().isSchoolPartConcluded()) {
            throw new DomainException(
                    "error.PhdIndividualProgramProcess.set.thesis.discussion.state.registration.is.not.concluded");
        }

        return process;
    }
}