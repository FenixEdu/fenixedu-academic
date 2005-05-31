/*
 * Created on 06/Feb/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllTeachersNumberAndName implements IService {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

        final ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = suportePersistente.getIPersistentTeacher();
        final Collection<ITeacher> teachers = persistentTeacher.readAll(Teacher.class);

        if (teachers != null) {
            for (final ITeacher teacher : teachers) {
                InfoTeacher infoTeacher = new InfoTeacher();
                InfoPerson infoPerson = new InfoPerson();

                infoTeacher.setTeacherNumber(teacher.getTeacherNumber());
                infoTeacher.setInfoPerson(infoPerson);
                infoPerson.setNome(teacher.getPerson().getNome());

                result.add(infoTeacher);
            }
        }
        return result;
    }

}