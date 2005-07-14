/*
 * Created on 29/10/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.base.CreatePersonBaseClass;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class EditGrantOwner extends CreatePersonBaseClass implements IService {

    public EditGrantOwner() {
    }

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
        pGrantOwner.simpleLockWrite(grantOwner);
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

    /**
     * Executes the service.
     */
    public Integer run(InfoGrantOwner infoGrantOwner) throws FenixServiceException {
        ISuportePersistente sp = null;
        IPessoaPersistente pPerson = null;
        IPersistentGrantOwner pGrantOwner = null;
        IPersistentPersonRole pPersonRole = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("Unable to dao factory!", e);
        }
        pGrantOwner = sp.getIPersistentGrantOwner();
        pPersonRole = sp.getIPersistentPersonRole();
        pPerson = sp.getIPessoaPersistente();

        try {
            IPerson person = null;
            IGrantOwner grantOwner = null;

            //create or edit person information
            person = CreatePersonBaseClass.createPersonBase(person, infoGrantOwner.getPersonInfo(), sp,
                    pPerson, pPersonRole);

            //verify if person is new
            if (person.getUsername() != null)
                grantOwner = checkIfGrantOwnerExists(infoGrantOwner.getGrantOwnerNumber(), pGrantOwner);

            //create or edit grantOwner information
            if (grantOwner == null) {
                grantOwner = new GrantOwner();

                pPerson.lockWrite(person);
                IPersistentRole persistentRole = sp.getIPersistentRole();
                person.getPersonRoles().add(persistentRole.readByRoleType(RoleType.GRANT_OWNER));
            }
            grantOwner = prepareGrantOwner(grantOwner, person, infoGrantOwner, pGrantOwner);

            //Generate the GrantOwner's Person Username
            if (person.getUsername() == null)
                person.setUsername(generateGrantOwnerPersonUsername(grantOwner.getNumber()));

            return grantOwner.getIdInternal();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
    }
}
