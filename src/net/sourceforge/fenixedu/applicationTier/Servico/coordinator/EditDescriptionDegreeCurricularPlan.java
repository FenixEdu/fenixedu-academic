package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão Created on 17/Nov/2003
 */
public class EditDescriptionDegreeCurricularPlan implements IService {

    public InfoDegreeCurricularPlan run(Integer infoExecutionDegreeId,
            InfoDegreeCurricularPlan newInfoDegreeCP) throws FenixServiceException {
        if (infoExecutionDegreeId == null || newInfoDegreeCP == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
        IDegreeCurricularPlan degreeCP = null;
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();

            degreeCP = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOID(
                    DegreeCurricularPlan.class, newInfoDegreeCP.getIdInternal(), true);
            if (degreeCP == null) {
                throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
            }

            degreeCP.setDescription(newInfoDegreeCP.getDescription());
            degreeCP.setDescriptionEn(newInfoDegreeCP.getDescriptionEn());

            infoDegreeCurricularPlan = Cloner
                    .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCP);
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return infoDegreeCurricularPlan;
    }

    public InfoDegreeCurricularPlan run(InfoDegreeCurricularPlan newInfoDegreeCP)
            throws FenixServiceException {
        if (newInfoDegreeCP == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
        IDegreeCurricularPlan degreeCP = null;
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();

            degreeCP = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOID(
                    DegreeCurricularPlan.class, newInfoDegreeCP.getIdInternal(), true);
            if (degreeCP == null) {
                throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
            }

            degreeCP.setDescription(newInfoDegreeCP.getDescription());
            degreeCP.setDescriptionEn(newInfoDegreeCP.getDescriptionEn());

            infoDegreeCurricularPlan = Cloner
                    .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCP);
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return infoDegreeCurricularPlan;
    }
}