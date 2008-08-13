package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadTeacherByUsername extends Service {

    public InfoTeacher run(String username) {        
        final Teacher teacher = Teacher.readTeacherByUsername(username);

        if (teacher != null) {
            return InfoTeacher.newInfoFromDomain(teacher);
        }
        return null;
    }
}