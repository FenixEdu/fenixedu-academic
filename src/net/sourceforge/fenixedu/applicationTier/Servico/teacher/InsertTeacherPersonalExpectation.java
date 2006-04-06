package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author naat
 */
public class InsertTeacherPersonalExpectation extends Service {

    public void run(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation, Integer teacherID,
            Integer executionYearID) throws ExcepcaoPersistencia, FenixServiceException {
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionYear executionYear = (ExecutionYear) persistentObject.readByOID(
                ExecutionYear.class, executionYearID);
        
        teacher.createTeacherPersonalExpectation(infoTeacherPersonalExpectation, executionYear);

    }
}