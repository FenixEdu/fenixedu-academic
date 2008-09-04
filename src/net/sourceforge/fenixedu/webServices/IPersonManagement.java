package net.sourceforge.fenixedu.webServices;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationDTO;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.codehaus.xfire.MessageContext;

public interface IPersonManagement {

    public abstract PersonInformationDTO getPersonInformation(String username, String password, String unserUID,
	    MessageContext context) throws NotAuthorizedException;

    public abstract Boolean setPersonInformation(String username, String password, String idNumber, String emissionDate,
	    String emissionLocale, String expirationDate, String fiscalNumber, String name, String fatherName, String motherName,
	    String birthDate, String gender, String address, String postalCode, String postalArea, String parish,
	    String locality, String municipality, String district, String country, MessageContext context)
	    throws NotAuthorizedException;
}