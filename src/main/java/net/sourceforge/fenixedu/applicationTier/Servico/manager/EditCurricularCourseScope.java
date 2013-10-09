package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCurricularCourseScope {

    @Atomic
    public static void run(InfoCurricularCourseScopeEditor newInfoCurricularCourseScope) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        CurricularCourseScope oldCurricularCourseScope = null;
        CurricularSemester newCurricularSemester = null;
        Branch newBranch = null;

        String branchId = newInfoCurricularCourseScope.getInfoBranch().getExternalId();
        newBranch = FenixFramework.getDomainObject(branchId);

        if (newBranch == null) {
            throw new NonExistingServiceException("message.non.existing.branch", null);
        }

        String curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getExternalId();
        newCurricularSemester = FenixFramework.getDomainObject(curricularSemesterId);

        if (newCurricularSemester == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
        }

        oldCurricularCourseScope = FenixFramework.getDomainObject(newInfoCurricularCourseScope.getExternalId());

        if (oldCurricularCourseScope == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.course.scope", null);
        }

        oldCurricularCourseScope.edit(newBranch, newCurricularSemester, newInfoCurricularCourseScope.getBeginDate(), null,
                newInfoCurricularCourseScope.getAnotation());
    }
}