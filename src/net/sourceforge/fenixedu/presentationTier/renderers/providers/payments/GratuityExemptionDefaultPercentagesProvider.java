package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import java.math.BigDecimal;
import java.util.Arrays;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class GratuityExemptionDefaultPercentagesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(new BigDecimal[] { new BigDecimal("25"), new BigDecimal("50"),
		new BigDecimal("75"), new BigDecimal("100") });
    }

    public Converter getConverter() {
	return null;
    }

}
