package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão 13/Nov/2003
 */
public class ReadCurriculumByCurricularCourseCode implements IService {

    public InfoCurriculum run(Integer curricularCourseCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoCurriculum infoCurriculum = null;
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
        IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                .getIPersistentCurricularCourseScope();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
                .getIdInternal());
        if (curriculum != null) {
            infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
        } else {
            //Although doesn't exist CURRICULUM, an object is returned with
            // the correspond curricular course
            infoCurriculum = new InfoCurriculum();
            infoCurriculum.setInfoCurricularCourse(Cloner
                    .copyCurricularCourse2InfoCurricularCourse(curricularCourse));
        }

        List infoExecutionCourses = buildExecutionCourses(persistentExecutionCourse, curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);

        List activeInfoScopes = buildActiveScopes(persistentCurricularCourseScope, curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(activeInfoScopes);
        return infoCurriculum;
    }

    private List buildExecutionCourses(IPersistentExecutionCourse persistentExecutionCourse,
            ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        List infoExecutionCourses = new ArrayList();
        List executionCourses = curricularCourse.getAssociatedExecutionCourses();
        Iterator iterExecutionCourses = executionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourses.next();
            if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.OPEN)
                    || executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
                        .get(executionCourse);
                infoExecutionCourse.setHasSite(persistentExecutionCourse.readSite(executionCourse
                        .getIdInternal()));
                infoExecutionCourses.add(infoExecutionCourse);
            }
        }
        return infoExecutionCourses;
    }

    private List buildActiveScopes(IPersistentCurricularCourseScope persistentCurricularCourseScope,
            ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        //selects active curricular course scopes
        List activeCurricularCourseScopes = persistentCurricularCourseScope
                .readActiveCurricularCourseScopesByCurricularCourse(curricularCourse);

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

        List activeInfoScopes = (List) CollectionUtils.collect(activeCurricularCourseScopes,
                new Transformer() {

                    public Object transform(Object arg0) {

                        return Cloner
                                .copyICurricularCourseScope2InfoCurricularCourseScope((ICurricularCourseScope) arg0);
                    }
                });
        return activeInfoScopes;
    }
}