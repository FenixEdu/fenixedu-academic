/*
 * Created on 5/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteCurricularCoursesOfDegreeCurricularPlan implements IServico {

	private static DeleteCurricularCoursesOfDegreeCurricularPlan service = new DeleteCurricularCoursesOfDegreeCurricularPlan();

	public static DeleteCurricularCoursesOfDegreeCurricularPlan getService() {
		return service;
	}

	private DeleteCurricularCoursesOfDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "DeleteCurricularCoursesOfDegreeCurricularPlan";
	}
	
	// delete a set of curricularCourses
	public List run(List curricularCoursesIds) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

			Iterator iter = curricularCoursesIds.iterator();

			Boolean result = new Boolean(true);
			List undeletedCurricularCourses = new ArrayList();
			Integer curricularCourseId;
			ICurricularCourse curricularCourse;

			while (iter.hasNext()) {

				curricularCourseId = (Integer) iter.next();
				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
				if (curricularCourse != null)
					result = persistentCurricularCourse.delete(curricularCourse);			
				if(result.equals(new Boolean(false))) {
					undeletedCurricularCourses.add((String) curricularCourse.getName());		
					undeletedCurricularCourses.add((String) curricularCourse.getCode());	
				}
			}	
			return undeletedCurricularCourses;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}