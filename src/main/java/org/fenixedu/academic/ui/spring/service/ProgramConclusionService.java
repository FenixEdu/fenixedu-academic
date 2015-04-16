package org.fenixedu.academic.ui.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.accounting.EventTypes;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
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
            boolean isAlumniProvider, boolean isSkipValidation, RegistrationStateType targetState) {
        programConclusion.edit(name, description, graduationTitle, graduationLevel, isAverageEditable, isAlumniProvider,
                isSkipValidation, targetState, programConclusion.getEventTypes());
    }

    @Atomic
    public void delete(ProgramConclusion programConclusion) {
        programConclusion.delete();
    }

}
