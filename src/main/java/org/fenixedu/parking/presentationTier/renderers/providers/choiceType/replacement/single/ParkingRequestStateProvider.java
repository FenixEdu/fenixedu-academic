package org.fenixedu.parking.presentationTier.renderers.providers.choiceType.replacement.single;

import java.util.Arrays;

import org.fenixedu.parking.domain.ParkingRequestState;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ParkingRequestStateProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return Arrays.asList(ParkingRequestState.values());
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
