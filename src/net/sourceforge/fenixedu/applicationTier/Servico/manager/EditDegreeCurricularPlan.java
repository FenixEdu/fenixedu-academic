/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class EditDegreeCurricularPlan implements IService {

    public EditDegreeCurricularPlan() {
    }

    public void run(InfoDegreeCurricularPlan newInfoDegreeCP) throws FenixServiceException {

        IDegree degree = null;
        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
        IDegreeCurricularPlan oldDegreeCP = null;
        ICursoPersistente persistentDegree = null;
        String newName = null;

        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
            oldDegreeCP = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOID(
                    DegreeCurricularPlan.class, newInfoDegreeCP.getIdInternal());

            if (oldDegreeCP == null) {
                throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
            }
            persistentDegree = persistentSuport.getICursoPersistente();
            Integer degreeId = oldDegreeCP.getDegree().getIdInternal();
            degree = persistentDegree.readByIdInternal(degreeId);

            if (degree == null) {
                throw new NonExistingServiceException("message.nonExistingDegree", null);
            }
            newName = newInfoDegreeCP.getName();

            persistentDegreeCurricularPlan.simpleLockWrite(oldDegreeCP);
            oldDegreeCP.setName(newName);
            oldDegreeCP.setDegree(degree);
            oldDegreeCP.setState(newInfoDegreeCP.getState());
            oldDegreeCP.setInitialDate(newInfoDegreeCP.getInitialDate());
            oldDegreeCP.setEndDate(newInfoDegreeCP.getEndDate());
            oldDegreeCP.setDegreeDuration(newInfoDegreeCP.getDegreeDuration());
            oldDegreeCP.setMinimalYearForOptionalCourses(newInfoDegreeCP
                    .getMinimalYearForOptionalCourses());
            oldDegreeCP.setNeededCredits(newInfoDegreeCP.getNeededCredits());
            oldDegreeCP.setMarkType(newInfoDegreeCP.getMarkType());
            oldDegreeCP.setNumerusClausus(newInfoDegreeCP.getNumerusClausus());

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}