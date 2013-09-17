package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;


import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;

public class ReadTeacherByUsername {

    @Atomic
    public static InfoTeacher run(String username) {
        final Teacher teacher = Teacher.readTeacherByUsername(username);

        if (teacher != null) {
            return InfoTeacher.newInfoFromDomain(teacher);
        }
        return null;
    }
}