package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;

public class Clocking extends Clocking_Base {

    public Clocking(Assiduousness assiduousness, ClockUnit clockUnit, DateTime date,
	    Integer oracleSequence) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDate(date);
	setAssiduousness(assiduousness);
	setClockUnit(clockUnit);
	setOracleSequence(oracleSequence);
	setOjbConcreteClass(Clocking.class.getName());
    }

    public TimeOfDay getTimeOfDay() {
	return getDate().toTimeOfDay();
    }

    public String getDeleteSlot() {
	return "("
		+ ResourceBundle
			.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale())
			.getString("label.delete") + ")";
    }

    public String getRestoreSlot() {
	return "("
		+ ResourceBundle
			.getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale())
			.getString("label.restore") + ")";
    }

}
