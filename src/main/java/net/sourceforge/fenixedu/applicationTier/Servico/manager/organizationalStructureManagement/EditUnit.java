package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EditUnit {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer unitID, MultiLanguageString unitName, String unitNameCard, String unitCostCenter,
            String acronym, YearMonthDay begin, YearMonthDay end, Integer departmentID, Integer degreeID,
            Integer administrativeOfficeID, String webAddress, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, Integer campusID) throws FenixServiceException, DomainException {

        ServiceMonitoring.logService(EditUnit.class, unitID, unitName, unitNameCard, unitCostCenter, acronym, begin, end,
                departmentID, degreeID, administrativeOfficeID, webAddress, classification, canBeResponsibleOfSpaces, campusID);

        Unit unit = (Unit) RootDomainObject.getInstance().readPartyByOID(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.noUnit");
        }

        Integer costCenterCode = getCostCenterCode(unitCostCenter);

        Degree degree = RootDomainObject.getInstance().readDegreeByOID(degreeID);
        Department department = RootDomainObject.getInstance().readDepartmentByOID(departmentID);
        AdministrativeOffice administrativeOffice =
                RootDomainObject.getInstance().readAdministrativeOfficeByOID(administrativeOfficeID);
        Campus campus = (Campus) RootDomainObject.getInstance().readResourceByOID(campusID);

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