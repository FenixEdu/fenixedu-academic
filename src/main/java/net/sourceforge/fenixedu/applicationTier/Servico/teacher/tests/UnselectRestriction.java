package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import pt.ist.fenixWebFramework.services.Service;

public class UnselectRestriction {
    @Service
    public static void run(NewModelRestriction modelRestriction) throws FenixServiceException {
        NewTestModel testModel = modelRestriction.getTestModel();

        testModel.unselectRestriction(modelRestriction);
    }
}