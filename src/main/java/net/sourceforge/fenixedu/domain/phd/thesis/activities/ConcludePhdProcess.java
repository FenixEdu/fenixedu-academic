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
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

import org.fenixedu.bennu.core.domain.User;

public class ConcludePhdProcess extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isConcluded()) {
            throw new PreConditionNotValidException();
        }

        if (process.getIndividualProgramProcess().hasRegistration()
                && !process.getIndividualProgramProcess().getRegistration().isRegistrationConclusionProcessed()) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        PhdConclusionProcessBean bean = (PhdConclusionProcessBean) object;
        PhdConclusionProcess.create(bean, userView.getPerson());

        PhdIndividualProgramProcess individualProgramProcess = process.getIndividualProgramProcess();

        if (!PhdIndividualProgramProcessState.CONCLUDED.equals(individualProgramProcess.getActiveState())) {
            individualProgramProcess.createState(PhdIndividualProgramProcessState.CONCLUDED, userView.getPerson(), "");
        }

        process.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);

        return process;
    }

}
