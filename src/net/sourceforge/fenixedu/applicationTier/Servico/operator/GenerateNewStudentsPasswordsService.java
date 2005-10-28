/*
 * Created on Sep 8, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.RandomStringGenerator;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GenerateNewStudentsPasswordsService implements IService {
    
    public List run(Integer fromNumber, Integer toNumber) throws ExistingPersistentException,
            ExcepcaoPersistencia {

        List infoPersonList = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        List studentsList = persistentStudent.readAllBetweenNumbers(fromNumber, toNumber);
        Set<IStudent> studentsUniqueList = new HashSet(studentsList);

        for (IStudent student : studentsUniqueList) {

            IPerson person = student.getPerson();
            boolean isFirstTimeStudent = person.hasRole(RoleType.FIRST_TIME_STUDENT);
                        
            if (isFirstTimeStudent) {
                String password = GeneratePassword.getInstance().generatePassword(person);

                InfoPerson infoPerson = InfoPerson.newInfoFromDomain(person);
                infoPerson.setPassword(password);
                infoPersonList.add(infoPerson);

                person.setPassword(PasswordEncryptor.encryptPassword(password));
            }
        }

        return infoPersonList;

    }

}