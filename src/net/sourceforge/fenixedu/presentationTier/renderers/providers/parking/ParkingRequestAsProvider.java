package net.sourceforge.fenixedu.presentationTier.renderers.providers.parking;

import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactory;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ParkingRequestAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ParkingRequestFactory parkingRequestFactory = (ParkingRequestFactory) source;
        return parkingRequestFactory.getParkingParty().getSubmitAsRoles();
    }

    public Converter getConverter() {
        return new EnumConverter();
    }
}
