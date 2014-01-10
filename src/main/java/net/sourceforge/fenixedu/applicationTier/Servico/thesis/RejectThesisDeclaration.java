package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis.StudentThesisAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixframework.Atomic;

public class RejectThesisDeclaration {

    protected void run(Thesis thesis) {
        thesis.rejectDeclaration();
    }

    // Service Invokers migrated from Berserk

    private static final RejectThesisDeclaration serviceInstance = new RejectThesisDeclaration();

    @Atomic
    public static void runRejectThesisDeclaration(Thesis thesis) throws NotAuthorizedException {
        StudentThesisAuthorizationFilter.instance.execute(thesis);
        serviceInstance.run(thesis);
    }

}