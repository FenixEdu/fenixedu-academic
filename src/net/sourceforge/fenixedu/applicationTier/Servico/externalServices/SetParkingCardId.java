package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.parking.CreateParkingParty;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class SetParkingCardId extends FenixService {

    public static class NotAuthorizedException extends FenixServiceException {
    }

    public static class UserDoesNotExistException extends FenixServiceException {

    }

    private static final Set<String> allowedHosts = new HashSet<String>();

    private static final String password;
    static {
	final String allowedHostString = PropertiesManager.getProperty("parkingCardId.admin.allowed.hosts");
	if (allowedHostString != null) {
	    final String[] allowedHostTokens = allowedHostString.split(",");
	    for (int i = 0; i < allowedHostTokens.length; i++) {
		if (!StringUtils.isEmpty(allowedHostTokens[i])) {
		    allowedHosts.add(allowedHostTokens[i]);
		}
	    }
	}
	password = PropertiesManager.getProperty("parkingCardId.admin.password");
    }

    public static boolean isAllowed(final String host, final String ip, final String password) {
	return SetParkingCardId.password != null && SetParkingCardId.password.equals(password)
		&& (allowedHosts.isEmpty() || allowedHosts.contains(host) || allowedHosts.contains(ip));
    }

    private static String set(final String identificationCardCode, final Long parkingCardID) throws FenixServiceException {
	CardGenerationEntry cardGenerationEntry = CardGenerationEntry
		.readByEntityCodeAndCategoryCodeAndMemberNumber(identificationCardCode.substring(0, 13));
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
    public static String run(final String host, final String ip, final String password, final String identificationCardCode,
	    final Long parkingCardID) throws FenixServiceException {
	if (isAllowed(host, ip, password)) {
	    return set(identificationCardCode, parkingCardID);
	} else {
	    throw new NotAuthorizedException();
	}
    }

}