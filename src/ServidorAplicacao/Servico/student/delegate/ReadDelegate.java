/*
 * Created on Feb 25, 2004
 *
 */
package ServidorAplicacao.Servico.student.delegate;

import java.util.HashMap;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.student.InfoDelegate;
import Dominio.IStudent;
import Dominio.student.IDelegate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.student.IPersistentDelegate;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadDelegate implements IService {
 
    public InfoDelegate run(HashMap hashMap) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        final IPersistentDelegate persistentDelegate = sp.getIPersistentDelegate();
        final IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        final String user = (String) hashMap.get("user");
        final IStudent student = persistentStudent.readByUsername(user);
        final IDelegate delegate = persistentDelegate.readByStudent(student);

        return InfoDelegate.newInfoFromDomain(delegate);
    }

}