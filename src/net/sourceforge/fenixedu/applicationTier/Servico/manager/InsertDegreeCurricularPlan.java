/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
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
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                    .getIPersistentDegreeCurricularPlan();

            ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();
            IDegree degree = persistentDegree.readByIdInternal(infoDegreeCurricularPlan.getInfoDegree()
                    .getIdInternal());
            if (degree == null)
                throw new NonExistingServiceException();

            IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
            persistentDegreeCurricularPlan.simpleLockWrite(degreeCurricularPlan);
            degreeCurricularPlan.setName(infoDegreeCurricularPlan.getName());
            degreeCurricularPlan.setDegree(degree);
            degreeCurricularPlan.setState(infoDegreeCurricularPlan.getState());
            degreeCurricularPlan.setInitialDate(infoDegreeCurricularPlan.getInitialDate());
            degreeCurricularPlan.setEndDate(infoDegreeCurricularPlan.getEndDate());
            degreeCurricularPlan.setDegreeDuration(infoDegreeCurricularPlan.getDegreeDuration());
            degreeCurricularPlan.setMinimalYearForOptionalCourses(infoDegreeCurricularPlan
                    .getMinimalYearForOptionalCourses());
            degreeCurricularPlan.setNeededCredits(infoDegreeCurricularPlan.getNeededCredits());
            degreeCurricularPlan.setMarkType(infoDegreeCurricularPlan.getMarkType());
            degreeCurricularPlan.setNumerusClausus(infoDegreeCurricularPlan.getNumerusClausus());
            degreeCurricularPlan.setConcreteClassForStudentCurricularPlans(degree
                    .getConcreteClassForDegreeCurricularPlans());

        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException("O plano curricular com nome "
                    + infoDegreeCurricularPlan.getName(), existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}