/*
 * Created on Sep 8, 2004
 *
 */
package ServidorAplicacao.Servico.operator;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import Dominio.IPessoa;
import Dominio.IStudent;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.RandomStringGenerator;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GenerateNewStudentsPasswordsService implements IService {

    public GenerateNewStudentsPasswordsService() {
    }

    public List run(Integer fromNumber, Integer toNumber) throws ExcepcaoInexistente,
            FenixServiceException, InvalidPasswordServiceException, ExistingPersistentException,
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

            IPessoa person = student.getPerson();
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