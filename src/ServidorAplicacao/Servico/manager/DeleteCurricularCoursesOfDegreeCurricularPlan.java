/*
 * Created on 5/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.ICurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.CurricularCourseScopeOJB;
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

			List undeletedCurricularCourses = new ArrayList();
			List executionCourses, scopes;
			Integer curricularCourseId;
			ICurricularCourse curricularCourse;

			while(iter.hasNext()) {

				curricularCourseId = (Integer) iter.next();
				curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
				if(curricularCourse != null) {
					executionCourses = curricularCourse.getAssociatedExecutionCourses();
					if(executionCourses.isEmpty()) {
						scopes = curricularCourse.getScopes();
						if (scopes != null) {
									if(!scopes.isEmpty()) {
										Iterator iterator = scopes.iterator();
										CurricularCourseScopeOJB scopeOJB = new CurricularCourseScopeOJB();
										while(iterator.hasNext()) {
											scopeOJB.delete((CurricularCourseScope) iterator.next());
										}
									}
						}
						persistentCurricularCourse.delete(curricularCourse);
					}
					else {
						undeletedCurricularCourses.add((String) curricularCourse.getName());		
						undeletedCurricularCourses.add((String) curricularCourse.getCode());
					}
				}
			}
				
			return undeletedCurricularCourses;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}