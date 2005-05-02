package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.GuiderType;

public class GuiderDTO extends DataTranferObject {

    private GuiderType guiderType;
    
    private String guiderName;
    
    private Integer guiderNumber;
    
    private String institutionName;

    public GuiderDTO(GuiderType type, String name, Integer number, String institutionName) {
        this.guiderType = type;
        this.guiderName = name;
        this.guiderNumber = number;
        this.institutionName = institutionName;
    }

    public String getGuiderName() {
        return guiderName;
    }

    public void setGuiderName(String guiderName) {
        this.guiderName = guiderName;
    }

    public Integer getGuiderNumber() {
        return guiderNumber;
    }

    public void setGuiderNumber(Integer guiderNumber) {
        this.guiderNumber = guiderNumber;
    }

    public GuiderType getGuiderType() {
        return guiderType;
    }

    public void setGuiderType(GuiderType guiderType) {
        this.guiderType = guiderType;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    

}
