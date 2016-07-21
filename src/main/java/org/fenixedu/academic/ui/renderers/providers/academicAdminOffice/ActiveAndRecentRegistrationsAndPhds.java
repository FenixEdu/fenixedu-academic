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
package org.fenixedu.academic.ui.renderers.providers.academicAdminOffice;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.EditCandidacyInformationDA.ChooseRegistrationOrPhd;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.EditCandidacyInformationDA.PhdRegistrationWrapper;
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

        for (final PhdIndividualProgramProcess phdProcess : student.getPerson().getPhdIndividualProgramProcessesSet()) {
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

        for (Registration concludedRegistration : student.getConcludedRegistrations()) {
            if (!concludedRegistration.getDegreeType().isEmpty() && concludedRegistration.isBolonha()
                    && concludedRegistration.isAllowedToManageRegistration()) {

                ProgramConclusion
                        .conclusionsFor(concludedRegistration)
                        .filter(ProgramConclusion::isTerminal)
                        .forEach(
                                programConclusion -> {
                                    RegistrationConclusionBean conclusionBean =
                                            new RegistrationConclusionBean(concludedRegistration, programConclusion);
                                    if(conclusionBean.isConcluded()) {
                                        ExecutionYear conclusionYear = conclusionBean.getConclusionYear();

                                        if (matchesRecentExecutionYear(currentExecutionYear, conclusionYear)) {
                                            phdRegistrationWrapperResult.add(new PhdRegistrationWrapper(concludedRegistration));
                                        }
                                    }
                                });
            }
        }
        return phdRegistrationWrapperResult;
    }

    private boolean matchesRecentExecutionYear(ExecutionYear currentExecutionYear, ExecutionYear conclusionYear) {
        return conclusionYear == currentExecutionYear || conclusionYear == currentExecutionYear.getPreviousExecutionYear()
                || conclusionYear == currentExecutionYear.getPreviousExecutionYear().getPreviousExecutionYear();
    }
}
