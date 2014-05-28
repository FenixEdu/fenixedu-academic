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
package net.sourceforge.fenixedu.domain.phd.migration.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PhdThesisActivity;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

public class SkipThesisJuryActivities extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (process.getActiveState() != PhdThesisProcessStateType.NEW) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

        final PhdThesisProcessBean thesisBean = (PhdThesisProcessBean) object;

        process.setWhenJuryDesignated(process.getIndividualProgramProcess().getCandidacyDate());
        LocalDate candidacyDate = process.getIndividualProgramProcess().getCandidacyDate();
        process.setWhenJuryValidated(candidacyDate);

        process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK, userView.getPerson(),
                thesisBean.getRemarks());

        if (!process.hasMeetingProcess()) {
            Process.createNewProcess(userView, PhdMeetingSchedulingProcess.class, thesisBean);
        }

        return process;
    }

}
