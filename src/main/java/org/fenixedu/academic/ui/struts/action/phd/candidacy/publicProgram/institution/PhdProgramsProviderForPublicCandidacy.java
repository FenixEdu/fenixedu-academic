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
import java.util.List;

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessBean;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramsProviderForPublicCandidacy extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
        // return fromFocusArea(source);

        if (source instanceof PhdProgramCandidacyProcessBean) {
            PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;
            InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) bean.getPhdCandidacyPeriod();

            return phdCandidacyPeriod.getPhdProgramsSet();

        } else if (source instanceof PhdIndividualProgramProcessBean) {
            PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) source;
            InstitutionPhdCandidacyPeriod publicPhdCandidacyPeriod =
                    (InstitutionPhdCandidacyPeriod) bean.getIndividualProgramProcess().getCandidacyProcess()
                            .getPublicPhdCandidacyPeriod();

            return publicPhdCandidacyPeriod.getPhdProgramsSet();
        }

        return Collections.EMPTY_LIST;
    }

    protected Object fromFocusArea(Object source) {
        PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;
        List<PhdProgram> activePhdProgramList = new ArrayList<PhdProgram>();
        InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) bean.getPhdCandidacyPeriod();

        if (bean.getFocusArea() == null) {
            return Collections.EMPTY_LIST;
        }

        for (PhdProgram phdProgram : bean.getFocusArea().getPhdProgramsSet()) {
            if (phdCandidacyPeriod.getPhdProgramsSet().contains(phdProgram)) {
                activePhdProgramList.add(phdProgram);
            }
        }

        return activePhdProgramList;
    }

}
