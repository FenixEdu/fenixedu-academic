/**
 * 
 */
package net.sourceforge.fenixedu.renderers.validators;

import java.text.ParseException;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class AdvancedDateValidator extends DateValidator {

    private String validationPeriod;

    public AdvancedDateValidator(Validatable component) {
	super(component);
    }

    public AdvancedDateValidator(Validatable component, String dateFormat) {
	super(component, dateFormat);
    }

    @Override
    public void performValidation() {
	super.performValidation();

	if (isValid()) {
	    try {
		DateTime dateTime = new DateTime(DateFormatUtil.parse(getDateFormat(),
			getComponent().getValue()).getTime());
		setValid(getValidationPeriodType().evaluateDate(dateTime));
	    } catch (ParseException e) {
		setValid(false);
		e.printStackTrace();
	    }
	}

    }

    public String getValidationPeriod() {
	return validationPeriod;
    }

    public void setValidationPeriod(String validationPeriod) {
	this.validationPeriod = validationPeriod;
	setMessage("renderers.validator.advancedDate." + getValidationPeriod());
    }

    public ValidationPeriodType getValidationPeriodType() {
	if (this.validationPeriod != null) {
	    return ValidationPeriodType.valueOf(getValidationPeriod().toUpperCase());
	}
	return null;
    }

    private static Predicate pastPredicate = new Predicate() {
	public boolean evaluate(Object arg0) {
	    return ((DateTime) arg0).isBeforeNow();
	}
    };

    private static Predicate pastOrTodayPredicate = new Predicate() {
	public boolean evaluate(Object arg0) {
	    final DateTime dateTime = (DateTime) arg0;
	    return dateTime.isBeforeNow() || dateTime.toYearMonthDay().isEqual(new YearMonthDay());
	}
    };

    private static Predicate futurePredicate = new Predicate() {
	public boolean evaluate(Object arg0) {
	    return ((DateTime) arg0).isAfterNow();
	}
    };

    private enum ValidationPeriodType {

	PAST(pastPredicate), PASTORTODAY(pastOrTodayPredicate), FUTURE(futurePredicate);

	private Predicate predicate;

	private ValidationPeriodType(Predicate predicate) {
	    this.predicate = predicate;
	}

	protected boolean evaluateDate(DateTime dateTime) {
	    return this.predicate.evaluate(dateTime);
	}

    }

}
