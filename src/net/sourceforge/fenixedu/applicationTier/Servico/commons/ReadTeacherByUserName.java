package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author jpvl
 */

public class ReadTeacherByUserName implements IService {

    /**
     *  
     */
    public ReadTeacherByUserName() {

    }

    public InfoTeacher run(String userName) {

        InfoTeacher infoTeacher = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

            ITeacher teacher = teacherDAO.readTeacherByUsername(userName);
            if (teacher != null) {
                infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        return infoTeacher;
    }

}