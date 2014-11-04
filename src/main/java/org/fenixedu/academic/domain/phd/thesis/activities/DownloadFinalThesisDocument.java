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
/**
 * 
 */
package org.fenixedu.academic.domain.phd.thesis.activities;

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.bennu.core.domain.User;

public class DownloadFinalThesisDocument extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (process.hasFinalThesisDocument() && PhdThesisProcess.isParticipant(process, userView)) {
            return;
        }

        throw new PreConditionNotValidException();

    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        // nothing to be done
        return null;
    }
}