package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfoWithDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Tânia Pousão Create on 11/Nov/2003
 */
public class ReadDegreeInfoByDegreeAndExecutionPeriod implements IServico {
    private static ReadDegreeInfoByDegreeAndExecutionPeriod service = new ReadDegreeInfoByDegreeAndExecutionPeriod();

    public ReadDegreeInfoByDegreeAndExecutionPeriod() {
    }

    public String getNome() {
        return "ReadDegreeInfoByDegreeAndExecutionPeriod";
    }

    public static ReadDegreeInfoByDegreeAndExecutionPeriod getService() {
        return service;
    }

    public InfoDegreeInfo run(Integer executionPeriodId, Integer degreeId) throws FenixServiceException {
        InfoDegreeInfo infoDegreeInfo = null;

        try {
            if (degreeId == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            SuportePersistenteOJB sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            //Execution Period
            IExecutionPeriod executionPeriod;

            if (executionPeriodId == null) {
                executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            } else {

                executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                        ExecutionPeriod.class, executionPeriodId);
            }

            if (executionPeriod == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            IExecutionYear executionYear = executionPeriod.getExecutionYear();
            if (executionYear == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            //Degree
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeId);
            if (degree == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            //Degree Info
            IPersistentDegreeInfo persistentDegreeInfo = sp.getIPersistentDegreeInfo();
            List degreeInfoList = persistentDegreeInfo.readDegreeInfoByDegreeAndExecutionYear(degree,
                    executionYear);
            if (degreeInfoList == null || degreeInfoList.size() <= 0) {

                //If dosn't exists information belongs to this execution year
                //Find the last information in data base
                degreeInfoList = persistentDegreeInfo.readDegreeInfoByDegree(degree);
                if (degreeInfoList == null || degreeInfoList.size() <= 0) {
                    throw new FenixServiceException("error.impossibleDegreeSite");
                }
            }

            //Find the degree info most recent
            Collections.sort(degreeInfoList, new BeanComparator("lastModificationDate"));
            IDegreeInfo degreeInfo = (IDegreeInfo) degreeInfoList.get(degreeInfoList.size() - 1);
            //CLONER
            //infoDegreeInfo = Cloner.copyIDegreeInfo2InfoDegree(degreeInfo);
            infoDegreeInfo = InfoDegreeInfoWithDegree.newInfoFromDomain(degreeInfo);
            infoDegreeInfo.recaptureNULLs(degreeInfo);

            if (infoDegreeInfo == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoDegreeInfo;
    }
}