package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadCurricularCourseByIdInternal {

    @Service
    public static InfoCurricularCourse run(Integer curricularCourseCode) throws FenixServiceException {
        InfoCurricularCourse infoCurricularCourse = null;
        CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseCode);

        infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
        return infoCurricularCourse;
    }
}