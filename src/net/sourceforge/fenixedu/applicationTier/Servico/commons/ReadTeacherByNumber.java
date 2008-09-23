package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadTeacherByNumber extends FenixService {

    public InfoTeacher run(Integer teacherNumber) {
	final Teacher teacher = Teacher.readByNumber(teacherNumber);
	return (teacher != null) ? InfoTeacher.newInfoFromDomain(teacher) : null;
    }

}