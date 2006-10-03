package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SelectAtomicQuestionRestrictions extends Service {
	public void run(NewTestModel testModel, List<NewModelRestriction> atomicRestrictions, NewModelGroup destinationGroup, Double value)
			throws FenixServiceException, ExcepcaoPersistencia {
		testModel.selectAtomicQuestionRestrictions(atomicRestrictions, destinationGroup, value);
	}
}
