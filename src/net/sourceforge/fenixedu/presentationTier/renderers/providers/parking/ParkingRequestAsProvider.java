package net.sourceforge.fenixedu.presentationTier.renderers.providers.parking;

import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactory;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class ParkingRequestAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ParkingRequestFactory parkingRequestFactory = (ParkingRequestFactory) source;
        return parkingRequestFactory.getParkingParty().getSubmitAsRoles();
    }

    public Converter getConverter() {
        return new EnumConverter();
    }
}
