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
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EditUnit {

    @Atomic
    public static void run(String unitID, MultiLanguageString unitName, String unitNameCard, String unitCostCenter,
            String acronym, YearMonthDay begin, YearMonthDay end, String departmentID, String degreeID,
            String administrativeOfficeID, String webAddress, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, String campusID) throws FenixServiceException, DomainException {
        check(RolePredicates.MANAGER_PREDICATE);

        ServiceMonitoring.logService(EditUnit.class, unitID, unitName, unitNameCard, unitCostCenter, acronym, begin, end,
                departmentID, degreeID, administrativeOfficeID, webAddress, classification, canBeResponsibleOfSpaces, campusID);

        Unit unit = (Unit) FenixFramework.getDomainObject(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.noUnit");
        }

        Integer costCenterCode = getCostCenterCode(unitCostCenter);

        Degree degree = FenixFramework.getDomainObject(degreeID);
        Department department = FenixFramework.getDomainObject(departmentID);
        AdministrativeOffice administrativeOffice = FenixFramework.getDomainObject(administrativeOfficeID);
        Space campus = (Space) FenixFramework.getDomainObject(campusID);

        unit.edit(unitName, unitNameCard, costCenterCode, acronym, begin, end, webAddress, classification, department, degree,
                administrativeOffice, canBeResponsibleOfSpaces, campus);
    }

    private static Integer getCostCenterCode(String unitCostCenter) {
        Integer costCenterCode = null;
        if (unitCostCenter != null && !unitCostCenter.equals("")) {
            costCenterCode = (Integer.valueOf(unitCostCenter));
        }
        return costCenterCode;
    }
}