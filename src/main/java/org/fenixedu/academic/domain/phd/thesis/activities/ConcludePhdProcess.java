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
package org.fenixedu.academic.domain.phd.thesis.activities;

import java.util.Optional;

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.conclusion.PhdConclusionProcess;
import org.fenixedu.academic.domain.phd.conclusion.PhdConclusionProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserLoginPeriod;

public class ConcludePhdProcess extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isConcluded()) {
            throw new PreConditionNotValidException();
        }

        if (!isStudentCurricularPlanFinishedForPhd(process)) {
            throw new PreConditionNotValidException();
        }
    }

    /**
     * TODO: phd-refactor
     * 
     * Check if the root group of the phd student curricular plan is concluded is enough
     * to be able to conclude the phd process.
     * 
     * This should be changed when the phd process becomes itself a curriculum group.
     * 
     * @param process the student's thesis process
     * @return true if the root curriculum group has a conclusion process.
     */
    private boolean isStudentCurricularPlanFinishedForPhd(PhdThesisProcess process) {
        return Optional.ofNullable(process.getIndividualProgramProcess().getRegistration())
                .map(r -> r.getLastStudentCurricularPlan().getRoot().isConclusionProcessed()).orElse(null);
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        PhdConclusionProcessBean bean = (PhdConclusionProcessBean) object;
        PhdConclusionProcess.create(bean, userView.getPerson());

        PhdIndividualProgramProcess individualProgramProcess = process.getIndividualProgramProcess();

        if (!PhdIndividualProgramProcessState.CONCLUDED.equals(individualProgramProcess.getActiveState())) {
            individualProgramProcess.createState(PhdIndividualProgramProcessState.CONCLUDED, userView.getPerson(), "");
        }
        UserLoginPeriod.createOpenPeriod(process.getPerson().getUser());

        return process;
    }

}
