package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Clocking extends Clocking_Base {

    public Clocking(Assiduousness assiduousness, ClockUnit clockUnit, DateTime date, Integer oracleSequence) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setDate(date);
	setAssiduousness(assiduousness);
	setClockUnit(clockUnit);
	setOracleSequence(oracleSequence);
    }

    public TimeOfDay getTimeOfDay() {
	return getDate().toTimeOfDay();
    }

    public String getDeleteSlot() {
	YearMonth yearMonth = new YearMonth(getDate().toLocalDate());
	if (yearMonth.getIsThisYearMonthClosed()) {
	    return "";
	}
	return "(" + ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale()).getString("label.delete")
		+ ")";
    }

    public String getRestoreSlot() {
	YearMonth yearMonth = new YearMonth(getDate().toLocalDate());
	if (yearMonth.getIsThisYearMonthClosed()) {
	    return "";
	}
	return "("
		+ ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale()).getString("label.restore")
		+ ")";
    }

    @Override
    public boolean isClocking() {
	return true;
    }

}
