package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SelectQuestionGroupRestriction extends Service {
	public void run(NewTestModel testModel, NewModelRestriction atomicRestriction,
			NewModelGroup destinationGroup, Integer count, Double value) throws FenixServiceException,
			ExcepcaoPersistencia {
		testModel.selectQuestionGroupRestriction(atomicRestriction, destinationGroup, count, value);
	}
}
