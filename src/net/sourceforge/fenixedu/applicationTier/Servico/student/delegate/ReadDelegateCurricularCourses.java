/*
 * Created on Feb 19, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.IDelegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadDelegateCurricularCourses extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(IDomainObject object) {
        ICurricularCourse curricularCourse = (ICurricularCourse) object;
        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(curricularCourse);

        List infoScopes = (List) CollectionUtils.collect(curricularCourse.getScopes(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                        return Cloner
                                .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                    }

                });
        infoCurricularCourse.setInfoScopes(infoScopes);

        return infoCurricularCourse;
    }

    @Override
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        final String user = (String) searchParameters.get("user");
        final IStudent student = sp.getIPersistentStudent().readByUsername(user);
        final IDelegate delegate = sp.getIPersistentDelegate().readByStudent(student);

        // if he's a degree delegate then he can read all curricular courses
        // report
        final IPersistentCurricularCourse persistentCurricularCourse = sp
                .getIPersistentCurricularCourse();
        List curricularCourses = null;
        if (delegate.getType().booleanValue()) {
            curricularCourses = persistentCurricularCourse
                    .readExecutedCurricularCoursesByDegreeAndExecutionYear(delegate.getDegree()
                            .getIdInternal(), delegate.getExecutionYear().getIdInternal());
        } else {
            Integer year = new Integer(delegate.getYearType().getValue());
            curricularCourses = persistentCurricularCourse
                    .readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(delegate.getDegree()
                            .getIdInternal(), year, delegate.getExecutionYear().getIdInternal());
        }
        return curricularCourses;
    }

}
