/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenEvaluationCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.CurricularCourseScopeOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1 modified by Fernanda Quit�rio
 */
public class DeleteCurricularCoursesOfDegreeCurricularPlan implements IService {

    // delete a set of curricularCourses
    public List run(List curricularCoursesIds) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        IPersistentWrittenEvaluationCurricularCourseScope persistentWrittenEvaluationCurricularCourseScope = sp
                .getIPersistentWrittenEvaluationCurricularCourseScope();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

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
                        List scopeIdInternals = (List) CollectionUtils.collect(scopes,
                                new Transformer() {
                                    public Object transform(Object arg0) {
                                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                                        return curricularCourseScope.getIdInternal();
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
}