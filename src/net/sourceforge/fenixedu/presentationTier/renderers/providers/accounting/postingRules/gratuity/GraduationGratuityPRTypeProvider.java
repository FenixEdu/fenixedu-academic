package net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.postingRules.gratuity;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.PastDegreeGratuityPR;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class GraduationGratuityPRTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Arrays.asList(GratuityWithPaymentPlanPR.class, PastDegreeGratuityPR.class);
    }

    @Override
    public Converter getConverter() {
        return new BiDirectionalConverter() {

            static private final long serialVersionUID = 1L;

            @Override
            public Object convert(Class arg0, Object value) {
                try {
                    return Class.forName((String) value);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String deserialize(Object value) {
                return ((Class) value).getName();
            }
        };
    }
}
