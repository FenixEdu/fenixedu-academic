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
package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

public class PhdConfigurationIndividualProgramProcessBean implements Serializable {
    private Boolean generateAlerts;
    private Boolean migratedProcess;
    private Boolean isBolonha;

    public PhdConfigurationIndividualProgramProcessBean() {
    }

    public PhdConfigurationIndividualProgramProcessBean(final PhdIndividualProgramProcess process) {
        setGenerateAlerts(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());
        setMigratedProcess(process.getPhdConfigurationIndividualProgramProcess().getMigratedProcess());
        setIsBolonha(process.getPhdConfigurationIndividualProgramProcess().getIsBolonha());
    }

    public Boolean getGenerateAlerts() {
        return generateAlerts;
    }

    public void setGenerateAlerts(Boolean generateAlerts) {
        this.generateAlerts = generateAlerts;
    }

    public Boolean getMigratedProcess() {
        return migratedProcess;
    }

    public void setMigratedProcess(Boolean migratedProcess) {
        this.migratedProcess = migratedProcess;
    }

    public Boolean getIsBolonha() {
        return isBolonha;
    }

    public void setIsBolonha(Boolean isBolonha) {
        this.isBolonha = isBolonha;
    }
}
