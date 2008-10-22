package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

public class ReadTeacherByNumber extends FenixService {

    @Service
    public static InfoTeacher run(Integer teacherNumber) {
	final Teacher teacher = Teacher.readByNumber(teacherNumber);
	return (teacher != null) ? InfoTeacher.newInfoFromDomain(teacher) : null;
    }

}