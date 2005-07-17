/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegreeAndScopes;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadCurricularCourse implements IService {

    /**
     * Executes the service. Returns the current InfoCurricularCourse.
     */
    public InfoCurricularCourse run(Integer idInternal) throws FenixServiceException {
        ICurricularCourse curricularCourse;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOID(
                    CurricularCourse.class, idInternal);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
		
        return InfoCurricularCourseWithInfoDegreeAndScopes.newInfoFromDomain(curricularCourse);
    }
}