/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import java.text.ParseException;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.renderers.validators.DateValidator;
import net.sourceforge.fenixedu.renderers.validators.HtmlChainValidator;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.joda.time.DateTime;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DateInExecutionPeriodValidator extends DateValidator {

    public DateInExecutionPeriodValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
    }

    public DateInExecutionPeriodValidator(HtmlChainValidator htmlChainValidator, String dateFormat) {
	super(htmlChainValidator, dateFormat);
    }

    @Override
    public void performValidation() {
	super.performValidation();

	if (isValid()) {
	    try {
		DateTime dateTime = new DateTime(DateFormatUtil.parse(getDateFormat(), getComponent().getValue()).getTime());
		setValid(ExecutionPeriod.readByDateTime(dateTime) != null);
		if (!isValid()) {
		    setMessage("renderers.validator.dateInExecutionPeriod.notInExecutionPeriod");
		}
	    } catch (ParseException e) {
		setValid(false);
		e.printStackTrace();
	    }
	}
    }

}
