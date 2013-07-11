package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import pt.ist.fenixframework.Atomic;

public class DeleteTestGroup {
    @Atomic
    public static void run(NewTestGroup testGroup) throws FenixServiceException {
        testGroup.delete();
    }
}