package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;

public class ResultUnitAssociationCreationBean implements Serializable {
    private DomainReference<ResearchResult> result;
    private DomainReference<Unit> unit;
    private String role;
    private String unitName;
    private UnitType unitType; 
    
    public ResultUnitAssociationCreationBean(ResearchResult result) {
	setResult(new DomainReference<ResearchResult>(result));
	setUnit(null);
	setUnitName(null);
	setRole(ResultUnitAssociationRole.getDefaultRole());
	setUnitType(UnitType.INTERNAL_UNIT);
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
    
    public ResearchResult getResult() {
        return result.getObject();
    }

    public void setResult(DomainReference<ResearchResult> result) {
        this.result = result;
    }


    public Boolean getExternalUnit() {
    	return isExternalUnit();
    }
    
	public Boolean isExternalUnit() {
		return getUnitType().equals(UnitType.EXTERNAL_UNIT);
	}
	
	public static enum UnitType {
		INTERNAL_UNIT ("Internal"),
		EXTERNAL_UNIT ("External");
		
		private String type;
		
		private UnitType(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}
}
