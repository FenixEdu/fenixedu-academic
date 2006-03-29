/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.gaugingTests.physics;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.GaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 * 
 */

public class readGaugingTestsResults extends Service {

	public InfoGaugingTestResult run(IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentGaugingTestResult persistentGaugingTestResult = persistentSupport.getIPersistentGaugingTestResult();
		Person person = Person.readPersonByUsername(userView.getUtilizador());

		IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

		Student student = persistentStudent.readByPersonAndDegreeType(person.getIdInternal(),
				DegreeType.DEGREE);
		if (student == null) {
			return null;
		}
		GaugingTestResult gaugingTestsResult = persistentGaugingTestResult.readByStudent(student);
		if (gaugingTestsResult != null) {

			InfoGaugingTestResult infoGaugingTestResult = InfoGaugingTestResult
					.newInfoFromDomain(gaugingTestsResult);
			return infoGaugingTestResult;
		}

		return null;
	}
}