package net.sourceforge.fenixedu.applicationTier.Servico.student.gaugingTests.physics;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.GaugingTestResult;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class readGaugingTestsResults extends Service {

    public InfoGaugingTestResult run(IUserView userView) throws FenixServiceException,
            ExcepcaoPersistencia {
       
        Person person = Person.readPersonByUsername(userView.getUtilizador());

        Registration registration = person.readStudentByDegreeType(DegreeType.DEGREE);
        if (registration == null) {
            return null;
        }
        
        GaugingTestResult gaugingTestsResult = registration.getAssociatedGaugingTestResult();
		if (gaugingTestsResult != null) {
			return InfoGaugingTestResult.newInfoFromDomain(gaugingTestsResult);
		}

		return null;
    }
}