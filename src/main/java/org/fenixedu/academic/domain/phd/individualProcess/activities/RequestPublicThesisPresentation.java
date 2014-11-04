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
import org.fenixedu.academic.domain.caseHandling.Process;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.bennu.core.domain.User;

public class RequestPublicThesisPresentation extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {

        if (process.getSeminarProcess() == null
                || (!process.getSeminarProcess().isExempted() && !process.getSeminarProcess().isConcluded())) {
            throw new PreConditionNotValidException();
        }

        if (process.getThesisProcess() != null || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess individualProcess, User userView,
            Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        bean.setProcess(individualProcess);

        Process.createNewProcess(userView, PhdThesisProcess.class, bean);
        individualProcess.createState(PhdIndividualProgramProcessState.THESIS_DISCUSSION, userView.getPerson(), "");

        return individualProcess;
    }

}