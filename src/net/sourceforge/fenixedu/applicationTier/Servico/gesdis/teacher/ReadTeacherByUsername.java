/*
 * Created on 20/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 * 
 */
public class ReadTeacherByUsername implements IService {

    public InfoTeacher run(String username) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

        final ITeacher teacher = persistentTeacher.readTeacherByUsername(username);
        InfoTeacher infoTeacher = null;
        if (teacher != null) {
            infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
        }
        return infoTeacher;
    }
}