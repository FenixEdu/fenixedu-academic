/*
 * Created on 2003/07/29
 * 
 *  
 */
package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadExecutionCourseByOID implements IServico
{

    private static ReadExecutionCourseByOID service = new ReadExecutionCourseByOID();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadExecutionCourseByOID getService()
    {
        return service;
    }

    /**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadExecutionCourseByOID";
    }

    public InfoExecutionCourse run(Integer oid) throws FenixServiceException
    {

        InfoExecutionCourse result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionDegreeDAO = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse =
                (IExecutionCourse) executionDegreeDAO.readByOID(ExecutionCourse.class, oid);
            if (executionCourse != null)
            {
                result = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            }

            //			result.setAssociatedInfoCurricularCourses(new ArrayList());
            //			Iterator iterator =
			// executionCourse.getAssociatedCurricularCourses().iterator();
            //			while(iterator.hasNext()) {
            //				ICurricularCourse curricularCourse = (ICurricularCourse)
			// iterator.next();
            //				result.getAssociatedInfoCurricularCourses().add(Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse));
            //			}
            //			if (result.getAssociatedInfoCurricularCourses().isEmpty()) {
            //				result.setAssociatedInfoCurricularCourses(null);
            //			}

        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}
