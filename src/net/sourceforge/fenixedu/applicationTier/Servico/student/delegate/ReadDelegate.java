/*
 * Created on Feb 25, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.HashMap;

import net.sourceforge.fenixedu.dataTransferObject.student.InfoDelegate;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.IDelegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadDelegate implements IService {
 
    public InfoDelegate run(HashMap hashMap) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentDelegate persistentDelegate = sp.getIPersistentDelegate();
        final IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        final String user = (String) hashMap.get("user");
        final IStudent student = persistentStudent.readByUsername(user);
        final IDelegate delegate = persistentDelegate.readByStudent(student);

        return InfoDelegate.newInfoFromDomain(delegate);
    }

}