package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Created on 10/Out/2003
 */
public class ReadCurricularCourseScopeListByDegreeCurricularPlan implements IServico {

    private static ReadCurricularCourseScopeListByDegreeCurricularPlan service = new ReadCurricularCourseScopeListByDegreeCurricularPlan();

    /**
     * The singleton access method of this class.
     */
    public static ReadCurricularCourseScopeListByDegreeCurricularPlan getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadCurricularCourseScopeListByDegreeCurricularPlan() {
    }

    /**
     * Service name
     */
    public final String getNome() {
        return "ReadCurricularCourseScopeListByDegreeCurricularPlan";
    }

    /**
     * Executes the service. Returns the current collection of
     * infoCurricularCourses.
     */
    public List run(Integer idDegreeCurricularPlan) throws FenixServiceException {
        ISuportePersistente sp;
        List allCurricularCourses = null;
        List allCurricularCourseScope = new ArrayList();
        try {
            sp = SuportePersistenteOJB.getInstance();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            idDegreeCurricularPlan);
            allCurricularCourses = sp.getIPersistentCurricularCourse()
                    .readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);

            if (allCurricularCourses == null || allCurricularCourses.isEmpty())
                return allCurricularCourses;

            // build the result of this service, ie, curricular course scope's
            // list
            //for each curricular course, add it scopes in the result list
            Iterator iterator = allCurricularCourses.iterator();

            ICurricularCourse curricularCourse = null;
            ListIterator iteratorScopes = null;
            while (iterator.hasNext()) {
                curricularCourse = (ICurricularCourse) iterator.next();

                List curricularCourseScopes = sp.getIPersistentCurricularCourseScope()
                        .readActiveCurricularCourseScopesByCurricularCourse(curricularCourse);

                if (curricularCourseScopes != null) {
                    iteratorScopes = curricularCourseScopes.listIterator();
                    while (iteratorScopes.hasNext()) {
                        allCurricularCourseScope
                                .add(Cloner
                                        .copyICurricularCourseScope2InfoCurricularCourseScope((ICurricularCourseScope) iteratorScopes
                                                .next()));
                    }
                }
            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return allCurricularCourseScope;
    }
}