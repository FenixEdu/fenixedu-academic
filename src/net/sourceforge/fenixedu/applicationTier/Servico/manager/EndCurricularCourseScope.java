package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 28/10/2003
 *  
 */
public class EndCurricularCourseScope implements IService {

    public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

        ICurricularCourseScope oldCurricularCourseScope = null;
        try {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = ps
                    .getIPersistentCurricularCourseScope();

            if (!newInfoCurricularCourseScope.getEndDate().after(
                    newInfoCurricularCourseScope.getBeginDate())) {
                throw new InvalidArgumentsServiceException();
            }

            if (newInfoCurricularCourseScope.getEndDate().after(Calendar.getInstance())) {
                throw new InvalidSituationServiceException();
            }

            oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class,
                            newInfoCurricularCourseScope.getIdInternal(), true);

            if (oldCurricularCourseScope == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.course.scope",
                        null);
            }
            oldCurricularCourseScope.setEndDate(newInfoCurricularCourseScope.getEndDate());

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}