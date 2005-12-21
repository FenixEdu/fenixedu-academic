/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.IGroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import net.sourceforge.fenixedu.domain.department.CompetenceCourseMembersGroup;
import net.sourceforge.fenixedu.domain.department.ICompetenceCourseMembersGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UpdateMembersGroup implements IService {

    public void run(IGroupUnion membersGroup,
            Integer[] personIDsToAdd, Integer[] personGroupsIDsToRemove) throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPerson creator = AccessControl.getUserView().getPerson();

        if (personIDsToAdd != null) {
            for (Integer personID : personIDsToAdd) {
                IPerson person = (IPerson) ps.getIPersistentObject().readByOID(Person.class, personID);
                if (!membersGroup.isMember(person)) {
                    DomainFactory.makePersonGroup(creator, person, membersGroup);
                }
            }
        }

        if (personGroupsIDsToRemove != null) {
            for (Integer personID : personGroupsIDsToRemove) {
                IUserGroup userGroup = (IUserGroup) ps.getIPersistentObject().readByOID(UserGroup.class,
                        personID);
                membersGroup.removePart(userGroup);
            }
        }

    }

}
