package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author jpvl
 */

public class ReadTeacherByNumber implements IService {

    public InfoTeacher run(Integer teacherNumber) {

        InfoTeacher infoTeacher = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

            ITeacher teacher = teacherDAO.readByNumber(teacherNumber);
            if (teacher != null) {
                infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        return infoTeacher;
    }

}