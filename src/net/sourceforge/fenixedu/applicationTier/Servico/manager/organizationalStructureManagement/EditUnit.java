package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitClassification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.YearMonthDay;

public class EditUnit extends Service {

    public void run(Integer unitID, String unitName, String unitCostCenter, String acronym,
	    YearMonthDay begin, YearMonthDay end, Integer departmentID, Integer degreeID, Integer administrativeOfficeID,
            String webAddress, UnitClassification classification, Boolean canBeResponsibleOfSpaces)
    		throws ExcepcaoPersistencia, FenixServiceException, DomainException, FenixFilterException {

        Unit unit = (Unit) rootDomainObject.readPartyByOID(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.noUnit");
        }
              
        Integer costCenterCode = getCostCenterCode(unitCostCenter);
        
        Degree degree = rootDomainObject.readDegreeByOID(degreeID);
        Department department = rootDomainObject.readDepartmentByOID(departmentID);        
        AdministrativeOffice administrativeOffice = rootDomainObject.readAdministrativeOfficeByOID(administrativeOfficeID);
        
        unit.edit(unitName, costCenterCode, acronym, begin, end, webAddress, classification, department, degree, administrativeOffice, canBeResponsibleOfSpaces);	                 
    }
    
    private Integer getCostCenterCode(String unitCostCenter) {
        Integer costCenterCode = null;
        if (unitCostCenter != null && !unitCostCenter.equals("")) {
            costCenterCode = (Integer.valueOf(unitCostCenter));
        }
        return costCenterCode;
    }       
}
