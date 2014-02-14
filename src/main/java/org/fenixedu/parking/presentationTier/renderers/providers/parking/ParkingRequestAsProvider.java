package org.fenixedu.parking.presentationTier.renderers.providers.parking;

import org.fenixedu.parking.domain.ParkingRequest.ParkingRequestFactory;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ParkingRequestAsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ParkingRequestFactory parkingRequestFactory = (ParkingRequestFactory) source;
        return parkingRequestFactory.getParkingParty().getSubmitAsRoles();
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }
}
