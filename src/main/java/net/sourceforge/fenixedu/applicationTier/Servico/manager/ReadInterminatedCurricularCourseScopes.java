package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadInterminatedCurricularCourseScopes {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List<InfoCurricularCourseScope> run(Integer curricularCourseId) throws FenixServiceException {
        CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseId);

        List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getInterminatedScopes();
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