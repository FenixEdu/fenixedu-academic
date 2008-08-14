package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author naat
 */
public class ReadGuidedMasterDegreeThesisByTeacherIDAndExecutionYearID extends Service {

    public List<MasterDegreeThesisDataVersion> run(Integer teacherID, Integer executionYearID) throws FenixServiceException {
	Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
	List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions;

	if (executionYearID == null) {
	    masterDegreeThesisDataVersions = teacher.getAllGuidedMasterDegreeThesis();
	} else {
	    ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

	    masterDegreeThesisDataVersions = teacher.getGuidedMasterDegreeThesisByExecutionYear(executionYear);
	}

	return masterDegreeThesisDataVersions;

    }
}