/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;

/**
 * @author Susana Fernandes
 */
public class ReadPersonToDelegateAccess extends FenixService {

    public InfoPerson run(String userView, String costCenter, String username, BackendInstance instance, String userNumber)
            throws FenixServiceException {
        Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new ExcepcaoInexistente();
        } else if (!isTeacherOrEmployeeOrResearcherOrGrantOwner(person)) {
            throw new InvalidArgumentsServiceException();
        }
        return InfoPerson.newInfoFromDomain(person);
    }

    private boolean isTeacherOrEmployeeOrResearcherOrGrantOwner(Person person) {
        return person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.EMPLOYEE) || person.hasRole(RoleType.GRANT_OWNER)
                || (person.hasResearcher() && person.getResearcher().isActiveContractedResearcher());
    }
}