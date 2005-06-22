package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 13/Nov/2003
 */
public class ReadCurrentCurriculumByCurricularCourseCode implements IService {

    public InfoCurriculum run(Integer executionDegreeCode, Integer curricularCourseCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        InfoCurriculum infoCurriculum = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
        IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                .getIPersistentCurricularCourseScope();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
        //selects active curricular course scopes
        List activeCurricularCourseScopes = persistentCurricularCourseScope
                .readActiveCurricularCourseScopesByCurricularCourse(curricularCourse.getIdInternal());

        activeCurricularCourseScopes = (List) CollectionUtils.select(activeCurricularCourseScopes,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
                        if (curricularCourseScope.isActive().booleanValue()) {
                            return true;
                        }
                        return false;
                    }
                });

        //selects execution courses for current execution period
        final IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
       
//        List associatedExecutionCourses = persistentExecutionCourse
//                .readListbyCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
        
        
        List associatedExecutionCourses = new ArrayList();
        List<IExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for(IExecutionCourse executionCourse : executionCourses){
            if(executionCourse.getExecutionPeriod().equals(executionPeriod))
                associatedExecutionCourses.add(executionCourse);
        }
        

        ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
                .getIdInternal());
        if (curriculum == null) {
            curriculum = new Curriculum();
            curriculum.setIdInternal(new Integer(0));
            curriculum.setCurricularCourse(curricularCourse);
        }

        infoCurriculum = createInfoCurriculum(curriculum, persistentExecutionCourse,
                activeCurricularCourseScopes, associatedExecutionCourses);
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(ICurriculum curriculum,
            IPersistentExecutionCourse persistentExecutionCourse, List activeCurricularCourseScopes,
            List associatedExecutionCourses) throws ExcepcaoPersistencia {

        InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse
                .newInfoFromDomain(curriculum);

        List scopes = new ArrayList();

        CollectionUtils.collect(activeCurricularCourseScopes, new Transformer() {
            public Object transform(Object arg0) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;

                return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                        .newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List infoExecutionCourses = new ArrayList();
        Iterator iterExecutionCourses = associatedExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();

            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse);

            Boolean hasSite;
            if(executionCourse.getSite() != null)
                hasSite = true;
            else
                hasSite = false;
            
            infoExecutionCourse.setHasSite(hasSite);
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}