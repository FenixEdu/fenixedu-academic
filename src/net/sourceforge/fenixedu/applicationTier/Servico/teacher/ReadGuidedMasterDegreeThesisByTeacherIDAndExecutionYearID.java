package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author naat
 */
public class ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID extends Service {

    public List<MasterDegreeThesisDataVersion> run(Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia, FenixServiceException {

        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        IPersistentExecutionYear persistentExecutionYear = persistentSupport
                .getIPersistentExecutionYear();

        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions;

        if (executionYearID == null) {
            masterDegreeThesisDataVersions = teacher.getAllGuidedMasterDegreeThesis();
        } else {
            ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);

            masterDegreeThesisDataVersions = teacher
                    .getGuidedMasterDegreeThesisByExecutionYear(executionYear);
        }

        return masterDegreeThesisDataVersions;

    }
}