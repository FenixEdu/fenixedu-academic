package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurricularCourseByID {

    @Service
    public static InfoCurricularCourse run(Integer curricularCourseID) throws FenixServiceException {
        CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseID);

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
    }
}