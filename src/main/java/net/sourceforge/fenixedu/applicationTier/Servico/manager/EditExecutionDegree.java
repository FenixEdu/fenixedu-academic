package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeEditor;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditExecutionDegree {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(InfoExecutionDegreeEditor infoExecutionDegree) throws FenixServiceException {

        final ExecutionDegree oldExecutionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getExternalId());
        if (oldExecutionDegree == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionDegree", null);
        }

        final Campus campus = (Campus) RootDomainObject.getInstance().readResourceByOID(infoExecutionDegree.getInfoCampus().getExternalId());
        if (campus == null) {
            throw new NonExistingServiceException("message.nonExistingCampus", null);
        }

        final ExecutionYear executionYear =
                RootDomainObject.getInstance().readExecutionYearByOID(infoExecutionDegree.getInfoExecutionYear().getExternalId());
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