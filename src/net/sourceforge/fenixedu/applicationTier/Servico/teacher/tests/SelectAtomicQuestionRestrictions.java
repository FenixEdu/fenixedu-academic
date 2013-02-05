package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import pt.ist.fenixWebFramework.services.Service;

public class SelectAtomicQuestionRestrictions extends FenixService {
    @Service
    public static void run(NewTestModel testModel, List<NewModelRestriction> atomicRestrictions, NewModelGroup destinationGroup,
            Double value) throws FenixServiceException {
        testModel.selectAtomicQuestionRestrictions(atomicRestrictions, destinationGroup, value);
    }
}