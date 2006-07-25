package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;;

public class ResultUnitAssociationCreateBean implements Serializable {
  
    private DomainReference<Unit> unit;
    private String role;
    private String unitName;

    public ResultUnitAssociationCreateBean() {
        role = ResultUnitAssociationRole.getDefaultUnitRoleType().toString();
    }
    
    public ResultUnitAssociationRole getRole() {
        return ResultUnitAssociationRole.valueOf(role); 
    }

    public void setRole(ResultUnitAssociationRole resultUnitAssociationRole) {
        this.role = resultUnitAssociationRole.toString();
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String name) {
        this.unitName = name;
    }

    public Unit getUnit() {
        return (this.unit == null) ? null : this.unit.getObject();
    }

    public void setUnit(Unit unit) {
        this.unit = (unit != null) ? new DomainReference<Unit>(unit) : null;
    }
}
