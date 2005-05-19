/*
 * Created on 4/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class ReadCurricularCoursesByDegreeCurricularPlan implements IService {

    public List run(final Integer idDegreeCurricularPlan) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistenceSupport
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        idDegreeCurricularPlan);

        final List<ICurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();
        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>(curricularCourses.size());
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            infoCurricularCourses.add(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse));
        }
        return infoCurricularCourses;
    }

}