package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis.StudentThesisAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class AcceptThesisDeclaration {

    protected void run(Thesis thesis, ThesisVisibilityType visibility, DateTime availableAfter) {
        thesis.acceptDeclaration(visibility, availableAfter);
    }

    // Service Invokers migrated from Berserk

    private static final AcceptThesisDeclaration serviceInstance = new AcceptThesisDeclaration();

    @Atomic
    public static void runAcceptThesisDeclaration(Thesis thesis, ThesisVisibilityType visibility, DateTime availableAfter)
            throws NotAuthorizedException {
        StudentThesisAuthorizationFilter.instance.execute(thesis);
        serviceInstance.run(thesis, visibility, availableAfter);
    }

}