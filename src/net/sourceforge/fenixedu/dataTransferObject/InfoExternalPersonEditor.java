package net.sourceforge.fenixedu.dataTransferObject;

public class InfoExternalPersonEditor extends InfoObject {
    
    private String name;
    
    private InfoInstitution infoInstitution;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public InfoInstitution getInfoInstitution() {
        return infoInstitution;
    }

    public void setInfoInstitution(InfoInstitution infoInstitution) {
        this.infoInstitution = infoInstitution;
    }

}
