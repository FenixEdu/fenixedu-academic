package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author David Santos 10/Jun/2003
 */

public class ReadExecutionDegreesByExecutionYearAndDegreeType implements IServico
{

    private static ReadExecutionDegreesByExecutionYearAndDegreeType service =
        new ReadExecutionDegreesByExecutionYearAndDegreeType();

    public static ReadExecutionDegreesByExecutionYearAndDegreeType getService()
    {
        return service;
    }

    private ReadExecutionDegreesByExecutionYearAndDegreeType()
    {
    }

    public final String getNome()
    {
        return "ReadExecutionDegreesByExecutionYearAndDegreeType";
    }

    public List run(InfoExecutionYear infoExecutionYear, TipoCurso degreeType)
        throws FenixServiceException
    {

        List infoExecutionDegreeList = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
            IExecutionYear executionYear =
                Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
            List executionDegreeList =
                executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear, degreeType);
            if ((executionDegreeList != null) && (!executionDegreeList.isEmpty()))
            {
                infoExecutionDegreeList = new ArrayList();
                Iterator iterator = executionDegreeList.iterator();
                while (iterator.hasNext())
                {
                    ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
                    InfoExecutionDegree infoExecutionDegree =
                        Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
                    infoExecutionDegreeList.add(infoExecutionDegree);
                }
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegreeList;
    }
}