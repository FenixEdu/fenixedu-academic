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
package org.fenixedu.academic.ui.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.dto.student.IRegistrationBean;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

public class ProgramConclusionProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        if (source instanceof DocumentRequestCreateBean) {
            return provide(((DocumentRequestCreateBean) source).getRegistration(), currentValue);
        }

        if (source instanceof IRegistrationBean) {
            return provide(((IRegistrationBean) source).getRegistration(), currentValue);
        }

        if (source instanceof Registration) {
            return ProgramConclusion.conclusionsFor((Registration) source).collect(Collectors.toList());
        }

        if (source instanceof DegreeCurricularPlan) {
            return ProgramConclusion.conclusionsFor((DegreeCurricularPlan) source).collect(Collectors.toList());
        }

        return new ArrayList<ProgramConclusion>();
    }
}
