package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class EditWrittenEvaluationAuthorization extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        final IUserView userView = getRemoteUser(request);

        if (!userView.hasRoleType(RoleType.TIME_TABLE_MANAGER)) {
            final Object[] arguments = getServiceCallArguments(request);
            final IWrittenEvaluation writtenEvaluation = readWrittenEvaluation(arguments);

            if (writtenEvaluation.getAssociatedRoomOccupation().size() > 0) {
                throw new NotAuthorizedFilterException("written.evaluation.has.alocated.rooms");
            }
        }
    }

    private IWrittenEvaluation readWrittenEvaluation(final Object[] arguments) throws ExcepcaoPersistencia {
        final Integer writtenEvaluationID = getWrittenEvaluationID(arguments);
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();
        return (IWrittenEvaluation) persistentObject.readByOID(WrittenEvaluation.class, writtenEvaluationID);
    }

    private Integer getWrittenEvaluationID(final Object[] arguments) {
        return (Integer) ((arguments.length == 2) ? arguments[1] : arguments[7]);
    }

}
