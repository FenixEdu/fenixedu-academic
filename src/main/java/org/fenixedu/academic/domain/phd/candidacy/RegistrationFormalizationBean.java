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
package org.fenixedu.academic.domain.phd.candidacy;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.fenixedu.academic.domain.student.Registration;
import org.joda.time.LocalDate;

public class RegistrationFormalizationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PhdProgramCandidacyProcess process;
    private LocalDate whenStartedStudies;

    private boolean selectRegistration;
    private Registration registration;

    public RegistrationFormalizationBean() {
    }

    public RegistrationFormalizationBean(PhdProgramCandidacyProcess process) {
        this.process = process;
    }

    public LocalDate getWhenStartedStudies() {
        return whenStartedStudies;
    }

    public void setWhenStartedStudies(LocalDate whenStartedStudies) {
        this.whenStartedStudies = whenStartedStudies;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public boolean isSelectRegistration() {
        return selectRegistration;
    }

    public void setSelectRegistration(boolean selectRegistration) {
        this.selectRegistration = selectRegistration;
    }

    public Collection<Registration> getAvailableRegistrationsToAssociate() {
        if (process.getPerson().getStudent() == null) {
            return Collections.emptySet();
        }

        if (!process.getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()) {
            final Registration registration =
                    process.getPerson().getStudent()
                            .getActiveRegistrationFor(process.getPhdProgramLastActiveDegreeCurricularPlan());

            if (registration != null) {
                return Collections.singleton(registration);
            }
        } else {
            return process.getPerson().getStudent().getRegistrationsSet();
        }

        return Collections.emptySet();
    }

    public boolean hasRegistration() {
        return registration != null;
    }
}
