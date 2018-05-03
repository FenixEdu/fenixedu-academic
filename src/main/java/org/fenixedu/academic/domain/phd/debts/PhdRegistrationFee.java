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
package org.fenixedu.academic.domain.phd.debts;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;

import pt.ist.fenixframework.Atomic;

public class PhdRegistrationFee extends PhdRegistrationFee_Base {

    private PhdRegistrationFee() {
        super();
    }

    public PhdRegistrationFee(final PhdIndividualProgramProcess process) {
        this();
        init(process.getAdministrativeOffice(), process.getPerson(), process);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, PhdIndividualProgramProcess process) {
        super.init(administrativeOffice, EventType.PHD_REGISTRATION_FEE, person);
        checkProcess(process);
        super.setProcess(process);
    }

    private void checkProcess(PhdIndividualProgramProcess process) {
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PhdRegistrationFee.process.cannot.be.null", args);
        }

        if (process.getWhenFormalizedRegistration() == null) {
            throw new DomainException("error.PhdRegistrationFee.process.no.registration.formalization.date");
        }

        if (process.getRegistrationFee() != null) {
            throw new DomainException("error.PhdRegistrationFee.process.already.has.registration.fee");
        }
    }

    @Override
    protected void disconnect() {
        setProcess(null);
        super.disconnect();
    }

    @Override
    protected PhdProgram getPhdProgram() {
        return getProcess().getPhdProgram();
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    public boolean hasPhdRegistrationFeePenaltyExemption() {
        return getPhdRegistrationFeePenaltyExemption() != null;
    }

    public PhdRegistrationFeePenaltyExemption getPhdRegistrationFeePenaltyExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof PhdRegistrationFeePenaltyExemption) {
                return (PhdRegistrationFeePenaltyExemption) exemption;
            }
        }

        return null;
    }

    @Atomic
    static public PhdRegistrationFee create(final PhdIndividualProgramProcess process) {
        return new PhdRegistrationFee(process);
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
        return getProcess();
    }

}
