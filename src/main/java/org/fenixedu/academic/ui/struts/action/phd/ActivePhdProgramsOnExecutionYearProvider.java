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
package org.fenixedu.academic.ui.struts.action.phd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;
import org.fenixedu.bennu.core.security.Authenticate;

public class ActivePhdProgramsOnExecutionYearProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;

        if (bean.getExecutionYear() == null) {
            return Collections.EMPTY_LIST;
        }

        List<PhdProgram> phdProgramsList = new ArrayList<PhdProgram>();

        for (PhdProgram phdProgram : AcademicAccessRule.getPhdProgramsAccessibleToFunction(
                AcademicOperationType.MANAGE_PHD_PROCESSES, Authenticate.getUser()).collect(Collectors.toSet())) {
            if (phdProgram.isActive(bean.getExecutionYear())) {
                phdProgramsList.add(phdProgram);
            }
        }

        return phdProgramsList;
    }
}
