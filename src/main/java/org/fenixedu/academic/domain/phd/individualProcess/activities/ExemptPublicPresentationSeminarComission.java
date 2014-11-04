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
import org.fenixedu.academic.domain.phd.alert.PhdAlert;
import org.fenixedu.academic.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcess;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcessStateType;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

public class ExemptPublicPresentationSeminarComission extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (process.getSeminarProcess() != null || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
        bean.setPresentationRequestDate(new LocalDate());
        bean.setPhdIndividualProgramProcess(process);

        final PublicPresentationSeminarProcess seminarProcess =
                Process.createNewProcess(userView, PublicPresentationSeminarProcess.class, object);

        seminarProcess.createState(PublicPresentationSeminarProcessStateType.EXEMPTED, userView.getPerson(), "");

        discardPublicSeminarAlerts(process);

        return process;
    }

    private void discardPublicSeminarAlerts(final PhdIndividualProgramProcess process) {
        for (final PhdAlert alert : process.getActiveAlerts()) {
            if (alert instanceof PhdPublicPresentationSeminarAlert) {
                alert.discard();
            }
        }
    }

}