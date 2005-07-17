/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1 modified by Fernanda Quiterio
 */
public class DeleteCurricularCoursesOfDegreeCurricularPlan implements IService {

    // delete a set of curricularCourses
    public List run(List curricularCoursesIds) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
		IPersistentCurricularCourseScope persistentCurricularCourseScope = sp.getIPersistentCurricularCourseScope();

        Iterator iter = curricularCoursesIds.iterator();
        List undeletedCurricularCourses = new ArrayList();
        List executionCourses, scopes;
        Integer curricularCourseId;
        ICurricularCourse curricularCourse;
        while (iter.hasNext()) {
            curricularCourseId = (Integer) iter.next();
            curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, curricularCourseId);
            if (curricularCourse != null) {
                //delete curriculum
                ICurriculum curriculum = persistentCurriculum
                        .readCurriculumByCurricularCourse(curricularCourse.getIdInternal());
                if (curriculum != null) {
                    curriculum.getCurricularCourse().getAssociatedCurriculums().remove(curriculum);
                    curriculum.getPersonWhoAltered().getAssociatedAlteredCurriculums().remove(curriculum);
                    persistentCurriculum.deleteByOID(Curriculum.class, curriculum.getIdInternal());
                }

                executionCourses = curricularCourse.getAssociatedExecutionCourses();
                if (executionCourses == null || executionCourses.isEmpty()) {
                    scopes = curricularCourse.getScopes();
                    if (scopes != null && !scopes.isEmpty()) {
                        // check that scopes are not associated with any
                        // written evaluation
                        // in case anyone is the correspondent curricular
                        // course can not be deleted

                        List allWrittenEvaluations = readWrittenEvaluationsByCurricularCourseScopes(scopes);
                        if (allWrittenEvaluations == null || allWrittenEvaluations.isEmpty()) {
                            Iterator iterator = scopes.iterator();

                            while (iterator.hasNext()) {
								try {
									ICurricularCourseScope scope = (ICurricularCourseScope)iterator.next();
									scope.delete();
								}
								catch (DomainException e) {}
                            }
                        } else {
                            undeletedCurricularCourses.add(curricularCourse.getName());
                            undeletedCurricularCourses.add(curricularCourse.getCode());
                            continue;
                        }
						
                       // persistentCurricularCourse.delete(curricularCourse);
						
                    } else {
                        undeletedCurricularCourses.add(curricularCourse.getName());
                        undeletedCurricularCourses.add(curricularCourse.getCode());
                    }
                }
            }
        }
        return undeletedCurricularCourses;
    }
	
	List<IWrittenEvaluation> readWrittenEvaluationsByCurricularCourseScopes(List<ICurricularCourseScope> scopes) {
		List result = new LinkedList();
		for (ICurricularCourseScope scope : scopes) {
			result.addAll(scope.getAssociatedWrittenEvaluations());
		}
		return result;
	}
}