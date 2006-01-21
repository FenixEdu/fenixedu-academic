/*
 * Created on Feb 25, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.HashMap;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoDelegate;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadDelegate extends Service {
 
    public InfoDelegate run(HashMap hashMap) throws ExcepcaoPersistencia {
        final IPersistentDelegate persistentDelegate = persistentSupport.getIPersistentDelegate();
        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        final String user = (String) hashMap.get("user");
        final Student student = persistentStudent.readByUsername(user);
        final Delegate delegate = persistentDelegate.readByStudent(student);

        return InfoDelegate.newInfoFromDomain(delegate);
    }

}