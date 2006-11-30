package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class GratuityExemptionTypeByDegreeTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final GratuityEvent gratuityEvent = getGratuityEvent(source);
	return GratuityExemptionType.getExemptionsFor(gratuityEvent.getDegree().getDegreeType());
    }

    private GratuityEvent getGratuityEvent(final Object source) {
	return ((CreateGratuityExemptionBean) source).getGratuityEvent();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
