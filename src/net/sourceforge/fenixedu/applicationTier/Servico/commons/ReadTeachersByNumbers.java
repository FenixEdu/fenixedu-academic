package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadTeachersByNumbers implements IService {

    public Collection<InfoTeacher> run(Collection<Integer> teacherNumbers) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Collection<InfoTeacher> infoTeachers = new ArrayList(teacherNumbers.size());
        Collection<Teacher> teachers = sp.getIPersistentTeacher().readByNumbers(teacherNumbers);

        for (Teacher teacher : teachers) {
            infoTeachers.add(InfoTeacherWithPerson.newInfoFromDomain(teacher));
        }

        return infoTeachers;
    }

}