package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class EditCurricularCourseScope {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(InfoCurricularCourseScopeEditor newInfoCurricularCourseScope) throws FenixServiceException {

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