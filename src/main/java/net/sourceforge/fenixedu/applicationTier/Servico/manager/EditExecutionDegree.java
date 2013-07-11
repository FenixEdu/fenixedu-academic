package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditExecutionDegree {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(InfoExecutionDegreeEditor infoExecutionDegree) throws FenixServiceException {

        final ExecutionDegree oldExecutionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        if (oldExecutionDegree == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionDegree", null);
        }

        final Campus campus = (Campus) FenixFramework.getDomainObject(infoExecutionDegree.getInfoCampus().getExternalId());
        if (campus == null) {
            throw new NonExistingServiceException("message.nonExistingCampus", null);
        }

        final ExecutionYear executionYear =
                FenixFramework.getDomainObject(infoExecutionDegree.getInfoExecutionYear().getExternalId());
        if (executionYear == null) {
            throw new NonExistingServiceException("message.non.existing.execution.year", null);
        }

        oldExecutionDegree.setCampus(campus);
        oldExecutionDegree.setExecutionYear(executionYear);
        if (!infoExecutionDegree.getTemporaryExamMap()) {
            oldExecutionDegree.getPublishedExamMaps().addAll(executionYear.getExecutionPeriods());
        }
    }

}