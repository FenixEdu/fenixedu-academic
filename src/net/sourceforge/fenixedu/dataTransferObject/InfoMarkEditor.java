package net.sourceforge.fenixedu.dataTransferObject;


public class InfoMarkEditor extends InfoObject {
    
    private String mark;

    private InfoFrequenta infoFrequenta;

    public InfoMarkEditor() {
    }

    public InfoFrequenta getInfoFrequenta() {
        return infoFrequenta;
    }

    public String getMark() {
        return mark;
    }

    public void setInfoFrequenta(InfoFrequenta frequenta) {
        infoFrequenta = frequenta;
    }

    public void setMark(String string) {
        mark = string;
    }

}
