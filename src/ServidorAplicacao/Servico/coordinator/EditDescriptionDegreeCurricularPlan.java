package ServidorAplicacao.Servico.coordinator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

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