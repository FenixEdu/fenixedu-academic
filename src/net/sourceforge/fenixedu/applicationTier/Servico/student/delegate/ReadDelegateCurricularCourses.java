/*
 * Created on Feb 19, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.student.Delegate;
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
        InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse);

        List infoScopes = (List) CollectionUtils.collect(curricularCourse.getScopes(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                        return InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear.newInfoFromDomain(curricularCourseScope);
                    }

                });
        infoCurricularCourse.setInfoScopes(infoScopes);

        return infoCurricularCourse;
    }

    @Override
    protected List doSearch(HashMap searchParameters) throws ExcepcaoPersistencia {

        final String user = (String) searchParameters.get("user");
        final Student student = persistentSupport.getIPersistentStudent().readByUsername(user);
        final Delegate delegate = persistentSupport.getIPersistentDelegate().readByStudent(student);

        // if he's a degree delegate then he can read all curricular courses
        // report
        
        List<CurricularCourse> curricularCourses;
        if (delegate.getType().booleanValue()) {
            curricularCourses = getCurricularCourses(delegate, null);
        } else {
            Integer year = new Integer(delegate.getYearType().getValue());
            curricularCourses = getCurricularCourses(delegate, year);
        }
        
        return curricularCourses;
    }

    private List<CurricularCourse> getCurricularCourses(Delegate delegate, Integer year) {
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        
        for (DegreeModule degreeModule : RootDomainObject.getInstance().getDegreeModules()) {
            if (! (degreeModule instanceof CurricularCourse)) {
                continue;
            }
            
            CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
            
            if (! DegreeCurricularPlanState.ACTIVE.equals(curricularCourse.getDegreeCurricularPlan().getState())) {
                continue;
            }
            
            if (! CurricularStage.OLD.equals(curricularCourse.getCurricularStage())) {
                continue;
            }
            
            if (! delegate.getDegree().equals(curricularCourse.getDegreeCurricularPlan().getDegree())) {
                continue;
            }
            
            boolean rightExecutionYear = false;
            for (ExecutionCourse associatedExecutionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                if (delegate.getExecutionYear().equals(associatedExecutionCourse.getExecutionPeriod().getExecutionYear())) {
                    rightExecutionYear = true;
                    break;
                }
            }
            
            if (! rightExecutionYear) {
                continue;
            }
            
            if (year != null) {
                boolean rightYear = false;
                for (CurricularCourseScope scope : curricularCourse.getScopes()) {
                    if (year.equals(scope.getCurricularSemester().getCurricularYear().getYear())) {
                        rightYear = true;
                        break;
                    }
                }
                
                if (! rightYear) {
                    continue;
                }
            }

            curricularCourses.add(curricularCourse);
        }
        
        return curricularCourses;
    }

}
