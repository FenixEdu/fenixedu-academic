package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadInterminatedCurricularCourseScopes {

    @Atomic
    public static List<InfoCurricularCourseScope> run(String curricularCourseId) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);

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