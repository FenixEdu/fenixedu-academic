/*
 * Created on Sep 8, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.RandomStringGenerator;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GenerateNewStudentsPasswordsService implements IService {

    public GenerateNewStudentsPasswordsService() {
    }

    public List run(Integer fromNumber, Integer toNumber) throws ExistingPersistentException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = null;

        List studentsList = null;
        List infoPersonList = new ArrayList();
        IPersistentStudent persistentStudent = null;
        IPessoaPersistente persistentPerson = null;

        sp = SuportePersistenteOJB.getInstance();
        persistentStudent = sp.getIPersistentStudent();
        persistentPerson = sp.getIPessoaPersistente();
        studentsList = persistentStudent.readAllBetweenNumbers(fromNumber, toNumber);

        for (int iterator = 0; iterator < studentsList.size(); iterator++) {
            IStudent student = (IStudent) studentsList.get(iterator);

            IPerson person = student.getPerson();
            persistentPerson.simpleLockWrite(person);
            String password = RandomStringGenerator.getRandomStringGenerator(8);

            InfoPerson infoPerson = InfoPerson.newInfoFromDomain(person);
            infoPerson.setPassword(password);
            infoPersonList.add(infoPerson);

            person.setPassword(PasswordEncryptor.encryptPassword(password));
        }

        return infoPersonList;

    }

}