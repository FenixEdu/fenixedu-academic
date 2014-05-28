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

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class ErasmusIndividualCandidacyProcessExecutedAction extends ErasmusIndividualCandidacyProcessExecutedAction_Base {

    private ErasmusIndividualCandidacyProcessExecutedAction() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ErasmusIndividualCandidacyProcessExecutedAction(MobilityIndividualApplicationProcess process, ExecutedActionType type) {
        this();

        if (type == null) {
            throw new DomainException("error.executed.action.type.mandatory");
        }

        if (process == null) {
            throw new DomainException("error.executed.action.process.mandatory");
        }

        init(type);
        setMobilityIndividualApplicationProcess(process);
    }

    public boolean isSentEmailForRequiredDocumentsExecutedAction() {
        return ExecutedActionType.SENT_EMAIL_FOR_MISSING_REQUIRED_DOCUMENTS.equals(getType());
    }

    @Deprecated
    public boolean hasMobilityIndividualApplicationProcess() {
        return getMobilityIndividualApplicationProcess() != null;
    }

}
