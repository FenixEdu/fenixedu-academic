package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;


public class EditCurricularCourseScope extends Service {

    public void run(InfoCurricularCourseScopeEditor newInfoCurricularCourseScope) throws FenixServiceException, ExcepcaoPersistencia {

        CurricularCourseScope oldCurricularCourseScope = null;
        CurricularSemester newCurricularSemester = null;
        Branch newBranch = null;

        Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
        newBranch = rootDomainObject.readBranchByOID(branchId);

        if (newBranch == null) {
            throw new NonExistingServiceException("message.non.existing.branch", null);
        }

		
        Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
        newCurricularSemester = rootDomainObject.readCurricularSemesterByOID(curricularSemesterId);

        if (newCurricularSemester == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
        }

		
        oldCurricularCourseScope = rootDomainObject.readCurricularCourseScopeByOID(newInfoCurricularCourseScope.getIdInternal());

        if (oldCurricularCourseScope == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.course.scope",
                    null);
        }

		oldCurricularCourseScope.edit(newBranch, newCurricularSemester, newInfoCurricularCourseScope.getBeginDate(),
				null, newInfoCurricularCourseScope.getAnotation());
    }
}