package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.NumberInputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.Money;

/**
 * {@inheritDoc}
 * 
 * This renderer converts the value to a Money with its string constructor.
 * 
 * @author lepc
 */
public class MoneyInputRenderer extends NumberInputRenderer {

    @Override
    protected Converter getConverter() {
	return new MoneyConverter();
    }

    @Override
    public HtmlComponent render(Object targetObject, Class type) {
	final Money money = ((Money) targetObject);
	return super.render(money != null ? money.getAmount() : null, type);
    }

    private class MoneyConverter extends Converter {

	@Override
	public Object convert(Class type, Object value) {
	    final String numberText = ((String) value).trim();
	    try {
		return numberText.length() == 0 ? null : new Money(numberText);
	    } catch (NumberFormatException e) {
		throw new ConversionException("renderers.converter.money", e, true, value);
	    }
	}

    }
}
