package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import pt.ist.fenixframework.Atomic;

public class CreateModelGroup {
    @Atomic
    public static NewModelGroup run(NewModelGroup parentGroup, String name) throws FenixServiceException {
        return new NewModelGroup(parentGroup, name);
    }
}