package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.kerberos.Script;

public class GeneratePasswordKerberos extends GeneratePasswordBase {

    public String generatePassword(Person person) {
        try {
            String password = randPass.getPass(PropertiesManager.getIntegerProperty("passSize"));
            if (person.hasIstUsername()) {
                if (person.getIsPassInKerberos()) {
                    Script.changeKerberosPass(person.getIstUsername(), password);
                } else {
                    person.setIsPassInKerberos(true);
                    Script.createUser(person.getIstUsername(), password);
                }
            }
            return password;
        } catch (Exception rte) {
            throw new RuntimeException(rte);
        }
    }
}
