package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerTurma {

    @Atomic
    public static InfoClass run(String className, InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final ExecutionSemester executionSemester =
                FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());

        final SchoolClass turma = executionDegree.findSchoolClassesByExecutionPeriodAndName(executionSemester, className);

        if (turma != null) {
            return InfoClass.newInfoFromDomain(turma);
        }

        return null;
    }

}