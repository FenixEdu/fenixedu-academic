package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;

public class ResultUnitAssociationCreationBean implements Serializable {
    private ResearchResult result;
    private Unit unit;
    private final List<Unit> suggestedUnits;
    private String role;
    private String unitName;
    private UnitType unitType;
    private boolean isSuggestion;

    public ResultUnitAssociationCreationBean(ResearchResult result) {
        setResult(result);
        setUnit(null);
        setUnitName(null);
        setRole(ResultUnitAssociationRole.getDefaultRole());
        setUnitType(UnitType.ACADEMIC_UNIT);
        suggestedUnits = new ArrayList<Unit>();
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
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ResearchResult getResult() {
        return result;
    }

    public void setResult(ResearchResult result) {
        this.result = result;
    }

    public static enum UnitType {
        ACADEMIC_UNIT("Academic"), RESEARCH_UNIT("Research"), ORGANIZATIONAL_UNIT("Organizational"), ANY_UNIT("Any");
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
        List<Unit> units = new ArrayList<Unit>();
        for (Unit suggestedUnit : this.suggestedUnits) {
            units.add(suggestedUnit);
        }
        return units;
    }

    public void setSuggestedUnits(List<Unit> units) {
        this.suggestedUnits.clear();
        for (Unit unit : units) {
            suggestedUnits.add(unit);
        }
    }

    public boolean isSuggestion() {
        return isSuggestion;
    }

    public void setSuggestion(boolean isSuggestion) {
        this.isSuggestion = isSuggestion;
    }
}
