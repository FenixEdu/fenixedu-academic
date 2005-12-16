package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author T�nia Pous�o Created on 10/Out/2003
 */
public class ReadCurricularCourseScopeListByDegreeCurricularPlan implements IService {

    public List run(Integer idDegreeCurricularPlan) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp;
        List allCurricularCourses = null;
        List allCurricularCourseScope = new ArrayList();

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        idDegreeCurricularPlan);

        String name = degreeCurricularPlan.getName();
        String degreeName = degreeCurricularPlan.getDegree().getNome();
        String degreeSigla = degreeCurricularPlan.getDegree().getSigla();

        allCurricularCourses = sp.getIPersistentCurricularCourse()
                .readCurricularCoursesByDegreeCurricularPlan(name, degreeName, degreeSigla);

        if (allCurricularCourses == null || allCurricularCourses.isEmpty())
            return allCurricularCourses;

        // build the result of this service, ie, curricular course scope's
        // list
        // for each curricular course, add it scopes in the result list
        Iterator iterator = allCurricularCourses.iterator();

        ICurricularCourse curricularCourse = null;
        ListIterator iteratorScopes = null;
        while (iterator.hasNext()) {
            curricularCourse = (ICurricularCourse) iterator.next();

            List curricularCourseScopes = sp
                    .getIPersistentCurricularCourseScope()
                    .readActiveCurricularCourseScopesByCurricularCourse(curricularCourse.getIdInternal());

            if (curricularCourseScopes != null) {
                iteratorScopes = curricularCourseScopes.listIterator();
                while (iteratorScopes.hasNext()) {
                    allCurricularCourseScope
                            .add(InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear
                                    .newInfoFromDomain((ICurricularCourseScope) iteratorScopes.next()));
                }
            }
        }

        return allCurricularCourseScope;
    }
}