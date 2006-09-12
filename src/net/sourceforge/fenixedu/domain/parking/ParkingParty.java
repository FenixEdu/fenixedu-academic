package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;

public class ParkingParty extends ParkingParty_Base{

    public ParkingParty(Party party) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParty(party);
        setAuthorized(Boolean.FALSE);
    }

    public boolean getHasAllNecessaryPersonalInfo() {
        return (((Person) getParty()).getPhone().length() != 0
                && ((Person) getParty()).getMobile().length() != 0 && ((Person) getParty()).getEmail()
                .length() != 0);
    }

    public ParkingRequest getFirstRequest() {
        return getParkingRequests().iterator().next();
    }

    public ParkingRequestFactoryCreator getParkingRequestFactoryCreator() {
        return new ParkingRequestFactoryCreator(this);
    }
}
