package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditGrantOwner extends Service {

    private String generateGrantOwnerPersonUsername(Integer grantOwnerNumber) {
        String result = null;
        result = "b" + grantOwnerNumber.toString();
        return result;
    }

    private GrantOwner checkIfGrantOwnerExists(Integer grantOwnerNumber) throws FenixServiceException,
            ExcepcaoPersistencia {
        return GrantOwner.readGrantOwnerByNumber(grantOwnerNumber);
    }

    private GrantOwner prepareGrantOwner(GrantOwner grantOwner, Person person,
            InfoGrantOwner infoGrantOwner, Integer maxNumber) throws ExcepcaoPersistencia {
       
        grantOwner.setPerson(person);
        grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
        grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());

        if (infoGrantOwner.getGrantOwnerNumber() == null) {
            // Generate the GrantOwner's number
            int aux = maxNumber + 1;
            Integer nextNumber = Integer.valueOf(aux);
            grantOwner.setNumber(nextNumber);
        } else
            grantOwner.setNumber(infoGrantOwner.getGrantOwnerNumber());

        return grantOwner;
    }

    protected boolean isNew(DomainObject domainObject) {
        Integer objectId = domainObject.getIdInternal();
        return ((objectId == null) || objectId.equals(Integer.valueOf(0)));
    }

    public Integer run(InfoGrantOwner infoGrantOwner) throws FenixServiceException, ExcepcaoPersistencia {
        Person person = null;
        GrantOwner grantOwner = null;
        Country country = null;

        if (infoGrantOwner.getPersonInfo().getInfoPais() != null) {
            country = rootDomainObject.readCountryByOID(infoGrantOwner.getPersonInfo().getInfoPais()
                    .getIdInternal());
        } else {
            // If the person country is undefined it is set to default
            // "PORTUGUESA NATURAL DO CONTINENTE"
            // In a not distance future this will not be needed since the coutry
            // can never be null
            country = Country.readCountryByNationality("PORTUGUESA NATURAL DO CONTINENTE");
        }

        // create or edit person information
        if (infoGrantOwner.getPersonInfo().getIdInternal() == null) {            
            infoGrantOwner.getPersonInfo().setUsername("T" + System.currentTimeMillis());
            person = new Person(infoGrantOwner.getPersonInfo(), country);            
        } else {
            person = (Person) rootDomainObject.readPartyByOID(infoGrantOwner.getPersonInfo()
                    .getIdInternal());
            person.edit(infoGrantOwner.getPersonInfo(), country);
        }

        // verify if person is new
        if (infoGrantOwner.getPersonInfo().getIdInternal() != null){
            grantOwner = checkIfGrantOwnerExists(infoGrantOwner.getGrantOwnerNumber());
        }

        // create or edit grantOwner information
        Integer maxNumber = null;
        if (grantOwner == null) {
            
            maxNumber = GrantOwner.readMaxGrantOwnerNumber();
            grantOwner = new GrantOwner();

            if (!person.hasRole(RoleType.PERSON)) {
                person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
            }
            person.addPersonRoles(Role.getRoleByRoleType(RoleType.GRANT_OWNER));
        }        
        grantOwner = prepareGrantOwner(grantOwner, person, infoGrantOwner, maxNumber);
       
        if (infoGrantOwner.getPersonInfo().getIdInternal() == null) {   
            person.changeUsername(generateGrantOwnerPersonUsername(grantOwner.getNumber()));
        }
        return grantOwner.getIdInternal();
    }
}
