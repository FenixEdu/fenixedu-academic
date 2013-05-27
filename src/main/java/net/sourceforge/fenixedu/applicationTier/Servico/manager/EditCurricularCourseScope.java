package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditCurricularCourseScope {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(InfoCurricularCourseScopeEditor newInfoCurricularCourseScope) throws FenixServiceException {

        CurricularCourseScope oldCurricularCourseScope = null;
        CurricularSemester newCurricularSemester = null;
        Branch newBranch = null;

        Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
        newBranch = RootDomainObject.getInstance().readBranchByOID(branchId);

        if (newBranch == null) {
            throw new NonExistingServiceException("message.non.existing.branch", null);
        }

        Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
        newCurricularSemester = RootDomainObject.getInstance().readCurricularSemesterByOID(curricularSemesterId);

        if (newCurricularSemester == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
        }

        oldCurricularCourseScope = RootDomainObject.getInstance().readCurricularCourseScopeByOID(newInfoCurricularCourseScope.getIdInternal());

        if (oldCurricularCourseScope == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.course.scope", null);
        }

        oldCurricularCourseScope.edit(newBranch, newCurricularSemester, newInfoCurricularCourseScope.getBeginDate(), null,
                newInfoCurricularCourseScope.getAnotation());
    }
}