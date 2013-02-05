package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCurricularCourseScopes extends FenixService {

    @Service
    public static List<InfoCurricularCourseScope> run(Integer curricularCourseId) throws FenixServiceException {
        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);

        List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
        if (curricularCourseScopes == null || curricularCourseScopes.isEmpty()) {
            return new ArrayList<InfoCurricularCourseScope>();
        }

        List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>(curricularCourseScopes.size());
        for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
            result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
        }

        return result;
    }

}