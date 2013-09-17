package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import pt.ist.fenixframework.Atomic;

public class SelectQuestionGroupRestriction {
    @Atomic
    public static void run(NewTestModel testModel, NewModelRestriction atomicRestriction, NewModelGroup destinationGroup,
            Integer count, Double value) throws FenixServiceException {
        testModel.selectQuestionGroupRestriction(atomicRestriction, destinationGroup, count, value);
    }
}