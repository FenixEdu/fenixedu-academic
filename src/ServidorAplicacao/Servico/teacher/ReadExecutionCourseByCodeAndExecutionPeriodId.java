/*
 * Created on Feb 23, 2005
 * 
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadExecutionCourseByCodeAndExecutionPeriodId implements IServico {

   
    private static ReadExecutionCourseByCodeAndExecutionPeriodId service =
        new ReadExecutionCourseByCodeAndExecutionPeriodId();

    public static ReadExecutionCourseByCodeAndExecutionPeriodId getService() {
        return service;
    }

    private ReadExecutionCourseByCodeAndExecutionPeriodId() {
    }

    public String getNome() {
        return "teacher.ReadExecutionCourseByCodeAndExecutionPeriodId";
    }
   
    public InfoExecutionCourse run(Integer executionPeriodId, String code) throws ExcepcaoInexistente,
    FenixServiceException {

		IExecutionCourse iExecCourse = null;
		InfoExecutionCourse infoExecCourse = null;
		
		try {
		    ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		    IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
		    iExecCourse = executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriodId(code,
		            executionPeriodId);
		    if (iExecCourse != null)
		        infoExecCourse = (InfoExecutionCourse) Cloner.get(iExecCourse);
		} catch (ExcepcaoPersistencia ex) {
		    ex.printStackTrace();
		    FenixServiceException newEx = new FenixServiceException("");
		    newEx.fillInStackTrace();
		    throw newEx;
		}
		
		return infoExecCourse;
	}
   

}
