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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessWithPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA.ChooseDegreeBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class DegreeChangeIndividualCandidacyDegreesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final Set<AcademicProgram> programs =
                AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES);

        return Collections2.filter(getDegrees(source), new Predicate<Degree>() {
            @Override
            public boolean apply(Degree degree) {
                return programs.contains(degree);
            }
        });
    }

    private Collection<Degree> getDegrees(Object source) {
        if (source instanceof IndividualCandidacyProcessWithPrecedentDegreeInformationBean) {
            IndividualCandidacyProcessWithPrecedentDegreeInformationBean bean =
                    (IndividualCandidacyProcessWithPrecedentDegreeInformationBean) source;

            if (bean.getCandidacyProcess() != null) {
                return bean.getCandidacyProcess().getDegree();
            } else {
                return bean.getIndividualCandidacyProcess().getCandidacyProcess().getDegree();
            }
        }
        if (source instanceof ChooseDegreeBean) {
            final ChooseDegreeBean bean = (ChooseDegreeBean) source;
            final CandidacyProcess candidacyProcess = bean.getCandidacyProcess();
            if (candidacyProcess instanceof DegreeCandidacyForGraduatedPersonProcess) {
                final DegreeCandidacyForGraduatedPersonProcess candidacyForGraduatedPersonProcess =
                        (DegreeCandidacyForGraduatedPersonProcess) candidacyProcess;
                return candidacyForGraduatedPersonProcess.getAvailableDegrees();
            }
        }
        return Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
