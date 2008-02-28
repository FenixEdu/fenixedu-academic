package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;

public class ResultUnitAssociationCreationBean implements Serializable {
    private DomainReference<ResearchResult> result;
    private DomainReference<Unit> unit;
    private List<DomainReference<Unit>> suggestedUnits;
    private String role;
    private String unitName;
    private UnitType unitType; 
    private boolean isSuggestion;
    
    
    public ResultUnitAssociationCreationBean(ResearchResult result) {
	setResult(new DomainReference<ResearchResult>(result));
	setUnit(null);
	setUnitName(null);
	setRole(ResultUnitAssociationRole.getDefaultRole());
	setUnitType(UnitType.ACADEMIC_UNIT);
	suggestedUnits = new ArrayList<DomainReference<Unit>>();
	setSuggestion(false);
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

	public static enum UnitType {
		ACADEMIC_UNIT ("Academic"),
		RESEARCH_UNIT ("Research"),
		ORGANIZATIONAL_UNIT ("Organizational");
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
	
	public List<Unit> getSuggestedUnits() {
	    List<Unit> units = new ArrayList<Unit> ();
	    for(DomainReference<Unit> suggestedUnit : this.suggestedUnits) {
		units.add(suggestedUnit.getObject());
	    }
	    return units;
	}
	
	public void setSuggestedUnits(List<Unit> units) {
	    this.suggestedUnits.clear();
	    for(Unit unit : units) {
		suggestedUnits.add(new DomainReference<Unit>(unit));
	    }
	}

	public boolean isSuggestion() {
	    return isSuggestion;
	}

	public void setSuggestion(boolean isSuggestion) {
	    this.isSuggestion = isSuggestion;
	}
}
