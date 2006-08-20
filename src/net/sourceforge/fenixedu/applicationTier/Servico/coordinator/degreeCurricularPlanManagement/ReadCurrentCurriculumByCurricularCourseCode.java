package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 13/Nov/2003
 */
public class ReadCurrentCurriculumByCurricularCourseCode extends Service {

    public InfoCurriculum run(Integer executionDegreeCode, Integer curricularCourseCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
        //selects active curricular course scopes
        List<CurricularCourseScope> activeCurricularCourseScopes = curricularCourse.getActiveScopes(); 
         
        activeCurricularCourseScopes = (List) CollectionUtils.select(activeCurricularCourseScopes,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                        if (curricularCourseScope.isActive().booleanValue()) {
                            return true;
                        }
                        return false;
                    }
                });

        final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
       
        List<ExecutionCourse> associatedExecutionCourses = new ArrayList<ExecutionCourse>();
        List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for(ExecutionCourse executionCourse : executionCourses){
            if(executionCourse.getExecutionPeriod().equals(executionPeriod))
                associatedExecutionCourses.add(executionCourse);
        }

        Curriculum curriculum = curricularCourse.findLatestCurriculum();
        InfoCurriculum infoCurriculum = null; 
        if (curriculum != null) {
            infoCurriculum = InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);            
        } else {
            infoCurriculum = new InfoCurriculumWithInfoCurricularCourse();
            infoCurriculum.setIdInternal(new Integer(0));
            infoCurriculum.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        infoCurriculum = createInfoCurriculum(infoCurriculum, activeCurricularCourseScopes, associatedExecutionCourses);
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(InfoCurriculum infoCurriculum, List activeCurricularCourseScopes,
            List associatedExecutionCourses) throws ExcepcaoPersistencia {

        List scopes = new ArrayList();

        CollectionUtils.collect(activeCurricularCourseScopes, new Transformer() {
            public Object transform(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                return InfoCurricularCourseScope
                        .newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Iterator iterExecutionCourses = associatedExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();

            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse);

            Boolean hasSite;
            if(executionCourse.getSite() != null)
                hasSite = Boolean.TRUE;
            else
                hasSite = Boolean.FALSE;
            
            infoExecutionCourse.setHasSite(hasSite);
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}