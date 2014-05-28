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
package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.domain.User;

public class DeleteQualification extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, User userView) {
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        final Qualification qualification = (Qualification) object;
        if (process.getPerson().getAssociatedQualificationsSet().contains(qualification)) {
            if (!canDelete(qualification, process, userView != null ? userView.getPerson() : null)) {
                throw new DomainException("error.PhdIndividualProgramProcess.DeleteQualification.not.authorized");
            }
            qualification.delete();
        }
        return process;
    }

    private boolean canDelete(final Qualification qualification, final PhdIndividualProgramProcess process, final Person person) {
        if (!qualification.hasCreator()) {
            return process.getCandidacyProcess().isPublicCandidacy();
        }
        final Person creator = qualification.getCreator();
        return creator == person
                || AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESS_STATE)
                        .isMember(creator.getUser());
    }
}