package pt.ist.fenix.presentationTier.renderers.providers.parking;

import pt.ist.fenix.domain.parking.ParkingRequest.ParkingRequestFactory;
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
