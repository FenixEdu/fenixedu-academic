/*
 * Created on Jul 28, 2004
 *
 */
package ServidorAplicacao.Servico.manager.curricularCourseGroupsManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseGroup;
import DataBeans.InfoCurricularCourseGroupWithCoursesToAdd;
import Dominio.AreaCurricularCourseGroup;
import Dominio.CurricularCourseGroup;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */
public class ReadCoursesByCurricularCourseGroup implements IService {

    /**
     *  
     */
    public ReadCoursesByCurricularCourseGroup() {

    }

    public InfoCurricularCourseGroupWithCoursesToAdd run(Integer groupId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                    .getIPersistentCurricularCourseGroup();
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) persistentCurricularCourseGroup
                    .readByOID(CurricularCourseGroup.class, groupId);

            List courses = curricularCourseGroup.getCurricularCourses();
            List infoCurricularCourses = tranformToListOfInfoCurricularCourses(courses);

            List coursesToAdd = getCoursesToAdd(curricularCourseGroup);
            List infoCurricularCoursesToAdd = tranformToListOfInfoCurricularCourses(coursesToAdd);

            InfoCurricularCourseGroupWithCoursesToAdd composite = new InfoCurricularCourseGroupWithCoursesToAdd();
            composite.setInfoCurricularCourseGroup(InfoCurricularCourseGroup
                    .newInfoFromDomain(curricularCourseGroup));
            composite.setInfoCurricularCourses(infoCurricularCourses);
            composite.setInfoCurricularCoursesToAdd(infoCurricularCoursesToAdd);
            return composite;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param courses
     * @return
     */
    private List tranformToListOfInfoCurricularCourses(List courses) {
        if (courses == null) {
            return null;
        }
        return (List) CollectionUtils.collect(courses, new Transformer() {

            public Object transform(Object arg0) {

                return InfoCurricularCourse.newInfoFromDomain((ICurricularCourse) arg0);
            }
        });
    }

    /**
     * @param curricularCourseGroup
     * @return
     */
    private List getCoursesToAdd(ICurricularCourseGroup curricularCourseGroup) {
        Iterator iter = curricularCourseGroup.getBranch().getDegreeCurricularPlan()
                .getCurricularCourses().iterator();
        List coursesToAdd = new ArrayList();

        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();

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
    private boolean containsScope(ICurricularCourseGroup curricularCourseGroup,
            ICurricularCourse curricularCourse2) {
        Iterator iter = curricularCourseGroup.getCurricularCourses().iterator();
        boolean result = true;
        while (iter.hasNext() && result) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
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
            ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
            if (!semesters.contains(scope.getCurricularSemester())) {
                semesters.add(scope.getCurricularSemester());
            }
        }
        return semesters;
    }
}