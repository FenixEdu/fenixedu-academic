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
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;

public class PhdThesisRequestFee extends PhdThesisRequestFee_Base {

    private PhdThesisRequestFee() {
        super();
    }

    public PhdThesisRequestFee(final PhdIndividualProgramProcess process) {
        this();
        init(process.getAdministrativeOffice(), process.getPerson(), process);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, PhdIndividualProgramProcess process) {
        super.init(administrativeOffice, EventType.PHD_THESIS_REQUEST_FEE, person);
        checkProcess(process);
        super.setProcess(process);
    }

    private void checkProcess(PhdIndividualProgramProcess process) {
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PhdThesisRequestFee.process.cannot.be.null", args);
        }

        if (process.getThesisRequestFee() != null) {
            throw new DomainException("error.PhdThesisRequestFee.process.already.has.thesis.fee");
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

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
        return getProcess();
    }

    @Override
    public boolean isPhdThesisRequestFee() {
        return true;
    }

}
