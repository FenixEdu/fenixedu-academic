/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.Predicate;

/**
 * @author lmac1 modified by Fernanda Quiterio
 */
public class DeleteCurricularCoursesOfDegreeCurricularPlan extends Service {

    // delete a set of curricularCourses
    public List run(List curricularCoursesIds) throws FenixServiceException, ExcepcaoPersistencia {

        Iterator iter = curricularCoursesIds.iterator();
        List undeletedCurricularCourses = new ArrayList();

        while (iter.hasNext()) {
			Integer curricularCourseId = (Integer) iter.next();
			CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
            if (curricularCourse != null) {
                //delete curriculum
                Curriculum curriculum =curricularCourse.findLatestCurriculum();
                
				if (curriculum != null) {
                    curricularCourse.removeAssociatedCurriculums(curriculum);
					curriculum.removePersonWhoAltered();
                    
                    curriculum.delete();
                }

                if (!curricularCourse.hasAnyAssociatedExecutionCourses()) {
                    List scopes = curricularCourse.getScopes();
                	if (canAllCurricularCourseScopesBeDeleted(scopes)) {
														
						Iterator iterator = scopes.iterator();
                        while (iterator.hasNext()) {
							try {
								CurricularCourseScope scope = (CurricularCourseScope)iterator.next();
								iterator.remove();
								curricularCourse.removeScopes(scope);
								
								scope.delete();
							}
							catch (DomainException e) {
								
							}
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
                    continue;
                }
            }
        }
        return undeletedCurricularCourses;
    }
	
	
	private Boolean canAllCurricularCourseScopesBeDeleted (List<CurricularCourseScope> scopes) {
		List nonDeletableScopes = (List)CollectionUtils.select(scopes,new Predicate() {
			public boolean evaluate(Object o) {
				CurricularCourseScope ccs = (CurricularCourseScope)o;
				return !ccs.canBeDeleted();
			}});
		
		return nonDeletableScopes.isEmpty();
	}
}