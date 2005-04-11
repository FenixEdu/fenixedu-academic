package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão Created on 4/Nov/2003
 */
public class ReadDegreeInfoByExecutionDegree implements IService {

    public InfoDegreeInfo run(Integer infoExecutionDegreeId) throws FenixServiceException {
        InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();
        infoDegreeInfo.setIdInternal(new Integer(0));

        try {
            if (infoExecutionDegreeId == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();

            //Execution degree
            IPersistentExecutionDegree cursoExecucaoPersistente = suportePersistente
                    .getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = (IExecutionDegree) cursoExecucaoPersistente.readByOID(
                    ExecutionDegree.class, infoExecutionDegreeId);

            if (executionDegree == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            if (executionDegree.getDegreeCurricularPlan() == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            //Degree
            IDegree degree = null;
            degree = executionDegree.getDegreeCurricularPlan().getDegree();

            if (degree == null) {
                throw new FenixServiceException("error.impossibleDegreeInfo");
            }

            //Read degree information
            IPersistentDegreeInfo persistentDegreeInfo = suportePersistente.getIPersistentDegreeInfo();
            List degreeInfoList = persistentDegreeInfo.readDegreeInfoByDegree(degree);

            //Last information about this degree
            if (degreeInfoList != null && degreeInfoList.size() > 0) {
                Collections.sort(degreeInfoList, new BeanComparator("lastModificationDate"));
                IDegreeInfo degreeInfo = (IDegreeInfo) degreeInfoList.get(degreeInfoList.size() - 1);

                infoDegreeInfo = InfoDegreeInfo.newInfoFromDomain(degreeInfo);

                infoDegreeInfo.recaptureNULLs(degreeInfo);

                //return the degree info with the last modification date
                //even if this degree info doesn't belong at execution period
                // used.
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoDegreeInfo;
    }
}

