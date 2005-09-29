package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditGrantOwner implements IService {

    private String generateGrantOwnerPersonUsername(Integer grantOwnerNumber) {
        String result = null;
        result = "b" + grantOwnerNumber.toString();
        return result;
    }

    private IGrantOwner checkIfGrantOwnerExists(Integer grantOwnerNumber,
            IPersistentGrantOwner persistentGrantOwner) throws FenixServiceException {
        IGrantOwner grantOwner = null;
        try {
            grantOwner = persistentGrantOwner.readGrantOwnerByNumber(grantOwnerNumber);
        } catch (ExcepcaoPersistencia persistentException) {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return grantOwner;
    }

    private IGrantOwner prepareGrantOwner(IGrantOwner grantOwner, IPerson person,
            InfoGrantOwner infoGrantOwner, IPersistentGrantOwner pGrantOwner)
            throws ExcepcaoPersistencia {
        grantOwner.setPerson(person);
        grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
        grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());

        if (infoGrantOwner.getGrantOwnerNumber() == null) {
            //Generate the GrantOwner's number
            Integer maxNumber = pGrantOwner.readMaxGrantOwnerNumber();
            int aux = maxNumber.intValue() + 1;
            Integer nextNumber = new Integer(aux);
            grantOwner.setNumber(nextNumber);
        } else
            grantOwner.setNumber(infoGrantOwner.getGrantOwnerNumber());

        return grantOwner;
    }

    protected boolean isNew(IDomainObject domainObject) {
        Integer objectId = domainObject.getIdInternal();
        return ((objectId == null) || objectId.equals(new Integer(0)));
    }

    public Integer run(InfoGrantOwner infoGrantOwner) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = null;
        IPersistentGrantOwner pGrantOwner = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("Unable to dao factory!", e);
        }
        pGrantOwner = sp.getIPersistentGrantOwner();
        
        IPerson person = null;
        IGrantOwner grantOwner = null;
        ICountry country = null;
        
        if (infoGrantOwner.getPersonInfo().getInfoPais() != null) {
            country = (ICountry) sp.getIPersistentCountry().readByOID(Country.class, infoGrantOwner.getPersonInfo().getInfoPais().getIdInternal());
        } else {
            //If the person country is undefined it is set to default "PORTUGUESA NATURAL DO CONTINENTE" 
            //In a not distance future this will not be needed since the coutry can never be null
            country = (ICountry) sp.getIPersistentCountry().readCountryByNationality("PORTUGUESA NATURAL DO CONTINENTE");
        } 
        
        //create or edit person information
        if (infoGrantOwner.getPersonInfo().getIdInternal() == null) {
            person = DomainFactory.makePerson(infoGrantOwner.getPersonInfo(), country);
        }
        else {
            person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class, infoGrantOwner.getPersonInfo().getIdInternal());
            person.edit(infoGrantOwner.getPersonInfo(), country);
        }

        //verify if person is new
        if (person.getUsername() != null )
            grantOwner = checkIfGrantOwnerExists(infoGrantOwner.getGrantOwnerNumber(), pGrantOwner);

        //create or edit grantOwner information
        if (grantOwner == null) {
            
            grantOwner = DomainFactory.makeGrantOwner();

            IPersistentRole persistentRole = sp.getIPersistentRole();
            if (!person.hasRole(RoleType.PERSON)) {
            	person.getPersonRoles().add(persistentRole.readByRoleType(RoleType.PERSON));
            }
            person.getPersonRoles().add(persistentRole.readByRoleType(RoleType.GRANT_OWNER));
        }
        grantOwner = prepareGrantOwner(grantOwner, person, infoGrantOwner, pGrantOwner);

        //Generate the GrantOwner's Person Username
        if (person.getUsername() == null || person.getUsername().length() == 0)
            person.changeUsername(generateGrantOwnerPersonUsername(grantOwner.getNumber()), (List<IPerson>) sp.getIPessoaPersistente().readAll(Person.class));
        return grantOwner.getIdInternal();
    }
}
