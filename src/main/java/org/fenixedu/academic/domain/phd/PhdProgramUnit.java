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
package org.fenixedu.academic.domain.phd;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.MultiLanguageString;
import org.joda.time.YearMonthDay;

public class PhdProgramUnit extends PhdProgramUnit_Base {

    private PhdProgramUnit() {
        super();
        super.setType(PartyTypeEnum.PHD_PROGRAM_UNIT);
    }

    static PhdProgramUnit create(final PhdProgram program, final MultiLanguageString unitName, final YearMonthDay beginDate,
            final YearMonthDay endDate, final Unit parent) {

        final PhdProgramUnit programUnit = new PhdProgramUnit();
        programUnit.init(unitName, null, null, null, beginDate, endDate, null, null, null, null, null);
        programUnit.setPhdProgram(program);
        programUnit.addParentUnit(parent, AccountabilityType.readByType(AccountabilityTypeEnum.ACADEMIC_STRUCTURE));

        return programUnit;
    }

    @Override
    public void delete() {
        setPhdProgram(null);
        super.delete();
    }

}
