/*
 * Created on Feb 19, 2004
 *  
 */
package ServidorAplicacao.Servico.student.delegate;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDomainObject;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.student.IDelegate;
import ServidorAplicacao.Servico.framework.SearchService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.student.IPersistentDelegate;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class ReadDelegateCurricularCourses extends SearchService {
    /**
     *  
     */
    public ReadDelegateCurricularCourses() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.SearchService#cloneDomainObject(Dominio.IDomainObject)
     */
    protected InfoObject cloneDomainObject(IDomainObject object) {
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

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.SearchService#doSearch(java.util.HashMap,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        List curricularCourses = null;
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IPersistentDelegate persistentDelegate = sp.getIPersistentDelegate();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

        String user = (String) searchParameters.get("user");

        IStudent student = persistentStudent.readByUsername(user);
        IDelegate delegate = persistentDelegate.readByStudent(student);
        IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
        // if he's a degree delegate then he can read all curricular courses
        // report
        if (delegate.getType().booleanValue()) {
            curricularCourses = persistentCurricularCourse
                    .readExecutedCurricularCoursesByDegreeAndExecutionYear(delegate.getDegree(),
                            executionYear);
        } else {
            Integer year = new Integer(delegate.getYearType().getValue());
            curricularCourses = persistentCurricularCourse
                    .readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(delegate.getDegree(),
                            year, executionYear);
        }
        return curricularCourses;
    }
}