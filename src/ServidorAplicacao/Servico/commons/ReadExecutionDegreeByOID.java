/*
 * Created on 2003/07/29
 * 
 *  
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoCoordinatorWithInfoPerson;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadExecutionDegreeByOID implements IServico
{

    private static ReadExecutionDegreeByOID service = new ReadExecutionDegreeByOID();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionDegreeByOID getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadExecutionDegreeByOID";
    }

    public InfoExecutionDegree run(Integer oid) throws FenixServiceException
    {

        InfoExecutionDegree infoExecutionDegree = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
            ICursoExecucao executionDegree =
                (ICursoExecucao) executionDegreeDAO.readByOID(CursoExecucao.class, oid);
            if (executionDegree != null)
            {
                //CLONER
                //infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);
                infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus.newInfoFromDomain(executionDegree);
                
                if (executionDegree.getCoordinatorsList() != null)
                {
                    List infoCoordinatorList = new ArrayList();
                    ListIterator iteratorCoordinator =
                        executionDegree.getCoordinatorsList().listIterator();
                    while (iteratorCoordinator.hasNext())
                    {
                        ICoordinator coordinator = (ICoordinator) iteratorCoordinator.next();

                        //infoCoordinatorList.add(Cloner.copyICoordinator2InfoCoordenator(coordinator));
                        infoCoordinatorList.add(InfoCoordinatorWithInfoPerson.newInfoFromDomain(coordinator));
                    }

                    infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
                }
                
				if(executionDegree.getPeriodExamsFirstSemester() != null)
				{
					infoExecutionDegree.setInfoPeriodExamsFirstSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsFirstSemester()));
            }
				if(executionDegree.getPeriodExamsSecondSemester() != null)
				{
					infoExecutionDegree.setInfoPeriodExamsSecondSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsSecondSemester()));
        }
				if(executionDegree.getPeriodLessonsFirstSemester() != null)
				{
					infoExecutionDegree.setInfoPeriodLessonsFirstSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsFirstSemester()));
				}
				if(executionDegree.getPeriodLessonsSecondSemester() != null)
				{
					infoExecutionDegree.setInfoPeriodLessonsSecondSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsSecondSemester()));
				}

            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return infoExecutionDegree;
    }
}
