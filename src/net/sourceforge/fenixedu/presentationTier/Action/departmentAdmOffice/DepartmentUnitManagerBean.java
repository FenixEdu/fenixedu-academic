package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.io.Serializable;

public class DepartmentUnitManagerBean implements Serializable {

    /**
     * Serial Version id.
     */
    private static final long serialVersionUID = 1L;

    private String alias;
    private String idNumber;

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
        
        if (this.alias.length() == 0) {
            this.alias = null;
        }
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
        
        if (this.idNumber.length() == 0) {
            this.idNumber = null;
        }
    }

}