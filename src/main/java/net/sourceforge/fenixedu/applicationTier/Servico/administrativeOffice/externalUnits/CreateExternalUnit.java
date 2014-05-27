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
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.CreateUnit;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalUnitBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateExternalUnit {

    @Atomic
    public static Unit run(final CreateExternalUnitBean externalUnitBean) throws FenixServiceException {

        if (externalUnitBean.getUnitType() == PartyTypeEnum.DEPARTMENT) {
            return DepartmentUnit.createNewOfficialExternalDepartmentUnit(externalUnitBean.getUnitName(),
                    externalUnitBean.getUnitCode(), externalUnitBean.getParentUnit());
        } else {
            return new CreateUnit().run(externalUnitBean.getParentUnit(), new MultiLanguageString(Locale.getDefault(),
                    externalUnitBean.getUnitName()), null, null, externalUnitBean.getUnitCode(), new YearMonthDay(), null,
                    externalUnitBean.getUnitType(), null, null, null, null, null, null, null, null);
        }
    }
}