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
package org.fenixedu.academic.ui.struts.action.phd.candidacy.publicProgram.institution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessBean;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramFocusArea;
import org.fenixedu.academic.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramFocusAreasProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {

        if (source instanceof PhdProgramCandidacyProcessBean) {
            PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;

            Set<PhdProgramFocusArea> focusAreaSet = new HashSet<PhdProgramFocusArea>();
            InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) bean.getPhdCandidacyPeriod();

            for (PhdProgram phdProgram : phdCandidacyPeriod.getPhdProgramsSet()) {
                focusAreaSet.addAll(phdProgram.getPhdProgramFocusAreasSet());
            }

            List<PhdProgramFocusArea> focusAreaList = new ArrayList<PhdProgramFocusArea>();
            focusAreaList.addAll(focusAreaSet);

            return focusAreaList;

        } else if (source instanceof PhdIndividualProgramProcessBean) {
            PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) source;
            Set<PhdProgramFocusArea> focusAreaSet = new HashSet<PhdProgramFocusArea>();

            InstitutionPhdCandidacyPeriod phdCandidacyPeriod =
                    (InstitutionPhdCandidacyPeriod) bean.getIndividualProgramProcess().getCandidacyProcess()
                            .getPublicPhdCandidacyPeriod();

            for (PhdProgram phdProgram : phdCandidacyPeriod.getPhdProgramsSet()) {
                focusAreaSet.addAll(phdProgram.getPhdProgramFocusAreasSet());
            }

            List<PhdProgramFocusArea> focusAreaList = new ArrayList<PhdProgramFocusArea>();
            focusAreaList.addAll(focusAreaSet);

            return focusAreaList;
        }

        return Collections.EMPTY_LIST;
    }

}
