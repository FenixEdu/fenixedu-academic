package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

public class EditWrittenEvaluationAuthorization extends Filtro {

    public static final EditWrittenEvaluationAuthorization instance = new EditWrittenEvaluationAuthorization();

    public void execute(String writtenEvaluationId) throws NotAuthorizedException {
        final User userView = Authenticate.getUser();

        if (!userView.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {

            final WrittenEvaluation writtenEvaluation = readWrittenEvaluation(writtenEvaluationId);

            if (writtenEvaluation.getWrittenEvaluationSpaceOccupations().size() > 0) {
                throw new NotAuthorizedException("written.evaluation.has.allocated.rooms");
            }
        }
    }

    private WrittenEvaluation readWrittenEvaluation(String writtenEvaluationId) {
        return (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationId);
    }

}
