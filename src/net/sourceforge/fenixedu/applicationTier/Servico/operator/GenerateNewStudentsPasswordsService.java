/*
 * Created on Sep 8, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GenerateNewStudentsPasswordsService extends Service {

    public List run(Integer fromNumber, Integer toNumber) {

        List infoPersonList = new ArrayList();

        List studentsList = Registration.readAllStudentsBetweenNumbers(fromNumber, toNumber);
        Set<Registration> studentsUniqueList = new HashSet(studentsList);

        for (Registration registration : studentsUniqueList) {

            Person person = registration.getPerson();
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