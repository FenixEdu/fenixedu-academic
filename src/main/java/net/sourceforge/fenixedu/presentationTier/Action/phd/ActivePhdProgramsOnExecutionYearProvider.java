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
package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class ActivePhdProgramsOnExecutionYearProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;

        if (bean.getExecutionYear() == null) {
            return Collections.EMPTY_LIST;
        }

        List<PhdProgram> phdProgramsList = new ArrayList<PhdProgram>();

        for (PhdProgram phdProgram : AcademicAuthorizationGroup.getPhdProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_PHD_PROCESSES)) {
            if (phdProgram.isActive(bean.getExecutionYear())) {
                phdProgramsList.add(phdProgram);
            }
        }

        return phdProgramsList;
    }
}
