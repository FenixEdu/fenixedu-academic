/*
 * Created on Feb 23, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
