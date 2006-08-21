/*
 * Created on Feb 25, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.HashMap;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoDelegate;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadDelegate extends Service {

    public InfoDelegate run(HashMap hashMap) throws ExcepcaoPersistencia {

        final String user = (String) hashMap.get("user");
        final Registration registration = Registration.readByUsername(user);

        Delegate delegate = null;
        if (!registration.getDelegate().isEmpty()) {
            delegate = registration.getDelegate().get(0);
        }

        return InfoDelegate.newInfoFromDomain(delegate);
    }

}