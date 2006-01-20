package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author T�nia Pous�o Created on 4/Nov/2003
 */
public class ReadDegreeInfoByExecutionDegree extends Service {

    public InfoDegreeInfo run(Integer infoExecutionDegreeId) throws FenixServiceException {
        InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();
        infoDegreeInfo.setIdInternal(new Integer(0));

        try {
            if (infoExecutionDegreeId == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            //Execution degree
            IPersistentExecutionDegree cursoExecucaoPersistente = persistentSupport
                    .getIPersistentExecutionDegree();
            ExecutionDegree executionDegree = (ExecutionDegree) cursoExecucaoPersistente.readByOID(
                    ExecutionDegree.class, infoExecutionDegreeId);

            if (executionDegree == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            if (executionDegree.getDegreeCurricularPlan() == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            //Degree
            Degree degree = executionDegree.getDegreeCurricularPlan().getDegree();

            if (degree == null) {
                throw new FenixServiceException("error.impossibleDegreeInfo");
            }

            //Read degree information
            List degreeInfoList = degree.getDegreeInfos();

            //Last information about this degree
            if (degreeInfoList != null && degreeInfoList.size() > 0) {
                DegreeInfo degreeInfo = (DegreeInfo) Collections.max(degreeInfoList, new BeanComparator("lastModificationDate"));
                infoDegreeInfo = InfoDegreeInfo.newInfoFromDomain(degreeInfo);

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

