package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;

public class ResultUnitAssociationCreationBean implements Serializable {
    private DomainReference<Result> result;
    private DomainReference<Unit> unit;
    private String role;
    private String unitName;

    public ResultUnitAssociationCreationBean(Result resultReference) {
        role = ResultUnitAssociationRole.getDefaultUnitRoleType().toString();
        this.result = new DomainReference<Result>(resultReference);
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
    
    public Result getResult() {
        return (this.result == null) ? null : this.result.getObject();
    }

    public void setResult(Result result) {
        this.result = (result != null) ? new DomainReference<Result>(result) : null;
    }
}
