/*
 * Created on Sep 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;

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

    public void copyFromDomain(IUnit unit) {
        super.copyFromDomain(unit);
        if (unit != null) {
            List superiorUnits = new ArrayList();
            setCostCenterCode(unit.getCostCenterCode().toString());
            setName(unit.getName());
            IUnit unitBase = unit;
            if (unit.getParentUnit() != null) {
                while (unit.getParentUnit() != null) {
                    superiorUnits.add(unit.getParentUnit().getName());
                    unit = unit.getParentUnit();
                }
                String unitName = unitBase.getCostCenterCode().toString() + " - " + unitBase.getName(); 
                superiorUnits.add(0, unitName);
                Collections.reverse(superiorUnits);
                setSuperiorUnitsNames(superiorUnits);
            }
        }
    }

    public static InfoUnit newInfoFromDomain(IUnit unit) {
        InfoUnit infoUnit = null;
        if (unit != null) {
            infoUnit = new InfoUnit();
            infoUnit.copyFromDomain(unit);
        }
        return infoUnit;
    }

}
