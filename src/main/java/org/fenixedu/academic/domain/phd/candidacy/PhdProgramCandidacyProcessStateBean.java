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

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramCandidacyProcessState;

public class PhdProgramCandidacyProcessStateBean implements Serializable {

    private static final long serialVersionUID = 4435060583355083376L;

    private PhdProgramCandidacyProcessState state;
    private String remarks;
    private Boolean generateAlert;

    public PhdProgramCandidacyProcessStateBean() {
        super();
    }

    public PhdProgramCandidacyProcessStateBean(final PhdIndividualProgramProcess process) {
        this();
        setGenerateAlert(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());
    }

    public PhdProgramCandidacyProcessState getState() {
        return state;
    }

    public void setState(PhdProgramCandidacyProcessState state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getGenerateAlert() {
        return generateAlert;
    }

    public void setGenerateAlert(Boolean generateAlert) {
        this.generateAlert = generateAlert;
    }
}
