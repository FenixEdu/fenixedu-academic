package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author naat
 */
public class ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID implements IService {

    public List<IMasterDegreeThesisDataVersion> run(Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = persistenceSupport.getIPersistentTeacher();
        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();

        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        List<IMasterDegreeThesisDataVersion> masterDegreeThesisDataVersions;

        if (executionYearID == null) {
            masterDegreeThesisDataVersions = teacher.getAllGuidedMasterDegreeThesis();
        } else {
            IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);

            masterDegreeThesisDataVersions = teacher
                    .getGuidedMasterDegreeThesisByExecutionYear(executionYear);
        }

        return masterDegreeThesisDataVersions;

    }
}