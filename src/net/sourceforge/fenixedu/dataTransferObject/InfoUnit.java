/*
 * Created on Sep 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IUnit;

public class InfoUnit extends InfoObject{
    
    private String name = null;
    private String costCenterCode = null;
    private String department = null;
    
    
    public String getCostCenterCode() {
        return costCenterCode;
    }
    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public void copyFromDomain(IUnit unit) {
        super.copyFromDomain(unit);
        if (unit != null) {
            setCostCenterCode(unit.getCostCenterCode().toString());
            setName(unit.getName());
            if(unit.getParentUnit() == null){
                if(unit.getDepartment() != null){
                    setDepartment(unit.getDepartment().getName());
                }
                else{
                    setDepartment("");
                }
            }
            else{               
                while(unit.getParentUnit() != null){
                    unit = unit.getParentUnit();
                }
               
                if(unit.getDepartment() != null){
                    setDepartment(unit.getDepartment().getName());
                }
                else{
                    setDepartment("");
                }
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
