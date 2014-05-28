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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ErasmusCandidacyProcessExecutedAction extends ErasmusCandidacyProcessExecutedAction_Base {

    protected ErasmusCandidacyProcessExecutedAction() {
        super();
    }

    protected ErasmusCandidacyProcessExecutedAction(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
            List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses) {
        this();
        init(type, applicationProcess, subjectCandidacyProcesses);
    }

    protected void init(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
            List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses) {
        super.init(type);
        if (applicationProcess == null) {
            throw new DomainException("error.erasmus.candidacy.process.executed.action.candidacy,process.is.null", null);
        }

        setMobilityApplicationProcess(applicationProcess);
        getSubjectMobilityIndividualApplicationProcess().addAll(subjectCandidacyProcesses);
    }

    public boolean isReceptionEmailExecutedAction() {
        return ExecutedActionType.SENT_RECEPTION_EMAIL.equals(getType());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess> getSubjectMobilityIndividualApplicationProcess() {
        return getSubjectMobilityIndividualApplicationProcessSet();
    }

    @Deprecated
    public boolean hasAnySubjectMobilityIndividualApplicationProcess() {
        return !getSubjectMobilityIndividualApplicationProcessSet().isEmpty();
    }

    @Deprecated
    public boolean hasMobilityApplicationProcess() {
        return getMobilityApplicationProcess() != null;
    }

}
