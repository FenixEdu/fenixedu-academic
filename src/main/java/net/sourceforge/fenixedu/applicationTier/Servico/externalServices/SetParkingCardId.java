package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.parking.CreateParkingParty;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;
import pt.ist.fenixWebFramework.services.Service;

public class SetParkingCardId {

    public static class NotAuthorizedException extends FenixServiceException {
    }

    public static class UserDoesNotExistException extends FenixServiceException {

    }

    private static final String password;
    static {
        password = PropertiesManager.getProperty("parkingCardId.admin.password");
    }

    public static boolean isAllowed(final String password) {
        return SetParkingCardId.password != null && SetParkingCardId.password.equals(password);
    }

    private static String set(final String identificationCardCode, final Long parkingCardID) throws FenixServiceException {
        CardGenerationEntry cardGenerationEntry =
                CardGenerationEntry.readByEntityCodeAndCategoryCodeAndMemberNumber(identificationCardCode.substring(0, 13));
        if (cardGenerationEntry == null) {
            throw new UserDoesNotExistException();
        }
        if (cardGenerationEntry.getPerson().getParkingParty() == null) {
            CreateParkingParty.run(cardGenerationEntry.getPerson());
        }
        cardGenerationEntry.getPerson().getParkingParty().setCardNumber(parkingCardID);
        return cardGenerationEntry.getPerson().getIstUsername();
    }

    @Service
    public static String run(final String password, final String identificationCardCode, final Long parkingCardID)
            throws FenixServiceException {
        if (isAllowed(password)) {
            return set(identificationCardCode, parkingCardID);
        } else {
            throw new NotAuthorizedException();
        }
    }

}