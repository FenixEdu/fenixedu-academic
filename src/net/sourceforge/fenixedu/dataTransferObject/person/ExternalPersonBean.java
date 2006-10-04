package net.sourceforge.fenixedu.dataTransferObject.person;

public class ExternalPersonBean extends PersonBean {

    String unitName;

    public ExternalPersonBean() {
	super();
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

}
