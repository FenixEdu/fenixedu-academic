package ServidorAplicacao.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quitério
 * 22/Dez/2003
 * 
 */
public class ReadInfoExecutionCourseByOID implements IServico {

	private static ReadInfoExecutionCourseByOID service = new ReadInfoExecutionCourseByOID();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadInfoExecutionCourseByOID getService() {
		return service;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadInfoExecutionCourseByOID() {
	}


	public final String getNome() {
		return "ReadInfoExecutionCourseByOID";
	}

	public InfoExecutionCourse run(Integer executionCourseOID) throws FenixServiceException {

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoExecucaoPersistente persistentExecutionCourse = sp.getICursoExecucaoPersistente();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			
			if (executionCourseOID == null) {
				throw new FenixServiceException("nullId");
			}
			
			IExecutionCourse executionCourse = new ExecutionCourse();
			executionCourse.setIdInternal(executionCourseOID);

			executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);

			List curricularCourses = executionCourse.getAssociatedCurricularCourses();
			

			Iterator iterator = curricularCourses.iterator();
			List infoCurricularCourses = new ArrayList();

			CollectionUtils.collect(curricularCourses, new Transformer()
			{
				public Object transform(Object input)
				{
					ICurricularCourse curricularCourse  =(ICurricularCourse) input;
						
					return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				}
			}, infoCurricularCourses);

			infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
			infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}
		return infoExecutionCourse;
	}
}