/*
 * Created on Feb 18, 2004
 *  
 */
package ServidorAplicacao.Filtro.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IStudent;
import Dominio.student.IDelegate;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.student.IPersistentDelegate;
import Util.DelegateYearType;
import Util.RoleType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class StudentCourseReportAuthorizationFilter extends DomainObjectAuthorizationFilter {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.DELEGATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#verifyCondition(ServidorAplicacao.IUserView,
     *      java.lang.Integer)
     */
    protected boolean verifyCondition(IUserView id, Integer objectId) {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IPersistentDelegate persistentDelegate = sp.getIPersistentDelegate();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            IStudent student = persistentStudent.readByUsername(id.getUtilizador());
            IDelegate delegate = persistentDelegate.readByStudent(student);
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, objectId);

            List degreeDelegates = persistentDelegate.readDegreeDelegateByDegreeAndExecutionYear(
                    curricularCourse.getDegreeCurricularPlan().getDegree(), delegate.getExecutionYear());

            // if it's a degree delegate then it's allowed
            if (degreeDelegates.contains(delegate))
                return true;

            List scopes = curricularCourse.getScopes();
            List years = (List) CollectionUtils.collect(scopes, new Transformer() {
                public Object transform(Object arg0) {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                    return curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
                }
            });
            years = removeDuplicates(years);
            Iterator iter = years.iterator();
            while (iter.hasNext()) {
                Integer year = (Integer) iter.next();
                List delegates = persistentDelegate.readByDegreeAndExecutionYearAndYearType(
                        curricularCourse.getDegreeCurricularPlan().getDegree(), delegate.getExecutionYear(),
                        DelegateYearType.getEnum(year.intValue()));

                if (delegates.contains(delegate))
                    return true;
            }
            return false;
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param years
     * @return
     */
    private List removeDuplicates(List years) {
        List result = new ArrayList();
        Iterator iter = years.iterator();
        while (iter.hasNext()) {
            Integer year = (Integer) iter.next();
            if (!result.contains(year))
                result.add(year);
        }
        return result;
    }
}