/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScope implements IService {

    /**
     * Executes the service. Returns the current InfoCurricularCourseScope.
     */
    public InfoCurricularCourseScope run(Integer idInternal) throws FenixServiceException {
        ICurricularCourseScope curricularCourseScope;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            curricularCourseScope = (ICurricularCourseScope) sp.getIPersistentCurricularCourseScope()
                    .readByOID(CurricularCourseScope.class, idInternal);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (curricularCourseScope == null) {
            throw new NonExistingServiceException();
        }

        Integer curricularSemesterId = curricularCourseScope.getCurricularSemester().getIdInternal();
        InfoCurricularCourseScope infoCurricularCourseScope = Cloner
                .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);

        infoCurricularCourseScope.getInfoCurricularSemester().setIdInternal(curricularSemesterId);

        return infoCurricularCourseScope;
    }
}