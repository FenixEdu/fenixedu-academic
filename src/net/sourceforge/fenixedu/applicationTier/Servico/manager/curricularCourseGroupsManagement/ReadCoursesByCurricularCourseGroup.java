/*
 * Created on Jul 28, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroupWithCoursesToAdd;
import net.sourceforge.fenixedu.domain.AreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author João Mota
 * 
 */
public class ReadCoursesByCurricularCourseGroup extends Service {

    public InfoCurricularCourseGroupWithCoursesToAdd run(Integer groupId) throws ExcepcaoPersistencia {
        IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSupport
                .getIPersistentCurricularCourseGroup();
        CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) persistentCurricularCourseGroup
                .readByOID(CurricularCourseGroup.class, groupId);

        List courses = curricularCourseGroup.getCurricularCourses();
        List infoCurricularCourses = tranformToListOfInfoCurricularCourses(courses);

        List coursesToAdd = getCoursesToAdd(curricularCourseGroup);
        if (courses != null) {
            coursesToAdd.removeAll(courses);
        }
        List infoCurricularCoursesToAdd = tranformToListOfInfoCurricularCourses(coursesToAdd);
        Collections.sort(infoCurricularCoursesToAdd, new Comparator() {

            public int compare(Object o1, Object o2) {

                return ((InfoCurricularCourse) o1).getName().compareTo(
                        ((InfoCurricularCourse) o2).getName());
            }
        });
        InfoCurricularCourseGroupWithCoursesToAdd composite = new InfoCurricularCourseGroupWithCoursesToAdd();
        composite.setInfoCurricularCourseGroup(InfoCurricularCourseGroup
                .newInfoFromDomain(curricularCourseGroup));
        composite.setInfoCurricularCourses(infoCurricularCourses);
        composite.setInfoCurricularCoursesToAdd(infoCurricularCoursesToAdd);
        return composite;
    }

    /**
     * @param courses
     * @return
     */
    private List tranformToListOfInfoCurricularCourses(List courses) {
        if (courses == null) {
            return new ArrayList();
        }
        return (List) CollectionUtils.collect(courses, new Transformer() {

            public Object transform(Object arg0) {

                return InfoCurricularCourse.newInfoFromDomain((CurricularCourse) arg0);
            }
        });
    }

    /**
     * @param curricularCourseGroup
     * @return
     */
    private List getCoursesToAdd(CurricularCourseGroup curricularCourseGroup) {
        Iterator iter = curricularCourseGroup.getBranch().getDegreeCurricularPlan()
                .getCurricularCourses().iterator();
        List coursesToAdd = new ArrayList();

        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();

            if (!coursesToAdd.contains(curricularCourse)
                    && (curricularCourseGroup instanceof AreaCurricularCourseGroup || (containsScope(
                            curricularCourseGroup, curricularCourse)))) {
                coursesToAdd.add(curricularCourse);
            }
        }
        return coursesToAdd;
    }

    /**
     * @param curricularCourseGroup
     * @param curricularCourse2
     * @return
     */
    private boolean containsScope(CurricularCourseGroup curricularCourseGroup,
            CurricularCourse curricularCourse2) {

        if (curricularCourseGroup.getCurricularCourses() == null
                || curricularCourseGroup.getCurricularCourses().isEmpty()) {
            return true;
        }

        Iterator iter = curricularCourseGroup.getCurricularCourses().iterator();
        boolean result = true;
        while (iter.hasNext() && result) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();
            result = haveCommonCurricularSemester(curricularCourse2.getScopes(), curricularCourse
                    .getScopes());
        }

        return result;
    }

    /**
     * @param scopes2
     * @param scopes
     * @return
     */
    private boolean haveCommonCurricularSemester(List scopes2, List scopes) {
        return !((List) CollectionUtils
                .intersection(getSemestersList(scopes2), getSemestersList(scopes))).isEmpty();
    }

    /**
     * @param scopes
     * @return
     */
    private Collection getSemestersList(List scopes) {
        List semesters = new ArrayList();
        Iterator iter = scopes.iterator();
        while (iter.hasNext()) {
            CurricularCourseScope scope = (CurricularCourseScope) iter.next();
            if (!semesters.contains(scope.getCurricularSemester())) {
                semesters.add(scope.getCurricularSemester());
            }
        }
        return semesters;
    }

}
