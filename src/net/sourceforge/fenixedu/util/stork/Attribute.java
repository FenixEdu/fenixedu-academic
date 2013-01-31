package net.sourceforge.fenixedu.util.stork;

import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.StorkAttributeType;

import org.apache.commons.lang.StringUtils;

public class Attribute {
	Integer id;
	StorkAttributeType type;
	Boolean mandatory;
	String value;

	public Attribute(Integer id, StorkAttributeType type, Boolean mandatory, String value) {
		setId(id);
		setType(type);
		setMandatory(mandatory);
		setValue(value);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer order) {
		this.id = order;
	}

	public StorkAttributeType getType() {
		return this.type;
	}

	public void setType(StorkAttributeType type) {
		this.type = type;
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
		if (!isValueAssigned()) {
			return null;
		}

		return getValue();
	}

	public boolean isValueAssigned() {
		return !StringUtils.isEmpty(getValue()) && !"null".equals(getValue());
	}
}
