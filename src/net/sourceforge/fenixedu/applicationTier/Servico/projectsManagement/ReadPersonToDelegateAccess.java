/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadPersonToDelegateAccess implements IService {

    public InfoPerson run(String userView, String costCenter, String username, String userNumber) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        if (person == null) {
            throw new ExcepcaoInexistente();
        } else if (!isTeacherOrEmployee(sp, person)) {
            throw new InvalidArgumentsServiceException();
        }
        return InfoPerson.newInfoFromDomain(person);
    }

    private boolean isTeacherOrEmployee(ISuportePersistente sp, Person person) throws ExcepcaoPersistencia {
        if (sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername()) == null) {
            if (sp.getIPersistentEmployee().readByPerson(person.getIdInternal()) == null) {
                return false;
            }
        }
        return true;
    }

}