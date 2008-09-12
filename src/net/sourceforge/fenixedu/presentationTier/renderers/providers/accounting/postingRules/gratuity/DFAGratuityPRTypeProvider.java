package net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.postingRules.gratuity;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DFAGratuityPRTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(DFAGratuityByAmountPerEctsPR.class, DFAGratuityByNumberOfEnrolmentsPR.class);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
