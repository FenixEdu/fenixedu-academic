/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class SetBasicCurricularCoursesService implements IServico {

    private static SetBasicCurricularCoursesService _servico = new SetBasicCurricularCoursesService();

    /**
     * The actor of this class.
     */

    private SetBasicCurricularCoursesService() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "setBasicCurricularCourses";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static SetBasicCurricularCoursesService getService() {
        return _servico;
    }

    public boolean run(List curricularCoursesIds, Integer degreeCurricularPlanId)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

            List basicCurricularCourses = persistentCurricularCourse
                    .readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(degreeCurricularPlan,
                            new Boolean(true));

            Iterator itBCCourses = basicCurricularCourses.iterator();
            ICurricularCourse basicCourse;

            while (itBCCourses.hasNext()) {

                basicCourse = (ICurricularCourse) itBCCourses.next();
                persistentCurricularCourse.simpleLockWrite(basicCourse);
                basicCourse.setBasic(new Boolean(false));
            }

            Iterator itId = curricularCoursesIds.iterator();

            while (itId.hasNext()) {

                ICurricularCourse curricularCourseBasic = (ICurricularCourse) persistentCurricularCourse
                        .readByOID(CurricularCourse.class, (Integer) itId.next(), true);
                curricularCourseBasic.setBasic(new Boolean(true));

            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return true;
    }

}