/*
 * Created on 8/Jan/2005
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ICostCenter;

/**
 * @author Tânia Pousão
 *
 */
public class InfoCostCenter extends InfoObject {
    private String code;
    private String departament;
    private String section1;
    private String section2;
    
    
    
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return Returns the departament.
     */
    public String getDepartament() {
        return departament;
    }
    /**
     * @param departament The departament to set.
     */
    public void setDepartament(String departament) {
        this.departament = departament;
    }
    /**
     * @return Returns the section1.
     */
    public String getSection1() {
        return section1;
    }
    /**
     * @param section1 The section1 to set.
     */
    public void setSection1(String section1) {
        this.section1 = section1;
    }
    /**
     * @return Returns the section2.
     */
    public String getSection2() {
        return section2;
    }
    /**
     * @param section2 The section2 to set.
     */
    public void setSection2(String section2) {
        this.section2 = section2;
    }
    
    
    /* (non-Javadoc)
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(ICostCenter costCenter) {
        super.copyFromDomain(costCenter);
        if(costCenter != null) {
            setCode(costCenter.getCode());
            setDepartament(costCenter.getDepartament());
            setSection1(costCenter.getSection1());
            setSection2(costCenter.getSection2());
        }
    }
    
    public static InfoCostCenter newInfoFromDomain(ICostCenter costCenter) {
        InfoCostCenter infoCostCenter = null;
        if (costCenter != null) {
            infoCostCenter = new InfoCostCenter();
            infoCostCenter.copyFromDomain(costCenter);
        }
        return infoCostCenter;
    }
}
