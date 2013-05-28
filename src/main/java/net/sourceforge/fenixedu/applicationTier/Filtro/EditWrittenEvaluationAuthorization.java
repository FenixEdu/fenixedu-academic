package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class EditWrittenEvaluationAuthorization extends Filtro {

    public static final EditWrittenEvaluationAuthorization instance = new EditWrittenEvaluationAuthorization();

    public void execute(Integer writtenEvaluationId) throws NotAuthorizedException {
        final IUserView userView = AccessControl.getUserView();

        if (!userView.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {

            final WrittenEvaluation writtenEvaluation = readWrittenEvaluation(writtenEvaluationId);

            if (writtenEvaluation.getWrittenEvaluationSpaceOccupations().size() > 0) {
                throw new NotAuthorizedException("written.evaluation.has.alocated.rooms");
            }
        }
    }

    private WrittenEvaluation readWrittenEvaluation(Integer writtenEvaluationId) {
        return (WrittenEvaluation) AbstractDomainObject.fromExternalId(writtenEvaluationId);
    }

}
