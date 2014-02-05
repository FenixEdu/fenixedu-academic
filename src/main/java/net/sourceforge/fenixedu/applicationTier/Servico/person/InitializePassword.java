package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

public class InitializePassword {

    private static final String NAMESPACE_URI = "";

    private static final String LOCAL_PART = "";

    @Atomic
    public static void run(User user, String password) throws InvalidPasswordServiceException, Exception {
        user.changePassword(password);
    }

    private static void run2() throws InvalidPasswordServiceException {
        try {
            QName qname = new QName(NAMESPACE_URI, LOCAL_PART);
            URL serviceUrl = new URL("http://localhost:9999/ws/hello?wsdl");
            Service service = Service.create(serviceUrl, qname);

            Class clazz = null;
            service.getPort(clazz);
        } catch (Exception e) {
            throw new InvalidPasswordServiceException(e.getCause());
        }
    }

}
