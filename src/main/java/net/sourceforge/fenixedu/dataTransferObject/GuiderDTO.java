package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.GuiderType;

public class GuiderDTO extends DataTranferObject {

    private GuiderType guiderType;

    private String guiderName;

    private String guiderId;

    private String institutionName;

    public GuiderDTO(GuiderType type, String name, String id, String institutionName) {
        this.guiderType = type;
        this.guiderName = name;
        this.guiderId = id;
        this.institutionName = institutionName;
    }

    public String getGuiderName() {
        return guiderName;
    }

    public void setGuiderName(String guiderName) {
        this.guiderName = guiderName;
    }

    public String getGuiderNumber() {
        return guiderId;
    }

    public void setGuiderNumber(String guiderId) {
        this.guiderId = guiderId;
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
