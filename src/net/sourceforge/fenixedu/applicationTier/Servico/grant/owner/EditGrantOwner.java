package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;

public class EditGrantOwner extends Service {

    private String generateGrantOwnerPersonUsername(Integer grantOwnerNumber) {
        String result = null;
        result = "b" + grantOwnerNumber.toString();
        return result;
    }

    private GrantOwner checkIfGrantOwnerExists(Integer grantOwnerNumber,
            IPersistentGrantOwner persistentGrantOwner) throws FenixServiceException,
            ExcepcaoPersistencia {
        GrantOwner grantOwner = null;
        grantOwner = persistentGrantOwner.readGrantOwnerByNumber(grantOwnerNumber);
        return grantOwner;
    }

    private GrantOwner prepareGrantOwner(GrantOwner grantOwner, Person person,
            InfoGrantOwner infoGrantOwner, IPersistentGrantOwner pGrantOwner)
            throws ExcepcaoPersistencia {
        grantOwner.setPerson(person);
        grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
        grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());

        if (infoGrantOwner.getGrantOwnerNumber() == null) {
            // Generate the GrantOwner's number
            Integer maxNumber = pGrantOwner.readMaxGrantOwnerNumber();
            int aux = maxNumber.intValue() + 1;
            Integer nextNumber = new Integer(aux);
            grantOwner.setNumber(nextNumber);
        } else
            grantOwner.setNumber(infoGrantOwner.getGrantOwnerNumber());

        return grantOwner;
    }

    protected boolean isNew(DomainObject domainObject) {
        Integer objectId = domainObject.getIdInternal();
        return ((objectId == null) || objectId.equals(new Integer(0)));
    }

    public Integer run(InfoGrantOwner infoGrantOwner) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentGrantOwner pGrantOwner = null;

        pGrantOwner = persistentSupport.getIPersistentGrantOwner();

        Person person = null;
        GrantOwner grantOwner = null;
        Country country = null;

        if (infoGrantOwner.getPersonInfo().getInfoPais() != null) {
            country = (Country) persistentObject.readByOID(Country.class, infoGrantOwner.getPersonInfo()
                    .getInfoPais().getIdInternal());
        } else {
            // If the person country is undefined it is set to default
            // "PORTUGUESA NATURAL DO CONTINENTE"
            // In a not distance future this will not be needed since the coutry
            // can never be null
            country = Country.readCountryByNationality("PORTUGUESA NATURAL DO CONTINENTE");
        }

        // create or edit person information
        if (infoGrantOwner.getPersonInfo().getIdInternal() == null) {           
            person = DomainFactory.makePerson(infoGrantOwner.getPersonInfo(), country);            
        } else {
            person = (Person) persistentObject.readByOID(Person.class, infoGrantOwner.getPersonInfo()
                    .getIdInternal());
            person.edit(infoGrantOwner.getPersonInfo(), country);
        }

        // verify if person is new
        if (person.getUsername() != null)
            grantOwner = checkIfGrantOwnerExists(infoGrantOwner.getGrantOwnerNumber(), pGrantOwner);

        // create or edit grantOwner information
        if (grantOwner == null) {

            grantOwner = DomainFactory.makeGrantOwner();

            if (!person.hasRole(RoleType.PERSON)) {
                person.getPersonRoles().add(Role.getRoleByRoleType(RoleType.PERSON));
            }
            person.getPersonRoles().add(Role.getRoleByRoleType(RoleType.GRANT_OWNER));
        }
        grantOwner = prepareGrantOwner(grantOwner, person, infoGrantOwner, pGrantOwner);

        // Generate the GrantOwner's Person Username
        if (person.getUsername() == null || person.getUsername().length() == 0)
            person.changeUsername(generateGrantOwnerPersonUsername(grantOwner.getNumber()));
        return grantOwner.getIdInternal();
    }
}
