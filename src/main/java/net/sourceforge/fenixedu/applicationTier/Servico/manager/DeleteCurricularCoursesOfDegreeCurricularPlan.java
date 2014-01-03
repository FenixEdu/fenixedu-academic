/*
 * Created on 5/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import org.apache.commons.collections.CollectionUtils;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1 modified by Fernanda Quiterio
 */
public class DeleteCurricularCoursesOfDegreeCurricularPlan {

    // delete a set of curricularCourses
    @Atomic
    public static List run(List<String> curricularCoursesIds) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        Iterator<String> iter = curricularCoursesIds.iterator();
        List undeletedCurricularCourses = new ArrayList();

        while (iter.hasNext()) {
            String curricularCourseId = iter.next();
            CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);
            if (curricularCourse != null) {
                // delete curriculum
                Curriculum curriculum = curricularCourse.findLatestCurriculum();

                if (curriculum != null) {
                    curricularCourse.removeAssociatedCurriculums(curriculum);
                    curriculum.setPersonWhoAltered(null);

                    curriculum.delete();
                }

                if (!curricularCourse.hasAnyAssociatedExecutionCourses()) {
                    Collection scopes = curricularCourse.getScopes();
                    if (canAllCurricularCourseScopesBeDeleted(scopes)) {

                        Iterator iterator = scopes.iterator();
                        while (iterator.hasNext()) {
                            try {
                                CurricularCourseScope scope = (CurricularCourseScope) iterator.next();
                                iterator.remove();
                                curricularCourse.removeScopes(scope);

                                scope.delete();
                            } catch (DomainException e) {

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

    private static Boolean canAllCurricularCourseScopesBeDeleted(Collection<CurricularCourseScope> scopes) {
        List nonDeletableScopes = (List) CollectionUtils.select(scopes, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                CurricularCourseScope ccs = (CurricularCourseScope) o;
                return !ccs.canBeDeleted();
            }
        });

        return nonDeletableScopes.isEmpty();
    }
}