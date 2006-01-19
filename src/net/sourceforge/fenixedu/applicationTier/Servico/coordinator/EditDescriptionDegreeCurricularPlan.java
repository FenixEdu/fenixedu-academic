package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Tânia Pousão Created on 17/Nov/2003
 */
public class EditDescriptionDegreeCurricularPlan implements IService {

    public InfoDegreeCurricularPlan run(Integer infoExecutionDegreeId,
            InfoDegreeCurricularPlan infoDegreeCurricularPlan) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoExecutionDegreeId == null || infoDegreeCurricularPlan == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                .getIPersistentDegreeCurricularPlan();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
                .readByOID(DegreeCurricularPlan.class, infoDegreeCurricularPlan.getIdInternal());
        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
        }

        degreeCurricularPlan.setDescription(infoDegreeCurricularPlan.getDescription());
        degreeCurricularPlan.setDescriptionEn(infoDegreeCurricularPlan.getDescriptionEn());

        InfoDegreeCurricularPlan resultInfoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);
        resultInfoDegreeCurricularPlan.setInfoDegree(new InfoDegree());
        resultInfoDegreeCurricularPlan.getInfoDegree().setIdInternal(
                degreeCurricularPlan.getDegree().getIdInternal());

        return resultInfoDegreeCurricularPlan;
    }

    public InfoDegreeCurricularPlan run(InfoDegreeCurricularPlan infoDegreeCurricularPlan)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (infoDegreeCurricularPlan == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                .getIPersistentDegreeCurricularPlan();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
                .readByOID(DegreeCurricularPlan.class, infoDegreeCurricularPlan.getIdInternal());
        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);
        }

        degreeCurricularPlan.setDescription(infoDegreeCurricularPlan.getDescription());
        degreeCurricularPlan.setDescriptionEn(infoDegreeCurricularPlan.getDescriptionEn());

        InfoDegreeCurricularPlan resultInfoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);
        resultInfoDegreeCurricularPlan.setInfoDegree(new InfoDegree());
        resultInfoDegreeCurricularPlan.getInfoDegree().setIdInternal(
                degreeCurricularPlan.getDegree().getIdInternal());

        return resultInfoDegreeCurricularPlan;
    }
}