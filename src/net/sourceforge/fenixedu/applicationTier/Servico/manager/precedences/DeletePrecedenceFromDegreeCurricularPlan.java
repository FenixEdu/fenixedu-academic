package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrecedence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeletePrecedenceFromDegreeCurricularPlan implements IService {

    public DeletePrecedenceFromDegreeCurricularPlan() {
    }

    public void run(Integer precedenceID) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();

            IPrecedence precedence = (IPrecedence) precedenceDAO.readByOID(Precedence.class,
                    precedenceID);

            List restrictions = precedence.getRestrictions();
            int size = restrictions.size();

            for (int i = 0; i < size; i++) {
                IRestriction restriction = (IRestriction) restrictions.get(i);
                precedenceDAO.deleteByOID(Restriction.class, restriction.getIdInternal());
            }

            precedenceDAO.deleteByOID(Precedence.class, precedenceID);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}