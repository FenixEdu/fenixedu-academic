package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;


import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteCompetenceCourseInformationChangeRequest {

    @Atomic
    public static void run(CompetenceCourseInformationChangeRequest request) {
        check(RolePredicates.BOLONHA_MANAGER_PREDICATE);
        request.delete();
    }
}