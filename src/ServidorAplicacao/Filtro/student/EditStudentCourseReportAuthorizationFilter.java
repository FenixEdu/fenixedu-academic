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

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.gesdis.IStudentCourseReport;
import Dominio.gesdis.StudentCourseReport;
import Dominio.student.IDelegate;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentStudentCourseReport;
import ServidorPersistente.student.IPersistentDelegate;
import Util.DelegateYearType;
import Util.RoleType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class EditStudentCourseReportAuthorizationFilter extends DomainObjectAuthorizationFilter
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
	 */
    protected RoleType getRoleType()
    {
        return RoleType.DELEGATE;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#verifyCondition(ServidorAplicacao.IUserView,
	 *      java.lang.Integer)
	 */
    protected boolean verifyCondition(IUserView id, Integer objectId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IPersistentStudentCourseReport persistentStudentCourseReport =
                sp.getIPersistentStudentCourseReport();
            IPersistentDelegate persistentDelegate = sp.getIPersistentDelegate();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            IStudent student = persistentStudent.readByUsername(id.getUtilizador());
            IStudentCourseReport studentCourseReport =
                (IStudentCourseReport) persistentStudentCourseReport.readByOId(
                    new StudentCourseReport(objectId),
                    false);
            ICurricularCourse curricularCourse = studentCourseReport.getCurricularCourse();
            List scopes = curricularCourse.getScopes();
            List years = (List) CollectionUtils.collect(scopes, new Transformer()
            {
                public Object transform(Object arg0)
                {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                    return curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
                }
            });
            years = removeDuplicates(years);
            IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
            Iterator iter = years.iterator();
            while (iter.hasNext())
            {
                Integer year = (Integer) iter.next();
                IDelegate delegate =
                    persistentDelegate.readByDegreeAndExecutionYearAndYearType(
                        curricularCourse.getDegreeCurricularPlan().getDegree(),
                        executionYear,
                        DelegateYearType.getEnum(year.intValue()));

                if (delegate == null)
                    continue;

                if (delegate.getStudent().equals(student))
                    return true;
            }
            return false;
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            return false;
        }
    }

    /**
	 * @param years
	 * @return
	 */
    private List removeDuplicates(List years)
    {
        List result = new ArrayList();
        Iterator iter = years.iterator();
        while (iter.hasNext())
        {
            Integer year = (Integer) iter.next();
            if (!result.contains(year))
                result.add(year);
        }
        return result;
    }
}
