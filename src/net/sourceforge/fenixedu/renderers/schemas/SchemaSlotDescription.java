package net.sourceforge.fenixedu.renderers.schemas;

import java.util.List;
import java.util.Properties;

import pt.ist.utl.fenix.utils.Pair;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public class SchemaSlotDescription {

    private String slotName;

    private String layout;
    private String schema;
    private Properties properties;

    private String bundle;
    private String key;

    private Class<Converter> converter;

    private List<Pair<Class<HtmlValidator>, Properties>> validators;

    private String defaultValue;

    private boolean readOnly;
    private boolean hidden;
    private boolean setterIgnored;

    public SchemaSlotDescription(String slotName) {
	this.slotName = slotName;
    }

    public String getSlotName() {
	return slotName;
    }

    public void setSlotName(String slotName) {
	this.slotName = slotName;
    }

    public String getLayout() {
	return layout;
    }

    public void setLayout(String layout) {
	this.layout = layout;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getBundle() {
	return this.bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public Properties getProperties() {
	return properties;
    }

    public void setProperties(Properties properties) {
	this.properties = properties;
    }

    public String getSchema() {
	return schema;
    }

    public void setSchema(String schema) {
	this.schema = schema;
    }

    public String getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
	this.defaultValue = defaultValue;
    }

    public Class<Converter> getConverter() {
	return this.converter;
    }

    public void setConverter(Class<Converter> converter) {
	this.converter = converter;
    }

    public boolean isReadOnly() {
	return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
	this.readOnly = readOnly;
    }

    public void setHidden(boolean hidden) {
	this.hidden = hidden;
    }

    public boolean isHidden() {
	return this.hidden;
    }

    public boolean isSetterIgnored() {
	return this.setterIgnored;
    }

    public void setSetterIgnored(boolean setterIgnored) {
	this.setterIgnored = setterIgnored;
    }

    public List<Pair<Class<HtmlValidator>, Properties>> getValidators() {
	return validators;
    }

    public void setValidators(List<Pair<Class<HtmlValidator>, Properties>> validators) {
	this.validators = validators;
    }

}
