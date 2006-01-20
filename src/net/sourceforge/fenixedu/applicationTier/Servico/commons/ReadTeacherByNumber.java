package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

public class ReadTeacherByNumber extends Service {

    public InfoTeacher run(Integer teacherNumber) throws ExcepcaoPersistencia {
        IPersistentTeacher teacherDAO = persistentSupport.getIPersistentTeacher();

        final Teacher teacher = teacherDAO.readByNumber(teacherNumber);
        return (teacher != null) ? InfoTeacher.newInfoFromDomain(teacher) : null;
    }

}