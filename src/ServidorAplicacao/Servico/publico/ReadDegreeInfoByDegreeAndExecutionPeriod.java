package ServidorAplicacao.Servico.publico;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoDegreeInfo;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.ExecutionPeriod;
import Dominio.ICurso;
import Dominio.IDegreeInfo;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeInfo;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 11/Nov/2003
 */
public class ReadDegreeInfoByDegreeAndExecutionPeriod implements IServico
{
    private static ReadDegreeInfoByDegreeAndExecutionPeriod service =
        new ReadDegreeInfoByDegreeAndExecutionPeriod();

    public ReadDegreeInfoByDegreeAndExecutionPeriod()
    {
    }

    public String getNome()
    {
        return "ReadDegreeInfoByDegreeAndExecutionPeriod";
    }

    public static ReadDegreeInfoByDegreeAndExecutionPeriod getService()
    {
        return service;
    }

    public InfoDegreeInfo run(Integer executionPeriodId, Integer degreeId) throws FenixServiceException
    {
        InfoDegreeInfo infoDegreeInfo = null;

        try
        {
            if (degreeId == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            //Execution Period
            IExecutionPeriod executionPeriod = new ExecutionPeriod();

            if (executionPeriodId == null)
            {
               executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            } else
            {
                executionPeriod.setIdInternal(executionPeriodId);

                executionPeriod =
                    (IExecutionPeriod) persistentExecutionPeriod.readByOId(executionPeriod, false);
            }
            
			if (executionPeriod == null)
			{
				throw new FenixServiceException("error.impossibleDegreeSite");
			}
            
            IExecutionYear executionYear = executionPeriod.getExecutionYear();
            if (executionYear == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }
            System.out.println(executionYear);

            //Degree
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            ICurso degree = new Curso();
            degree.setIdInternal(degreeId);

            degree = (ICurso) persistentDegree.readByOId(degree, false);
            if (degree == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            //Degree Info
            IPersistentDegreeInfo persistentDegreeInfo = sp.getIPersistentDegreeInfo();
            List degreeInfoList =
                persistentDegreeInfo.readDegreeInfoByDegreeAndExecutionYear(degree, executionYear);
            if (degreeInfoList == null || degreeInfoList.size() <= 0)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            //Find the degree info most recent
            Collections.sort(degreeInfoList, new BeanComparator("lastModificationDate"));
            IDegreeInfo degreeInfo = (IDegreeInfo) degreeInfoList.get(degreeInfoList.size() - 1);
            infoDegreeInfo = Cloner.copyIDegreeInfo2InfoDegree(degreeInfo);

            if (infoDegreeInfo == null)
            {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoDegreeInfo;
    }
}
