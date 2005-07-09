package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteRestrictionFromDegreeCurricularPlan implements IService {

    public DeleteRestrictionFromDegreeCurricularPlan() {
    }

    public void run(Integer restrictionID) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentRestriction restrictionDAO = persistentSuport.getIPersistentRestriction();
			IRestriction restriction = (IRestriction)restrictionDAO.readByOID(Restriction.class, restrictionID);
			
			restriction.deleteRestriction();	
            restrictionDAO.deleteByOID(Restriction.class, restrictionID);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}