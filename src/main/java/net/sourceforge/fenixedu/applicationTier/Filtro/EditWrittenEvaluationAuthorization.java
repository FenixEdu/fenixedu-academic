package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class EditWrittenEvaluationAuthorization extends Filtro {

    public static final EditWrittenEvaluationAuthorization instance = new EditWrittenEvaluationAuthorization();

    public void execute(Object[] parameters) throws Exception {
        final IUserView userView = AccessControl.getUserView();

        if (!userView.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {

            final Object[] arguments = parameters;
            final WrittenEvaluation writtenEvaluation = readWrittenEvaluation(arguments);

            if (writtenEvaluation.getWrittenEvaluationSpaceOccupations().size() > 0) {
                throw new NotAuthorizedException("written.evaluation.has.alocated.rooms");
            }
        }
    }

    private WrittenEvaluation readWrittenEvaluation(final Object[] arguments) {
        final Integer writtenEvaluationID = getWrittenEvaluationID(arguments);
        return (WrittenEvaluation) RootDomainObject.getInstance().readEvaluationByOID(writtenEvaluationID);
    }

    private Integer getWrittenEvaluationID(final Object[] arguments) {
        return (Integer) ((arguments.length == 2) ? arguments[1] : arguments[7]);
    }
}
