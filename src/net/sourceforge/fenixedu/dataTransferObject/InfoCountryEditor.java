package net.sourceforge.fenixedu.dataTransferObject;

public class InfoCountryEditor extends InfoObject {

    private String name;

    private String code;

    private String nationality;

    public InfoCountryEditor() {
    }

    public InfoCountryEditor(String name, String code, String nationality) {
        setName(name);
        setCode(code);
        setNationality(nationality);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

}
