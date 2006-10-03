package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

public class GenerateTests extends Service {
	public void run(NewTestModel testModel, String name, ExecutionCourse executionCourse, Integer variations, DateTime finalDate) throws FenixServiceException,
			ExcepcaoPersistencia {
		testModel.generateTests(name, executionCourse, variations, finalDate);
	}
}
