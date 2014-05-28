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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdProgramServiceAgreementTemplate extends PhdProgramServiceAgreementTemplate_Base {

    protected PhdProgramServiceAgreementTemplate() {
        super();
    }

    public PhdProgramServiceAgreementTemplate(final PhdProgram phdProgram) {
        init(phdProgram);
    }

    private void init(PhdProgram phdProgram) {
        String[] args = {};
        if (phdProgram == null) {
            throw new DomainException("error.phd.PhdProgramServiceAgreementTemplate.phdProgram.cannot.be.null", args);
        }

        super.setPhdProgram(phdProgram);
    }

    @Override
    public void setPhdProgram(PhdProgram phdProgram) {
        throw new DomainException("error.phd.PhdProgramServiceAgreementTemplate.cannot.modify.phdProgram");
    }

    @Deprecated
    public boolean hasPhdProgram() {
        return getPhdProgram() != null;
    }

}
