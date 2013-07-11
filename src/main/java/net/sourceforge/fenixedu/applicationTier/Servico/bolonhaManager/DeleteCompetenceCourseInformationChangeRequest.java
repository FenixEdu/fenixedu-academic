package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;


import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteCompetenceCourseInformationChangeRequest {

    @Checked("RolePredicates.BOLONHA_MANAGER_PREDICATE")
    @Atomic
    public static void run(CompetenceCourseInformationChangeRequest request) {
        request.delete();
    }
}