package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadTeachersByNumbers extends Service {

    public Collection<InfoTeacher> run(Collection<Integer> teacherNumbers) throws ExcepcaoPersistencia {
        Collection<InfoTeacher> infoTeachers = new ArrayList(teacherNumbers.size());
        Collection<Teacher> teachers = Teacher.readByNumbers(teacherNumbers);

        for (Teacher teacher : teachers) {
            infoTeachers.add(InfoTeacher.newInfoFromDomain(teacher));
        }

        return infoTeachers;
    }

}