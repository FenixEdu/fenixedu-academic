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
package org.fenixedu.academic.service.services.administrativeOffice.externalUnits;

import java.util.Locale;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.CreateExternalUnitBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.CreateUnit;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class CreateExternalUnit {

    @Atomic
    public static Unit run(final CreateExternalUnitBean externalUnitBean) throws FenixServiceException {

        if (externalUnitBean.getUnitType() == PartyTypeEnum.DEPARTMENT) {

            final LocalizedString localizedName = new LocalizedString(Locale.getDefault(), externalUnitBean.getUnitName());

            AccountabilityType accountabilityType = externalUnitBean.getParentUnit().isCountryUnit() ? AccountabilityType
                    .readByType(AccountabilityTypeEnum.GEOGRAPHIC) : AccountabilityType
                            .readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);

            return DepartmentUnit.createNewDepartmentUnit(localizedName, null, null, externalUnitBean.getUnitCode(),
                    new YearMonthDay(), null, externalUnitBean.getParentUnit(), accountabilityType, null, null, null, null);

//            return DepartmentUnit.createNewOfficialExternalDepartmentUnit(externalUnitBean.getUnitName(),
//                    externalUnitBean.getUnitCode(), externalUnitBean.getParentUnit());
        } else {
            return new CreateUnit().run(externalUnitBean.getParentUnit(),
                    new LocalizedString(Locale.getDefault(), externalUnitBean.getUnitName()), null, null,
                    externalUnitBean.getUnitCode(), new YearMonthDay(), null, externalUnitBean.getUnitType(), null, null, null,
                    null, null, null, null, null);
        }
    }
}