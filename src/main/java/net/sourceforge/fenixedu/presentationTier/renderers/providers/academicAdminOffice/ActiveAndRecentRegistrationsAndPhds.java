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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.academicAdminOffice;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.EditCandidacyInformationDA.ChooseRegistrationOrPhd;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.EditCandidacyInformationDA.PhdRegistrationWrapper;

import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ActiveAndRecentRegistrationsAndPhds implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object source, Object current) {
        ChooseRegistrationOrPhd chooseRegistrationOrPhd = (ChooseRegistrationOrPhd) source;
        Student student = chooseRegistrationOrPhd.getStudent();
        Set<PhdRegistrationWrapper> phdRegistrationWrapperResult = new HashSet<PhdRegistrationWrapper>();
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        for (final PhdIndividualProgramProcess phdProcess : student.getPerson().getPhdIndividualProgramProcesses()) {
            if (!phdProcess.isAllowedToManageProcess(Authenticate.getUser())) {
                continue;
            }
            if ((phdProcess.isProcessActive() && student.hasValidInsuranceEvent())) {
                phdRegistrationWrapperResult.add(new PhdRegistrationWrapper(phdProcess));
            } else if (phdProcess.isConcluded()) {
                ExecutionYear conclusionYear = phdProcess.getConclusionYear();
                if (matchesRecentExecutionYear(currentExecutionYear, conclusionYear)) {
                    phdRegistrationWrapperResult.add(new PhdRegistrationWrapper(phdProcess));
                }
            }
        }
        for (Registration registration : student.getActiveRegistrations()) {
            if (!registration.getDegreeType().isEmpty() && registration.isAllowedToManageRegistration()) {
                phdRegistrationWrapperResult.add(new PhdRegistrationWrapper(registration));
            }
        }

        for (Registration registration : student.getConcludedRegistrations()) {
            if (!registration.getDegreeType().isEmpty() && registration.isBolonha()
                    && registration.isAllowedToManageRegistration()) {
                RegistrationConclusionBean conclusionBean = null;
                CycleType cycleType = registration.getDegreeType().getLastOrderedCycleType();
                if (cycleType != null) {
                    conclusionBean = new RegistrationConclusionBean(registration, cycleType);
                } else {
                    conclusionBean = new RegistrationConclusionBean(registration);
                }
                ExecutionYear conclusionYear = conclusionBean.getConclusionYear();
                if (matchesRecentExecutionYear(currentExecutionYear, conclusionYear)) {
                    phdRegistrationWrapperResult.add(new PhdRegistrationWrapper(registration));
                }
            }
        }
        return phdRegistrationWrapperResult;
    }

    private boolean matchesRecentExecutionYear(ExecutionYear currentExecutionYear, ExecutionYear conclusionYear) {
        return conclusionYear == currentExecutionYear || conclusionYear == currentExecutionYear.getPreviousExecutionYear()
                || conclusionYear == currentExecutionYear.getPreviousExecutionYear().getPreviousExecutionYear();
    }
}
