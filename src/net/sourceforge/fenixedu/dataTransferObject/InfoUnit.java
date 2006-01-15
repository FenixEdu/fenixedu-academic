/*
 * Created on Sep 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class InfoUnit extends InfoObject {

    private String name = null;

    private String costCenterCode = null;

    private List superiorUnitsNames = new ArrayList();

    public String getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getSuperiorUnitsNames() {
        return superiorUnitsNames;
    }

    public void setSuperiorUnitsNames(List superiorUnitsNames) {
        this.superiorUnitsNames = superiorUnitsNames;
    }

    public void copyFromDomain(Unit unit) {
        super.copyFromDomain(unit);
        if (unit != null) {            
            setCostCenterCode(unit.getCostCenterCode().toString());
            setName(unit.getName());
            Unit unitBase = unit;
            getParentUnitsNames(unit, unitBase);
        }
    }

  
    private void getParentUnitsNames(Unit unit, Unit unitBase) {
        
        List superiorUnits = new ArrayList();
        String unitName = unitBase.getName();                
        unitName = unitBase.getCostCenterCode().toString() + " - " + unitName; 
        
        if (!unit.getTopUnits().isEmpty() && unit.getTopUnits().size() == 1) {                     
            superiorUnits.add(0, unit.getTopUnits().get(0).getName());
            superiorUnits.add(1, unitName);
            setSuperiorUnitsNames(superiorUnits);
        }
        else{
            StringBuilder buffer = new StringBuilder();
            int size = unit.getTopUnits().size(), i = 0;
            for (Unit unit2 : unit.getTopUnits()) {
                buffer.append(unit2.getName());
                i++;
                if(i < size){
                    buffer.append(" / ");
                }
            }
            if(size != 0){
                superiorUnits.add(0, buffer.toString());
                superiorUnits.add(1, unitName);
            }
            else{
                superiorUnits.add(0, unitName);
            }
            setSuperiorUnitsNames(superiorUnits);
        }                
    }

    public static InfoUnit newInfoFromDomain(Unit unit) {
        InfoUnit infoUnit = null;
        if (unit != null) {
            infoUnit = new InfoUnit();
            infoUnit.copyFromDomain(unit);
        }
        return infoUnit;
    }
}
