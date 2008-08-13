package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteTeacherDegreeFinalProjectStudentByOID extends Service {

    public void run(Integer teacherDegreeFinalProjectStudentID) throws FenixServiceException {
        final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = rootDomainObject.readTeacherDegreeFinalProjectStudentByOID(teacherDegreeFinalProjectStudentID);
        if (teacherDegreeFinalProjectStudent == null) {
            throw new FenixServiceException("message.noTeacherDegreeFinalProjectStudent");
        }
        teacherDegreeFinalProjectStudent.delete();
    }
    
}