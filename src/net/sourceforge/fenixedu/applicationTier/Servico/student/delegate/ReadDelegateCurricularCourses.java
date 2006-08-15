/*
 * Created on Feb 19, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadDelegateCurricularCourses extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject object) {
        CurricularCourse curricularCourse = (CurricularCourse) object;
        InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                .newInfoFromDomain(curricularCourse);

        List infoScopes = (List) CollectionUtils.collect(curricularCourse.getScopes(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                        return InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                    }

                });
        infoCurricularCourse.setInfoScopes(infoScopes);

        return infoCurricularCourse;
    }

    @Override
    protected List doSearch(HashMap searchParameters) throws ExcepcaoPersistencia {

        final String user = (String) searchParameters.get("user");
        final Registration student = Registration.readByUsername(user);

        Delegate delegate = null;
        if (!student.getDelegate().isEmpty()) {
            delegate = student.getDelegate().get(0);
        }

        // if he's a degree delegate then he can read all curricular courses
        // report
        List curricularCourses = null;
        if (delegate.getType().booleanValue()) {
            curricularCourses = delegate.getDegree().getExecutedCurricularCoursesByExecutionYear(
                    delegate.getExecutionYear());
        } else {
            Integer year = new Integer(delegate.getYearType().getValue());
            curricularCourses = delegate.getDegree().getExecutedCurricularCoursesByExecutionYearAndYear(
                    delegate.getExecutionYear(), year);
        }
        return curricularCourses;
    }

}
