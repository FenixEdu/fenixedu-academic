package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.TemporalInformationType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class TimeInformationProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        List<TemporalInformationType> types = new ArrayList<TemporalInformationType>();

        for (TemporalInformationType type : TemporalInformationType.values()) {
            types.add(type);
        }

        return types;
    }

    public Converter getConverter() {
        return new Converter() {

            @Override
            public Object convert(Class type, Object value) {
                return TemporalInformationType.valueOf((String) value);

            }

        };
    }

}