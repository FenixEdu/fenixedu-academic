package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

public class ReadTeacherById extends FenixService {
    // MARK DELTA 
    // ReadTeacherById
    @Service
    public static InfoTeacher run(String teacherId) {
        final Teacher teacher = Teacher.readByIstId(teacherId);
        return (teacher != null) ? InfoTeacher.newInfoFromDomain(teacher) : null;
    }

}