/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteProjects;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class ReadExecutionCourseProjects implements IServico {

	private static ReadExecutionCourseProjects _servico =
		new ReadExecutionCourseProjects();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionCourseProjects getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadExecutionCourseProjects() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadExecutionCourseProjects";
	}


	public ISiteComponent run(Integer executionCourseCode)throws FenixServiceException {
		
	InfoSiteProjects infoSiteProjects = new InfoSiteProjects();
		
	try 
	{
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IDisciplinaExecucao executionCourse = (IDisciplinaExecucao)sp.getIDisciplinaExecucaoPersistente().readByOId(new DisciplinaExecucao(executionCourseCode),false);
				
		List executionCourseProjects = sp.getIPersistentGroupProperties().readAllGroupPropertiesByExecutionCourse(executionCourse);
		
		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory =GroupEnrolmentStrategyFactory.getInstance();
		IGroupEnrolmentStrategy strategy =null;
		
			
		List infoGroupPropertiesList = new ArrayList();
		Iterator iterator = executionCourseProjects.iterator();
		
		while (iterator.hasNext()) {
			IGroupProperties groupProperties = (IGroupProperties)iterator.next();
			strategy =enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);
			if(strategy.checkEnrolmentDate(groupProperties,Calendar.getInstance()))
				infoGroupPropertiesList.add(groupProperties);

		}
		infoSiteProjects.setInfoGroupPropertiesList(infoGroupPropertiesList);	
		
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
	}
		
		
	return infoSiteProjects;
}
}