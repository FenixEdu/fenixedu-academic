/*
 * Created on Feb 25, 2004
 *
 */
package ServidorAplicacao.Servico.student.delegate;

import java.util.HashMap;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.student.InfoDelegate;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import Dominio.student.IDelegate;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.student.IPersistentDelegate;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *
 */
public class ReadDelegate implements IService
{
    public InfoDelegate run(HashMap hashMap) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentDelegate persistentDelegate = sp.getIPersistentDelegate();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
         
            String user = (String) hashMap.get("user");
            IStudent student = persistentStudent.readByUsername(user);
            IDelegate delegate = persistentDelegate.readByStudent(student);

            return Cloner.copyIDelegate2InfoDelegate(delegate);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }
    }
}
