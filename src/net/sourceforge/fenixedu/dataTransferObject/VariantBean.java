package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class VariantBean implements Serializable {

	public static enum Type {
		INTEGER, STRING, DATE, MULTI_LANGUAGE_STRING, DOMAIN_REFERENCE, YEAR_MONTH_DAY, LOCAL_DATE
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

	public MultiLanguageString getMLString() {
		return (MultiLanguageString) this.value;
	}

	public void setMLString(MultiLanguageString value) {
		this.value = value;
		setType(Type.MULTI_LANGUAGE_STRING);
	}

	public DomainObject getDomainObject() {
		return Type.DOMAIN_REFERENCE.equals(type) ? ((DomainObject) (this.value)) : null;
	}

	public void setDomainObject(DomainObject domainObject) {
		this.value = domainObject;
		setType(Type.DOMAIN_REFERENCE);
	}

	public void setYearMonthDay(YearMonthDay date) {
		this.value = date;
		setType(Type.YEAR_MONTH_DAY);
	}

	public YearMonthDay getYearMonthDay() {
		return Type.YEAR_MONTH_DAY.equals(getType()) ? (YearMonthDay) this.value : null;
	}

	public void setLocalDate(LocalDate date) {
		this.value = date;
		setType(Type.LOCAL_DATE);
	}

	public LocalDate getLocalDate() {
		return Type.LOCAL_DATE.equals(getType()) ? (LocalDate) this.value : null;
	}

}
