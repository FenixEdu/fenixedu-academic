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

import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateTypeEnum;
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

    @Deprecated
    private final Set<Object> eventTypes;

    public ProgramConclusionService() {
        eventTypes = new TreeSet<>();
    }

    public List<ProgramConclusion> getProgramConclusions() {
        return new ArrayList<>(Bennu.getInstance().getProgramConclusionSet());
    }

    @Atomic
    public ProgramConclusion createProgramConclusion(LocalizedString name, LocalizedString description,
            LocalizedString graduationTitle, LocalizedString graduationLevel, boolean isAverageEditable, boolean isAlumniProvider,
            boolean isSkipValidation, RegistrationStateTypeEnum targetState) {
        return new ProgramConclusion(name, description, graduationTitle, graduationLevel, isAverageEditable, isAlumniProvider,
                isSkipValidation, targetState);
    }

    @Atomic
    public void editProgramConclusion(ProgramConclusion programConclusion, LocalizedString name, LocalizedString description,
            LocalizedString graduationTitle, LocalizedString graduationLevel, boolean isAverageEditable, boolean isAlumniProvider,
            boolean isSkipValidation, RegistrationStateTypeEnum targetState) {
        programConclusion.edit(name, description, graduationTitle, graduationLevel, isAverageEditable, isAlumniProvider,
                isSkipValidation, targetState);
    }

    @Atomic
    public void delete(ProgramConclusion programConclusion) {
        programConclusion.delete();
    }

    @Deprecated
    public Set<Object> getEventTypes() {
        return eventTypes;
    }
}
