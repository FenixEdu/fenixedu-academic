/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class InsertDegreeCurricularPlan implements IService {

    public InsertDegreeCurricularPlan() {
    }

    public void run(InfoDegreeCurricularPlan infoDegreeCurricularPlan) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();
			
            IDegree degree = (IDegree)persistentDegree.readByOID(Degree.class,infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
            if (degree == null)
                throw new NonExistingServiceException();

            new DegreeCurricularPlan(degree, infoDegreeCurricularPlan.getName(), infoDegreeCurricularPlan.getState(),
					infoDegreeCurricularPlan.getInitialDate(), infoDegreeCurricularPlan.getEndDate(), 
					infoDegreeCurricularPlan.getDegreeDuration(), infoDegreeCurricularPlan.getMinimalYearForOptionalCourses(),
					infoDegreeCurricularPlan.getNeededCredits(), infoDegreeCurricularPlan.getMarkType(),
					infoDegreeCurricularPlan.getNumerusClausus(), infoDegreeCurricularPlan.getAnotation());

        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException("O plano curricular com nome "
                    + infoDegreeCurricularPlan.getName(), existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}