/*
 * Created on Dec 11, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.execution;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class ReadExecutionDegreesByExecutionYearAndDegreeType implements IService
{

    /**
	 *  
	 */
    public ReadExecutionDegreesByExecutionYearAndDegreeType()
    {
        super();
    }

    public List run(InfoExecutionYear infoExecutionYear, TipoCurso degreeType)
            throws FenixServiceException
    {

        ArrayList infoExecutionDegreeList = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();

            IExecutionYear executionYear = null;
            if (infoExecutionYear == null)
            {
                IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
                executionYear = persistentExecutionYear.readCurrentExecutionYear();
            }
            else
            {
                executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
            }

            List executionDegrees;

            if (degreeType == null)
            {
                executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear);
            }
            else
            {
                executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear,
                        degreeType);
            }

            infoExecutionDegreeList = (ArrayList) CollectionUtils.collect(executionDegrees,
                    new Transformer()
                    {

                        public Object transform(Object input)
                        {
                            ICursoExecucao executionDegree = (ICursoExecucao) input;
                            InfoExecutionDegree infoExecutionDegree = Cloner
                                    .copyIExecutionDegree2InfoExecutionDegree(executionDegree);
                            return infoExecutionDegree;
                        }
                    });
        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", ex);
        }
        return infoExecutionDegreeList;
    }
}