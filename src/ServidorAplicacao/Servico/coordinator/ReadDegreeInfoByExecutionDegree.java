package ServidorAplicacao.Servico.coordinator;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoDegreeInfo;
import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeInfo;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeInfo;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Created on 4/Nov/2003
 */
public class ReadDegreeInfoByExecutionDegree implements IServico {
    private static ReadDegreeInfoByExecutionDegree service = new ReadDegreeInfoByExecutionDegree();

    public static ReadDegreeInfoByExecutionDegree getService() {
        return service;
    }

    public ReadDegreeInfoByExecutionDegree() {
    }

    public final String getNome() {
        return "ReadDegreeInfoByExecutionDegree";
    }

    public InfoDegreeInfo run(Integer infoExecutionDegreeId)
            throws FenixServiceException {
        InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();
        infoDegreeInfo.setIdInternal(new Integer(0));

        try {
            if (infoExecutionDegreeId == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            ISuportePersistente suportePersistente = SuportePersistenteOJB
                    .getInstance();

            //Execution degree
            ICursoExecucaoPersistente cursoExecucaoPersistente = suportePersistente
                    .getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = (ICursoExecucao) cursoExecucaoPersistente
                    .readByOID(CursoExecucao.class, infoExecutionDegreeId);

            if (executionDegree == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            if (executionDegree.getCurricularPlan() == null) {
                throw new FenixServiceException("error.invalidExecutionDegree");
            }

            //Degree
            ICurso degree = null;
            degree = executionDegree.getCurricularPlan().getDegree();

            if (degree == null) {
                throw new FenixServiceException("error.impossibleDegreeInfo");
            }

            //Read degree information
            IPersistentDegreeInfo persistentDegreeInfo = suportePersistente
                    .getIPersistentDegreeInfo();
            List degreeInfoList = persistentDegreeInfo
                    .readDegreeInfoByDegree(degree);

            //Last information about this degree
            if (degreeInfoList != null && degreeInfoList.size() > 0) {
                Collections.sort(degreeInfoList, new BeanComparator(
                        "lastModificationDate"));
                IDegreeInfo degreeInfo = (IDegreeInfo) degreeInfoList
                        .get(degreeInfoList.size() - 1);
                //CLONER
                //infoDegreeInfo = Cloner.copyIDegreeInfo2InfoDegree(degreeInfo);
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

