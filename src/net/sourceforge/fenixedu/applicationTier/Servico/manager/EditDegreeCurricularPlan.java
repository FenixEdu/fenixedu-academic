/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class EditDegreeCurricularPlan implements IService {

    public void run(InfoDegreeCurricularPlan newInfoDegreeCP)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();

        final IDegreeCurricularPlan degreeCP = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                .readByOID(DegreeCurricularPlan.class, newInfoDegreeCP.getIdInternal());
        if (degreeCP == null) {
            throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
        }
		
        final IDegree degree = degreeCP.getDegree();
        if (degree == null) {
            throw new NonExistingServiceException("message.nonExistingDegree", null);
        }

		degreeCP.edit(newInfoDegreeCP.getName(), newInfoDegreeCP.getState(), newInfoDegreeCP.getInitialDate(),
				newInfoDegreeCP.getEndDate(), newInfoDegreeCP.getDegreeDuration(), 
				newInfoDegreeCP.getMinimalYearForOptionalCourses(), newInfoDegreeCP.getNeededCredits(), 
				newInfoDegreeCP.getMarkType(), newInfoDegreeCP.getNumerusClausus(), newInfoDegreeCP.getAnotation());
    }
}