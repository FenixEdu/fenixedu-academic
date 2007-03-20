package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.Date;

public class VariantBean implements Serializable {

    public static enum Type {
	INTEGER,
	STRING,
	DATE
    };
    
    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private Object value;
    private Type type;
    
    public VariantBean() {
	value = null;
    }
    
    public Type getType() {
        return type;
    }
    
    protected void setType(Type type) {
	this.type = type;
    }

    public Integer getInteger() {
	return (Integer) this.value;
    }

    public void setInteger(Integer value) {
	this.value = value;
	setType(Type.INTEGER);
    }

    public Date getDate() {
	return (Date) this.value;
    }
    
    public void setDate(Date date) {
	this.value = date;
	setType(Type.DATE);
    }
    
    public String getString() {
	return (String) this.value;
    }
    
    public void setString(String string) {
	this.value = string;
	setType(Type.STRING);
    }
    
}
