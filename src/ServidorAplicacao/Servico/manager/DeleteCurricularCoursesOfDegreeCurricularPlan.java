/*
 * Created on 5/Ago/2003
 */
package ServidorAplicacao.Servico.manager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.Transformer;
import commons.CollectionUtils;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentWrittenEvaluationCurricularCourseScope;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.CurricularCourseScopeOJB;
import ServidorPersistente.OJB.SuportePersistenteOJB;
/**
 * @author lmac1
 * modified by Fernanda Quitério
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
			IPersistentCurricularCourse persistentCurricularCourse = sp
					.getIPersistentCurricularCourse();
			IPersistentWrittenEvaluationCurricularCourseScope persistentWrittenEvaluationCurricularCourseScope = sp
					.getIPersistentWrittenEvaluationCurricularCourseScope();
			Iterator iter = curricularCoursesIds.iterator();
			List undeletedCurricularCourses = new ArrayList();
			List executionCourses, scopes;
			Integer curricularCourseId;
			ICurricularCourse curricularCourse;
			while (iter.hasNext()) {
				curricularCourseId = (Integer) iter.next();
				curricularCourse = (ICurricularCourse) persistentCurricularCourse
						.readByOId(new CurricularCourse(curricularCourseId),
								false);
				if (curricularCourse != null) {
					executionCourses = curricularCourse
							.getAssociatedExecutionCourses();
					if (executionCourses == null || executionCourses.isEmpty()) {
						scopes = curricularCourse.getScopes();
						if (scopes != null && !scopes.isEmpty()) {
							// check that scopes are not associated with any written evaluation
							// in case anyone is the correspondent curricular course can not be deleted
							List scopeIdInternals = (List) CollectionUtils
									.collect(scopes, new Transformer() {
										public Object transform(Object arg0) {
											ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
											return curricularCourseScope
													.getIdInternal();
										}
									});
							List writtenEvaluations = persistentWrittenEvaluationCurricularCourseScope
									.readByCurricularCourseScopeList(scopeIdInternals);
							if (writtenEvaluations == null) {
								Iterator iterator = scopes.iterator();
								CurricularCourseScopeOJB scopeOJB = new CurricularCourseScopeOJB();
								while (iterator.hasNext()) {
									scopeOJB.delete((ICurricularCourseScope) iterator.next());
								}
							} else {
								undeletedCurricularCourses.add(curricularCourse
										.getName());
								undeletedCurricularCourses.add(curricularCourse
										.getCode());
								continue;
							}
						}
						persistentCurricularCourse.delete(curricularCourse);
					} else {
						undeletedCurricularCourses.add(curricularCourse
								.getName());
						undeletedCurricularCourses.add(curricularCourse
								.getCode());
					}
				}
			}
			return undeletedCurricularCourses;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}