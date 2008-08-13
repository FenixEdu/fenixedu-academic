package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurricularCourseByID extends Service {

    public InfoCurricularCourse run(Integer curricularCourseID) throws FenixServiceException{
        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(
                curricularCourseID);

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
    }
}