/*
 * Created on 20/Mar/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadTeacherByUsername implements IService {

    /**
     * Executes the service. Returns the current collection of sitios names.
     * 
     * @throws ExcepcaoInexistente
     *             is there is none sitio.
     */

    public InfoTeacher run(String username) throws FenixServiceException {
        ITeacher teacher = null;
        InfoTeacher infoTeacher = null;
        try {

            ISuportePersistente sp;

            sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            teacher = persistentTeacher.readTeacherByUsername(username);
            if (teacher != null) {
                infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoTeacher;
    }
}