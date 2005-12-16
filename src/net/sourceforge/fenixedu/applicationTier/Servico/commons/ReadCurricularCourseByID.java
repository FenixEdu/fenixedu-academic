package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurricularCourseByID implements IService {

    public InfoCurricularCourse run(Integer curricularCourseID) throws FenixServiceException, ExcepcaoPersistencia {

        ICurricularCourse curricularCourse = null;
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject persistentObject = sp.getIPersistentObject();
        curricularCourse = (ICurricularCourse) persistentObject.readByOID(CurricularCourse.class,
                curricularCourseID);

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
    }
}