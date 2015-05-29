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
package org.fenixedu.academic.ui.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.EventTypes;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.Atomic;

/***
 * Program Conclusion Service
 * 
 * This service provides methods to manage program conclusions.
 * 
 * @author Sérgio Silva (sergio.silva@tecnico.ulisboa.pt)
 *
 */

@Service
public class ProgramConclusionService {

    private final Set<EventType> eventTypes;

    public ProgramConclusionService() {
        eventTypes = new TreeSet<EventType>();
        eventTypes.addAll(DegreeFinalizationCertificateRequest.getPossibleEventTypes());
        eventTypes.addAll(DiplomaRequest.getPossibleEventTypes());
        eventTypes.addAll(RegistryDiplomaRequest.getPossibleEventTypes());
    }

    public List<ProgramConclusion> getProgramConclusions() {
        return new ArrayList<>(Bennu.getInstance().getProgramConclusionSet());
    }

    @Atomic
    public ProgramConclusion createProgramConclusion(LocalizedString name, LocalizedString description,
            LocalizedString graduationTitle, LocalizedString graduationLevel, boolean isAverageEditable,
            boolean isAlumniProvider, boolean isSkipValidation, RegistrationStateType targetState, EventTypes eventTypes) {
        return new ProgramConclusion(name, description, graduationTitle, graduationLevel, isAverageEditable, isAlumniProvider,
                isSkipValidation, targetState, eventTypes);
    }

    @Atomic
    public void editProgramConclusion(ProgramConclusion programConclusion, LocalizedString name, LocalizedString description,
            LocalizedString graduationTitle, LocalizedString graduationLevel, boolean isAverageEditable,
            boolean isAlumniProvider, boolean isSkipValidation, RegistrationStateType targetState, EventTypes eventTypes) {
        programConclusion.edit(name, description, graduationTitle, graduationLevel, isAverageEditable, isAlumniProvider,
                isSkipValidation, targetState, eventTypes);
    }

    @Atomic
    public void delete(ProgramConclusion programConclusion) {
        programConclusion.delete();
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }
}
