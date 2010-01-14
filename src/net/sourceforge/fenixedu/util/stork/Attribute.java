package net.sourceforge.fenixedu.util.stork;

public class Attribute {
    Integer id;
    String name;
    Boolean mandatory;
    String value;

    public Attribute(Integer id, String name, Boolean mandatory, String value) {
	setId(id);
	setName(name);
	setMandatory(mandatory);
	setValue(value);
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer order) {
	this.id = order;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Boolean getMandatory() {
	return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
	this.mandatory = mandatory;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getSemanticValue() {
	if (!isValueAssigned())
	    return null;

	return getValue();
    }

    public boolean isValueAssigned() {
	return getValue() != null && !"null".equals(getValue());
    }
}
